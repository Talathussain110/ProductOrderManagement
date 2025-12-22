package com.example.demo.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderListPage {

	private final WebDriver driver;
	private final String url;
	private final WebDriverWait wait;

	public OrderListPage(WebDriver driver, int port) {
		this.driver = driver;
		this.url = "http://localhost:" + port + "/orders";
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	public OrderListPage open() {
		driver.get(url);
		wait.until(d -> d.findElement(By.tagName("body")));
		return this;
	}

	public boolean isEmpty() {
		return driver.findElements(By.id("order_table")).isEmpty();
	}

	public String tableText() {
		List<WebElement> tables = driver.findElements(By.id("order_table"));
		return tables.isEmpty() ? "" : tables.get(0).getText();
	}

	public OrderFormPage clickNew() {
		driver.findElement(By.cssSelector("a[href='/orders/new']")).click();
		return new OrderFormPage(driver);
	}

	public OrderFormPage clickEdit(long id) {
		driver.findElement(By.cssSelector("a[href='/orders/edit/" + id + "']")).click();
		return new OrderFormPage(driver);
	}

	public void clickDelete(long id) {
		driver.findElement(By.cssSelector("a[href='/orders/delete/" + id + "']")).click();
	}
}