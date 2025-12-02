package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;

	@Test
	public void testAllProductsEmpty() throws Exception {
		when(productService.getAllProducts()).thenReturn(List.of());

		mvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	public void testAllProductsNotEmpty() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Product p2 = new Product(2L, "Mouse", 25.00);

		when(productService.getAllProducts()).thenReturn(List.of(p1, p2));

		String expectedJson = "[" + "{\"id\":1,\"name\":\"Laptop\",\"price\":1500.0},"
				+ "{\"id\":2,\"name\":\"Mouse\",\"price\":25.0}" + "]";

		mvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}

	@Test
	public void testProductByIdWithExistingProduct() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);

		when(productService.getProductById(1L)).thenReturn(p1);

		mvc.perform(get("/api/products/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.name", is("Laptop")))
				.andExpect(jsonPath("$.price", is(1500.00)));
	}

	@Test
	public void testProductByIdWithNotFoundProduct() throws Exception {
		when(productService.getProductById(1L)).thenReturn(null);

		mvc.perform(get("/api/products/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}
}