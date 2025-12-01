package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.services.OrderService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OrderService orderService;

	@Test
	public void testAllOrdersEmpty() throws Exception {
		when(orderService.getAllOrders()).thenReturn(List.of());

		mvc.perform(get("/api/orders").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	public void testAllOrdersNotEmpty() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Product p2 = new Product(2L, "Mouse", 25.00);

		Order o1 = new Order(1L, LocalDate.parse("2025-01-10"));
		o1.setProducts(Set.of(p1, p2));

		Order o2 = new Order(2L, LocalDate.parse("2025-02-15"));
		o2.setProducts(Set.of(p2));

		when(orderService.getAllOrders()).thenReturn(List.of(o1, o2));

		String expectedJson = "["
				+ "{\"id\":1,\"orderDate\":\"2025-01-10\",\"products\":[{\"id\":1,\"name\":\"Laptop\",\"price\":1500.0},{\"id\":2,\"name\":\"Mouse\",\"price\":25.0}]},"
				+ "{\"id\":2,\"orderDate\":\"2025-02-15\",\"products\":[{\"id\":2,\"name\":\"Mouse\",\"price\":25.0}]}"
				+ "]";

		mvc.perform(get("/api/orders").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(expectedJson));
	}
}
