package com.example.demo.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {

	private Long id;
	private LocalDate orderDate;
	private Set<Product> products = new HashSet<>();

	public Order(Long id, LocalDate orderDate) {
		this.id = id;
		this.orderDate = orderDate;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderDate=" + orderDate + ", products=" + products + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderDate, products);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id) && Objects.equals(orderDate, other.orderDate)
				&& Objects.equals(products, other.products);
	}
}