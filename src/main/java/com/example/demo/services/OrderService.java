package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Order;

@Service
public class OrderService {

	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

	public List<Order> getAllOrders() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}

	public Order getOrderById(long id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
	}
	
	public Order insertNewOrder(Order order) {
        throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
    }

    public Order updateOrderById(long id, Order replacement) {
        throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
    }

    public void deleteOrderById(long id) {
        throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
    }
}