package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductServiceRepositoryIT {

	@SuppressWarnings("resource")
	@Container
	static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("productorder_management").withUsername("admin").withPassword("pass");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		registry.add("spring.jpa.show-sql", () -> "true");
	}

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	public Product defaultProduct;

	@BeforeEach
	void setup() {
		productRepository.deleteAll();
		productRepository.deleteAll();
		productRepository.flush();
		productRepository.flush();
		defaultProduct = productRepository.save(new Product(null, "Laptop", 1200.0));
	}

	@Test
	void testInsertNewProduct() {
		Product saved = productService.insertNewProduct(new Product(null, "Laptop", 1200.0));
		assertThat(saved.getId()).isNotNull();
		assertThat(productRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void testGetAllProducts() {
		productRepository.deleteAll();

		productService.insertNewProduct(new Product(null, "Laptop", 1200.0));
		productService.insertNewProduct(new Product(null, "Smartphone", 800.0));

		List<Product> all = productService.getAllProducts();
		assertThat(all).hasSize(2).extracting(Product::getName).containsExactlyInAnyOrder("Laptop", "Smartphone");
	}

	@Test
	void testUpdateProductById() {
		Product original = productService.insertNewProduct(new Product(null, "Laptop", 1200.0));

		Product updatedProduct = new Product(original.getId(), "Laptop Pro", 1500.0);
		Product updated = productService.updateProductById(original.getId(), updatedProduct);

		assertThat(updated.getPrice()).isEqualTo(1500.0);
		Product fromDb = productRepository.findById(original.getId()).orElseThrow();
		assertThat(fromDb.getPrice()).isEqualTo(1500.0);
	}

	@Test
	void testDeleteProductById() {
		Product product = productService.insertNewProduct(new Product(null, "Laptop", 1200.0));

		productService.deleteProductById(product.getId());

		assertThat(productRepository.findById(product.getId())).isEmpty();
	}
}