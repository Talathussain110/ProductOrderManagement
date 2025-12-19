package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Product;

@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void firstLearningTest() {
		Product product = new Product(null, "Laptop", 1500.00);
		Product savedProduct = repository.save(product);

		List<Product> products = repository.findAll();
		assertThat(products).containsExactly(savedProduct);
	}

	@Test
	public void secondLearningTest() {
		Product product = new Product(null, "Phone", 500.00);
		Product savedProduct = entityManager.persistFlushFind(product);

		List<Product> products = repository.findAll();
		assertThat(products).containsExactly(savedProduct);
	}

	@Test
	public void testCreateProduct() {
		Product product = new Product(null, "Laptop", 1500.00);
		Product saved = repository.save(product);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getName()).isEqualTo("Laptop");
		assertThat(saved.getPrice()).isEqualTo(1500.00);
	}

	@Test
	public void testFindById() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));

		Optional<Product> found = repository.findById(product.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getName()).isEqualTo("Laptop");
	}

	@Test
	public void testFindByName() {
		Product saved = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Product found = repository.findByName("Laptop");

		assertThat(found).isEqualTo(saved);
	}

	@Test
	public void testFindByPriceGreaterThan() {
		Product p1 = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Product p2 = entityManager.persistFlushFind(new Product(null, "Phone", 500.00));

		List<Product> found = repository.findAllByPriceGreaterThan(1000.00);

		assertThat(found).containsExactly(p1);
	}

	@Test
	public void testFindAllProducts() {
		Product p1 = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));
		Product p2 = entityManager.persistFlushFind(new Product(null, "Phone", 500.00));

		List<Product> found = repository.findAll();

		assertThat(found).containsExactlyInAnyOrder(p1, p2);
	}

	@Test
	public void testUpdateProduct() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));

		product.setName("Updated Laptop");
		product.setPrice(1600.00);
		Product updated = repository.save(product);
		entityManager.flush();
		entityManager.clear();

		Product reloaded = repository.findById(updated.getId()).orElseThrow();
		assertThat(reloaded.getName()).isEqualTo("Updated Laptop");
		assertThat(reloaded.getPrice()).isEqualTo(1600.00);
	}

	@Test
	public void testDeleteProduct() {
		Product product = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));

		repository.deleteById(product.getId());
		entityManager.flush();

		boolean exists = repository.existsById(product.getId());
		assertThat(exists).isFalse();
	}
}