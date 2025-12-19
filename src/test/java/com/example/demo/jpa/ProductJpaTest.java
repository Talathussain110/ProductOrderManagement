package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Product;

@DataJpaTest
class ProductJpaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testJpaMapping() {
		Product saved = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));

		assertThat(saved.getName()).isEqualTo("Laptop");
		assertThat(saved.getPrice()).isEqualTo(1500.00);
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isPositive();

		LoggerFactory.getLogger(ProductJpaTest.class).info("Saved Product: " + saved.toString());
	}
}