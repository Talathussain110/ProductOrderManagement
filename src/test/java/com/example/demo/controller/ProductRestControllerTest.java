package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
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
	public void testCreateProduct() throws Exception {
		Product newProduct = new Product(3L, "Smartphone", 500.00);
		when(productService.insertNewProduct(any(Product.class))).thenReturn(newProduct);

		String newProductJson = """
				{
				  "name": "Smartphone",
				  "price": 500.00
				}
				""";

		this.mvc.perform(post("/api/products/new").contentType(MediaType.APPLICATION_JSON).content(newProductJson))
				.andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.name", is("Smartphone")))
				.andExpect(jsonPath("$.price", is(500.00)));
	}

	@Test
	public void testUpdateProductExisting() throws Exception {
		Product updatedProduct = new Product(1L, "Laptop Pro", 1700.00);
		when(productService.updateProductById(anyLong(), any(Product.class))).thenReturn(updatedProduct);

		String updateProductJson = """
				{
				  "name": "Laptop Pro",
				  "price": 1700.00
				}
				""";

		mvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content(updateProductJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Laptop Pro"))).andExpect(jsonPath("$.price", is(1700.00)));
	}

	@Test
	public void testUpdateProductNotFound() throws Exception {
		when(productService.updateProductById(anyLong(), any(Product.class))).thenReturn(null);

		String updateProductJson = """
				{
				  "name": "Nonexistent Product",
				  "price": 999.99
				}
				""";

		mvc.perform(put("/api/products/99").contentType(MediaType.APPLICATION_JSON).content(updateProductJson))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

	@Test
	public void testDeleteProduct() throws Exception {
		doNothing().when(productService).deleteProductById(anyLong());

		mvc.perform(delete("/api/products/1")).andExpect(status().isOk()).andExpect(content().string(""));
	}
}