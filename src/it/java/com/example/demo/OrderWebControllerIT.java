package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

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

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.pages.OrderFormPage;
import com.example.demo.pages.OrderListPage;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderWebControllerIT {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private OrderListPage listPage;

	@BeforeEach
	void setup() {
		driver = new HtmlUnitDriver();
		orderRepository.deleteAll();
		productRepository.deleteAll();
		listPage = new OrderListPage(driver, port);
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	void test_HomePageWithOrders_ShowsDateAndProducts() {
		Product p = productRepository.save(new Product(null, "Laptop", 1500));
		Order o = new Order(null, LocalDate.of(2025, 1, 10));
		o.getProducts().add(p);
		orderRepository.save(o);

		listPage.open();
		assertThat(listPage.tableText()).contains("2025-01-10", "Laptop");
	}

	@Test
	void test_CanCreateOrderViaForm() {
		productRepository.save(new Product(null, "Mouse", 20));

		OrderFormPage form = listPage.open().clickNew();
		form.setOrderDate("2025-06-01").selectProduct("Mouse").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("2025-06-01", "Mouse");
	}

	@Test
	void test_CanUpdateOrder() {
		Product p = productRepository.save(new Product(null, "Keyboard", 100));
		Order o = new Order(null, LocalDate.of(2025, 5, 10));
		o.getProducts().add(p);
		orderRepository.save(o);

		OrderFormPage form = listPage.open().clickEdit(o.getId());
		form.setOrderDate("2025-05-20").submit();

		listPage.open();
		assertThat(listPage.tableText()).contains("2025-05-20");
	}

	@Test
	void test_CanDeleteOrder() {
		Order o = orderRepository.save(new Order(null, LocalDate.now()));

		listPage.open().clickDelete(o.getId());

		assertThat(driver.findElement(By.tagName("h1")).getText())
				.contains("Order with ID " + o.getId() + " was deleted.");

		driver.findElement(By.cssSelector("form[action='/orders'] button")).click();
		listPage.open();
		assertThat(listPage.tableText()).doesNotContain(o.getId().toString());
	}
}