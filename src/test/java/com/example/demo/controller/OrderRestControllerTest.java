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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.services.OrderService;

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

	@Test
	public void testCreateOrder() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Order newOrder = new Order(3L, LocalDate.parse("2025-08-10"));
		newOrder.setProducts(Set.of(p1));

		when(orderService.insertNewOrder(any(Order.class))).thenReturn(newOrder);

		String newOrderJson = """
				{
				  "orderDate": "2025-08-10",
				  "products": [{"id": 1, "name": "Laptop", "price": 1500.00}]
				}
				""";

		this.mvc.perform(post("/api/orders/new").contentType(MediaType.APPLICATION_JSON).content(newOrderJson))
				.andExpect(jsonPath("$.id", is(3))).andExpect(jsonPath("$.orderDate", is("2025-08-10")))
				.andExpect(jsonPath("$.products[0].id", is(1))).andExpect(jsonPath("$.products[0].name", is("Laptop")));
	}

	@Test
	public void testUpdateOrderExisting() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Order updatedOrder = new Order(1L, LocalDate.parse("2025-06-20"));
		updatedOrder.setProducts(Set.of(p1));

		when(orderService.updateOrderById(anyLong(), any(Order.class))).thenReturn(updatedOrder);

		String updateOrderJson = """
				{
				  "orderDate": "2025-06-20",
				  "products": [{"id": 1, "name": "Laptop", "price": 1500.00}]
				}
				""";

		mvc.perform(put("/api/orders/1").contentType(MediaType.APPLICATION_JSON).content(updateOrderJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.orderDate", is("2025-06-20")));
	}

	@Test
	public void testUpdateOrderNotFound() throws Exception {
		when(orderService.updateOrderById(anyLong(), any(Order.class))).thenReturn(null);

		String updateOrderJson = """
				{
				  "orderDate": "2025-06-20",
				  "products": [{"id": 1, "name": "Laptop", "price": 1500.00}]
				}
				""";

		mvc.perform(put("/api/orders/99").contentType(MediaType.APPLICATION_JSON).content(updateOrderJson))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

	@Test
	public void testDeleteOrder() throws Exception {
		doNothing().when(orderService).deleteOrderById(anyLong());

		mvc.perform(delete("/api/orders/1")).andExpect(status().isOk()).andExpect(content().string(""));
	}
}