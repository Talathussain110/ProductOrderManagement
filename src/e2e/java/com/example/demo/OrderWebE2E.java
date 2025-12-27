package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

class OrderWebE2E {

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	private static String baseUrl = "http://localhost:" + port;

	private WebDriver driver;

	@BeforeAll
	static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	void setup() {
		driver = new ChromeDriver();
	}

	@AfterEach
	void cleanup() {
		if (driver != null) {
			driver.quit();
		}
	}

	private void createProduct(String name, String price) {
		driver.get(baseUrl + "/products/new");
		driver.findElement(By.name("name")).sendKeys(name);
		driver.findElement(By.name("price")).sendKeys(price);
		driver.findElement(By.name("btn_submit")).click();

		new WebDriverWait(driver, Duration.ofSeconds(5))
				.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), name));
	}

	@Test
	void test_CreateOrder_WithProduct() {
		createProduct("Laptop", "1500");
		createProduct("Mouse", "25");

		String date = "2025-10-10";

		driver.get(baseUrl + "/orders/new");

		WebElement dateField = driver.findElement(By.name("orderDate"));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + date + "';", dateField);

		WebElement selectEl = driver.findElement(By.cssSelector("select.product-select"));
		Select select = new Select(selectEl);
		select.selectByVisibleText("Laptop");

		driver.findElement(By.id("add-product-btn")).click();

		WebElement secondSelectEl = driver.findElements(By.cssSelector("select.product-select")).get(1);
		new Select(secondSelectEl).selectByVisibleText("Mouse");

		driver.findElement(By.name("btn_submit")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_table")));

		WebElement table = driver.findElement(By.id("order_table"));
		String tableText = table.getText();

		assertThat(tableText).contains(date);
		assertThat(tableText).contains("Laptop");
		assertThat(tableText).contains("Mouse");
	}

	@Test
	void test_DeleteOrder_WithProduct() {

		createProduct("Keyboard", "80");

		String date = "2025-11-11";

		driver.get(baseUrl + "/orders/new");

		WebElement dateField = driver.findElement(By.name("orderDate"));
		((JavascriptExecutor) driver).executeScript("arguments[0].value='" + date + "';", dateField);

		WebElement selectEl = driver.findElement(By.cssSelector("select.product-select"));
		new Select(selectEl).selectByVisibleText("Keyboard");

		driver.findElement(By.name("btn_submit")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), date));

		driver.get(baseUrl + "/orders");
		WebElement row = driver.findElement(By.xpath("//tr[td/text() = '" + date + "']"));
		row.findElement(By.linkText("Delete")).click();

		String h1 = driver.findElement(By.tagName("h1")).getText();
		assertThat(h1).contains("Order with ID");

		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("btn_all_orders"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_table")));

		String afterDelete = driver.findElement(By.tagName("body")).getText();
		assertThat(afterDelete).doesNotContain(date);
	}
}