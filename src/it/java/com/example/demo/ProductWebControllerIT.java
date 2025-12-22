package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.demo.model.Product;
import com.example.demo.pages.ProductFormPage;
import com.example.demo.pages.ProductListPage;
import com.example.demo.repository.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductWebControllerIT {

	@Autowired
	private ProductRepository productRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private ProductListPage listPage;

	@BeforeEach
	void setup() {
		driver = new HtmlUnitDriver();
		productRepository.deleteAll();
		listPage = new ProductListPage(driver, port);
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	void test_HomePageWithProducts_ShowsNameAndPrice() {
		productRepository.save(new Product(null, "Laptop", 1500.00));

		listPage.open();
		assertThat(listPage.tableText()).contains("Laptop", "1500");
	}

	@Test
	void test_CanCreateProductViaForm() {
		ProductFormPage form = listPage.open().clickNew();
		form.setName("Mouse").setPrice("25").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("Mouse", "25");
	}

	@Test
	void test_CanUpdateProduct() {
		Product p = productRepository.save(new Product(null, "Phone", 500));

		ProductFormPage form = listPage.open().clickEdit(p.getId());
		form.setName("Smartphone").setPrice("800").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("Smartphone", "800");
	}

	@Test
	void test_CanDeleteProduct() {
		Product p = productRepository.save(new Product(null, "DeleteMe", 10));

		listPage.open().clickDelete(p.getId());

		assertThat(driver.findElement(By.tagName("h1")).getText())
				.contains("Product with ID " + p.getId() + " was deleted.");

		driver.findElement(By.cssSelector("form[action='/products'] button")).click();
		listPage.open();
		assertThat(listPage.tableText()).doesNotContain("DeleteMe");
	}
}