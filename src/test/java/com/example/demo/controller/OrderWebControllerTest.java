package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.services.OrderService;

@WebMvcTest(controllers = OrderWebController.class)
class OrderWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OrderService orderService;

	@Test
	void testStatus200_ListView() throws Exception {
		mvc.perform(get("/orders")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void testReturnOrderView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/orders")).andReturn().getModelAndView(), "order");
	}

	@Test
	void test_ListView_ShowsOrders() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Order o1 = new Order(1L, LocalDate.of(2025, 1, 10));
		o1.setProducts(Set.of(p1));

		List<Order> orders = asList(o1);

		when(orderService.getAllOrders()).thenReturn(orders);

		mvc.perform(get("/orders")).andExpect(view().name("order")).andExpect(model().attribute("orders", orders))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_ListView_ShowsMessageWhenNoOrders() throws Exception {
		when(orderService.getAllOrders()).thenReturn(emptyList());

		mvc.perform(get("/orders")).andExpect(view().name("order")).andExpect(model().attribute("orders", emptyList()))
				.andExpect(model().attribute("message", "No order"));
	}

	@Test
	void test_EditOrder_WhenOrderIsFound() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Order order = new Order(1L, LocalDate.of(2025, 1, 10));
		order.setProducts(Set.of(p1));

		when(orderService.getOrderById(1L)).thenReturn(order);

		mvc.perform(get("/orders/edit/1")).andExpect(view().name("edit_order")).andExpect(model().attribute("order", order))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	void test_EditOrder_WhenOrderIsNotFound() throws Exception {
		when(orderService.getOrderById(1L)).thenReturn(null);

		mvc.perform(get("/orders/edit/1")).andExpect(view().name("edit_order"))
				.andExpect(model().attribute("order", nullValue()))
				.andExpect(model().attribute("message", "No order found with id: 1"));
	}

	@Test
	void test_EditNewOrder() throws Exception {
		mvc.perform(get("/orders/new")).andExpect(view().name("order"))
				.andExpect(model().attribute("order", new Order())).andExpect(model().attribute("message", ""));

		verifyNoMoreInteractions(orderService);
	}

	@Test
	void test_PostOrderWithoutId_ShouldInsertNewOrder() throws Exception {
		mvc.perform(post("/orders/save").param("orderDate", "2025-01-10")).andExpect(view().name("redirect:/orders"));

		verify(orderService).insertNewOrder(new Order(null, LocalDate.parse("2025-01-10")));
	}

	@Test
	void test_PostOrderWithId_ShouldUpdateExistingOrder() throws Exception {
		mvc.perform(post("/orders/save").param("id", "2").param("orderDate", "2025-02-20"))
				.andExpect(view().name("redirect:/orders"));

		verify(orderService).updateOrderById(2L, new Order(2L, LocalDate.parse("2025-02-20")));
	}

	@Test
	void test_DeleteOrder() throws Exception {
		mvc.perform(delete("/orders/delete/3")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/orders"));

		verify(orderService).deleteOrderById(3L);
	}

}