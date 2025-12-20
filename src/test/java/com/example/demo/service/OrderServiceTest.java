package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.services.OrderService;

class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		orderService = new OrderService(orderRepository);
	}

	@Test
	void testGetOrderById() {
		Order order = new Order(1L, LocalDate.of(2025, 9, 5));
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		Order result = orderService.getOrderById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(LocalDate.of(2025, 9, 5), result.getOrderDate());
		verify(orderRepository, times(1)).findById(1L);
	}

	@Test
	void testGetOrderByIdNotFound() {
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		Order result = orderService.getOrderById(1L);

		assertNull(result);
		verify(orderRepository, times(1)).findById(1L);
	}

	@Test
	void testInsertNewOrder() {
		Product product1 = new Product(1L, "Laptop", 1200.0);
		Product product2 = new Product(2L, "Mouse", 25.0);

		Order order = new Order(null, LocalDate.of(2025, 9, 5));
		Set<Product> products = new HashSet<>();
		products.add(product1);
		products.add(product2);
		order.setProducts(products);

		Order savedOrder = new Order(1L, LocalDate.of(2025, 9, 5));
		savedOrder.setProducts(products);

		when(orderRepository.save(order)).thenReturn(savedOrder);

		Order result = orderService.insertNewOrder(order);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(LocalDate.of(2025, 9, 5), result.getOrderDate());
		assertEquals(2, result.getProducts().size());
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	void testUpdateOrderById() {
		Product product1 = new Product(1L, "Laptop", 1200.0);

		Order existingOrder = new Order(1L, LocalDate.of(2025, 9, 5));
		Set<Product> existingProducts = new HashSet<>();
		existingProducts.add(product1);
		existingOrder.setProducts(existingProducts);

		Order updatedOrder = new Order(1L, LocalDate.of(2025, 9, 6));
		updatedOrder.setProducts(existingProducts);

		when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);

		Order result = orderService.updateOrderById(1L, updatedOrder);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(LocalDate.of(2025, 9, 6), result.getOrderDate());
		verify(orderRepository, times(1)).save(updatedOrder);
	}

	@Test
	void testDeleteOrderById() {
		long id = 1L;
		doNothing().when(orderRepository).deleteById(id);

		orderService.deleteOrderById(id);

		verify(orderRepository, times(1)).deleteById(id);
	}

	@Test
	void testGetAllOrders() {
		Order order1 = new Order(1L, LocalDate.of(2025, 9, 5));
		Order order2 = new Order(2L, LocalDate.of(2025, 9, 6));

		when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

		List<Order> orders = orderService.getAllOrders();

		assertNotNull(orders);
		assertEquals(2, orders.size());
		verify(orderRepository, times(1)).findAll();
	}

	@Test
	void insertNewOrder_mustNullOutId_beforeCallingSave() {
		Product p1 = new Product(1L, "Laptop", 1200.00);
		Product p2 = new Product(2L, "Mouse", 25.00);

		Order input = new Order(null, LocalDate.of(2025, 9, 5));
		Set<Product> products = new HashSet<>();
		products.add(p1);
		products.add(p2);

		input.setProducts(products);
		Order saved = new Order(1L, LocalDate.of(2025, 9, 5));
		saved.setProducts(products);

		when(orderRepository.save(any())).thenReturn(saved);

		Order result = orderService.insertNewOrder(input);

		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderRepository).save(captor.capture());

		assertThat(captor.getValue().getId()).isNull();
		assertThat(result).isSameAs(saved);
	}

	@Test
	void updateOrderById_mustSetCorrectId_beforeCallingSave() {
		Product p1 = new Product(1L, "Laptop", 1200.00);

		Order existingOrder = new Order(1L, LocalDate.of(2025, 9, 5));
		Set<Product> existingProducts = new HashSet<>();
		existingProducts.add(p1);
		existingOrder.setProducts(existingProducts);

		Order updatedOrder = new Order(1L, LocalDate.of(2025, 9, 6));
		updatedOrder.setProducts(existingProducts);

		when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);

		Order result = orderService.updateOrderById(1L, updatedOrder);

		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderRepository).save(captor.capture());

		assertThat(captor.getValue().getId()).isEqualTo(1L);
		assertThat(result).isSameAs(updatedOrder);
	}

}