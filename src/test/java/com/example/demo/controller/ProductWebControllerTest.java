package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@WebMvcTest(controllers = ProductWebController.class)
class ProductWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ProductService productService;

	@Test
	void testStatus200_ListView() throws Exception {
		mvc.perform(get("/products")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testReturnProductView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/products")).andReturn().getModelAndView(), "product");
	}

	@Test
	void test_ListView_ShowsProducts() throws Exception {
		List<Product> products = asList(new Product(1L, "Laptop", 1500.00));
		when(productService.getAllProducts()).thenReturn(products);

		mvc.perform(get("/products")).andExpect(view().name("product"))
				.andExpect(model().attribute("products", products)).andExpect(model().attribute("message", ""));
	}

	@Test
	void test_ListView_ShowsMessageWhenNoProducts() throws Exception {
		when(productService.getAllProducts()).thenReturn(emptyList());

		mvc.perform(get("/products")).andExpect(view().name("product"))
				.andExpect(model().attribute("products", emptyList()))
				.andExpect(model().attribute("message", "No product"));
	}

	@Test
	void test_EditProduct_WhenFound() throws Exception {
		Product p = new Product(1L, "Mouse", 25.50);
		when(productService.getProductById(1L)).thenReturn(p);

		mvc.perform(get("/products/edit/1")).andExpect(view().name("edit_product"))
				.andExpect(model().attribute("product", p)).andExpect(model().attribute("message", ""));
	}

	@Test
	void test_EditProduct_WhenNotFound() throws Exception {
		when(productService.getProductById(1L)).thenReturn(null);

		mvc.perform(get("/products/edit/1")).andExpect(view().name("edit_product"))
				.andExpect(model().attribute("product", nullValue()))
				.andExpect(model().attribute("message", "No product found with id: 1"));
	}

	@Test
	void test_EditNewProduct() throws Exception {
		mvc.perform(get("/products/new")).andExpect(view().name("edit_product"))
				.andExpect(model().attribute("product", new Product())).andExpect(model().attribute("message", ""));

		verifyNoMoreInteractions(productService);
	}

	@Test
	void test_PostProductWithoutId_ShouldInsertNewProduct() throws Exception {
		mvc.perform(post("/products/save").param("name", "Keyboard").param("price", "99.99"))
				.andExpect(view().name("redirect:/products"));

		verify(productService).insertNewProduct(new Product(null, "Keyboard", 99.99));
	}

	@Test
	void test_PostProductWithId_ShouldUpdateExistingProduct() throws Exception {
		mvc.perform(post("/products/save").param("id", "2").param("name", "Monitor").param("price", "250.00"))
				.andExpect(view().name("redirect:/products"));

		verify(productService).updateProductById(2L, new Product(2L, "Monitor", 250.00));
	}

	@Test
	void test_DeleteProduct() throws Exception {
		mvc.perform(get("/products/delete/3")).andExpect(status().isOk()).andExpect(view().name("delete_product"))
				.andExpect(model().attribute("deletedId", 3L));

		verify(productService).deleteProductById(3L);
	}
}