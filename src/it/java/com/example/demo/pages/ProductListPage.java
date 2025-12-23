package com.example.demo.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductListPage {

	private final WebDriver driver;
	private final String url;
	private final WebDriverWait wait;

	public ProductListPage(WebDriver driver, int port) {
		this.driver = driver;
		this.url = "http://localhost:" + port + "/products";
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	}

	public ProductListPage open() {
		driver.get(url);
		wait.until(d -> d.findElement(By.tagName("body")));
		return this;
	}

	public String tableText() {
		List<WebElement> tables = driver.findElements(By.id("product_table"));
		return tables.isEmpty() ? "" : tables.get(0).getText();
	}

	public ProductFormPage clickNew() {
		driver.findElement(By.cssSelector("a[href='/products/new']")).click();
		return new ProductFormPage(driver);
	}

	public ProductFormPage clickEdit(long id) {
		driver.findElement(By.cssSelector("a[href='/products/edit/" + id + "']")).click();
		return new ProductFormPage(driver);
	}

	public void clickDelete(long id) {
		driver.findElement(By.cssSelector("a[href='/products/delete/" + id + "']")).click();
	}
}