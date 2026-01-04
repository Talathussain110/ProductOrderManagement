package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.services.OrderService;
import com.example.demo.services.ProductService;

@WebMvcTest(controllers = OrderWebController.class)
class OrderWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private OrderService orderService;

	@MockitoBean
	private ProductService productService;

	@Test
	void testStatus200_ListView() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testReturnOrderView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "order");
	}

	@Test
	void test_ListView_ShowsOrders() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.0);
		Product p2 = new Product(2L, "Mouse", 20.0);

		Order o = new Order(1L, LocalDate.parse("2025-08-10"));
		o.setProducts(new HashSet<>(List.of(p1, p2)));

		when(orderService.getAllOrders()).thenReturn(asList(o));

		mvc.perform(get("/")).andExpect(view().name("order")).andExpect(model().attribute("orders", asList(o)))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_ListView_ShowsMessageWhenNoOrders() throws Exception {
		when(orderService.getAllOrders()).thenReturn(emptyList());

		mvc.perform(get("/")).andExpect(view().name("order")).andExpect(model().attribute("orders", emptyList()))
				.andExpect(model().attribute("message", "No order"));
	}

	@Test
	void test_EditOrder_WhenFound() throws Exception {
		Order order = new Order(1L, LocalDate.parse("2025-05-15"));
		when(orderService.getOrderById(1L)).thenReturn(order);

		mvc.perform(get("/edit/1")).andExpect(view().name("edit_order"))
				.andExpect(model().attribute("order", order)).andExpect(model().attribute("message", ""));
	}

	@Test
	void test_EditOrder_WhenNotFound() throws Exception {
		when(orderService.getOrderById(1L)).thenReturn(null);

		mvc.perform(get("/edit/1")).andExpect(view().name("edit_order"))
				.andExpect(model().attribute("order", nullValue()))
				.andExpect(model().attribute("message", "No order found with id: 1"));
	}

	@Test
	void test_EditNewOrder() throws Exception {
		mvc.perform(get("/new")).andExpect(view().name("edit_order"))
				.andExpect(model().attribute("order", new Order())).andExpect(model().attribute("message", ""));

		verifyNoMoreInteractions(orderService);
	}

	@Test
	void test_PostOrderWithoutId_ShouldInsertNewOrder() throws Exception {
		mvc.perform(post("/save").param("orderDate", "2025-09-01")).andExpect(view().name("redirect:/"));

		verify(orderService).insertNewOrder(new Order(null, LocalDate.parse("2025-09-01")));
	}

	@Test
	void test_PostOrderWithId_ShouldUpdateExistingOrder() throws Exception {
		mvc.perform(post("/save").param("id", "5").param("orderDate", "2025-10-20"))
				.andExpect(view().name("redirect:/"));

		verify(orderService).updateOrderById(5L, new Order(5L, LocalDate.parse("2025-10-20")));
	}

	@Test
	void test_DeleteOrder() throws Exception {
		mvc.perform(get("/delete/7")).andExpect(status().isOk()).andExpect(view().name("delete_order"))
				.andExpect(model().attribute("deletedId", 7L));

		verify(orderService).deleteOrderById(7L);
	}

	@Test
	void saveOrder_withProducts_performsLookupAndSetsRealProducts() throws Exception {

		Product p = new Product(2L, "Laptop", 1500.0);
		when(productService.getProductById(2L)).thenReturn(p);

		mvc.perform(post("/save").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("orderDate", "2025-06-01").param("products", "2")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderService).insertNewOrder(captor.capture());

		Order saved = captor.getValue();

		assertThat(saved.getOrderDate()).isEqualTo(LocalDate.parse("2025-06-01"));
		assertThat(saved.getProducts()).hasSize(1).containsExactly(p);
	}
	
	@Test
	void saveOrder_withInvalidProductId_skipsAddingProduct() throws Exception {
		when(productService.getProductById(99L)).thenReturn(null);

		mvc.perform(post("/save").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("orderDate", "2025-07-01").param("products", "99")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));

		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderService).insertNewOrder(captor.capture());

		Order saved = captor.getValue();

		assertThat(saved.getOrderDate()).isEqualTo(LocalDate.parse("2025-07-01"));
		assertThat(saved.getProducts()).isEmpty();
	}

}