package com.example.demo.pages;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderFormPage {

	private final WebDriver driver;
	private final WebDriverWait wait;

	public OrderFormPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(d -> d.findElement(By.name("order_record")));
	}

	public OrderFormPage setOrderDate(String date) {
		WebElement e = driver.findElement(By.id("orderDate"));
		e.clear();
		e.sendKeys(date);
		return this;
	}

	public OrderFormPage selectProduct(String productName) {
		WebElement select = driver.findElement(By.cssSelector(".product-select"));
		new Select(select).selectByVisibleText(productName);
		return this;
	}

	public OrderListPage submit() {
		driver.findElement(By.cssSelector("button[type=submit]")).click();
		return new OrderListPage(driver, 0);
	}
}