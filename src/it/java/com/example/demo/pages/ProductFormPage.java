package com.example.demo.pages;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductFormPage {

	private final WebDriver driver;

	public ProductFormPage(WebDriver driver) {
		this.driver = driver;
		new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> d.findElement(By.name("product_record")));
	}

	public ProductFormPage setName(String name) {
		WebElement e = driver.findElement(By.id("name"));
		e.clear();
		e.sendKeys(name);
		return this;
	}

	public ProductFormPage setPrice(String price) {
		WebElement e = driver.findElement(By.id("price"));
		e.clear();
		e.sendKeys(price);
		return this;
	}

	public ProductListPage submit() {
		driver.findElement(By.cssSelector("button[type=submit]")).click();
		return new ProductListPage(driver, 0);
	}
}