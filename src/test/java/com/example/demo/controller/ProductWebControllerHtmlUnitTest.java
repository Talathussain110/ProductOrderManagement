package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@WebMvcTest(controllers = ProductWebController.class)
class ProductWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private ProductService productService;

	@Test
	void test_HomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/products");
		assertThat(page.getTitleText()).isEqualTo("Products");
	}

	@Test
	void testHomePageWithNoProducts() throws Exception {
		when(productService.getAllProducts()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/products");
		assertThat(page.getBody().getTextContent()).contains("No product");
	}

	@Test
	void test_HomePage_ShouldProvideALinkForCreatingANewProduct() throws Exception {
		HtmlPage page = webClient.getPage("/products");
		assertThat(page.getAnchorByText("New product").getHrefAttribute()).isEqualTo("/products/new");
	}

	@Test
	void test_HomePageWithProducts_ShouldShowThemInATable() throws Exception {
		Product p1 = new Product(1L, "Laptop", 1500.00);
		Product p2 = new Product(2L, "Mouse", 25.50);
		when(productService.getAllProducts()).thenReturn(asList(p1, p2));

		HtmlPage page = webClient.getPage("/products");
		assertThat(page.getBody().getTextContent()).doesNotContain("No product");

		HtmlTable table = page.getHtmlElementById("product_table");
		String normalized = removeWindowsCR(table.asNormalizedText());

		assertThat(normalized).isEqualTo("""
				Products
				ID\tName\tPrice\tEdit\tDelete
				1\tLaptop\t1500.0\tEdit\tDelete
				2\tMouse\t25.5\tEdit\tDelete""");

		page.getAnchorByHref("/products/edit/1");
		page.getAnchorByHref("/products/edit/2");
	}

	@Test
	void testEditNonExistentProduct() throws Exception {
		when(productService.getProductById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/products/edit/1");
		assertThat(page.getBody().getTextContent()).contains("No product found with id: 1");
	}

	@Test
	void testEditExistentProduct() throws Exception {
		Product original = new Product(1L, "Original", 99.99);
		when(productService.getProductById(1L)).thenReturn(original);

		HtmlPage page = webClient.getPage("/products/edit/1");
		HtmlForm form = page.getFormByName("product_record");

		form.getInputByValue("Original").setValueAttribute("Updated");
		form.getInputByValue("99.99").setValueAttribute("149.99");

		form.getButtonByName("btn_submit").click();

		verify(productService).updateProductById(1L, new Product(1L, "Updated", 149.99));
	}

	@Test
	void testEditNewProduct() throws Exception {
		HtmlPage page = webClient.getPage("/products/new");
		HtmlForm form = page.getFormByName("product_record");

		form.getInputByName("name").setValueAttribute("Keyboard");
		form.getInputByName("price").setValueAttribute("55.75");
		form.getButtonByName("btn_submit").click();

		verify(productService).insertNewProduct(new Product(null, "Keyboard", 55.75));
	}

	@Test
	void testDeleteProduct_ShouldDisplayConfirmationMessage() throws Exception {
		doNothing().when(productService).deleteProductById(3L);

		HtmlPage page = webClient.getPage("/products/delete/3");

		verify(productService, times(1)).deleteProductById(3L);

		String content = page.getBody().getTextContent();
		assertThat(content).contains("Product with ID 3 was deleted.");

		HtmlButton newButton = page.getElementByName("btn_new_product");
		assertThat(newButton).isNotNull();

		HtmlButton allButton = page.getElementByName("btn_all_products");
		assertThat(allButton).isNotNull();
	}

	private String removeWindowsCR(String s) {
		return s.replace("\r", "");
	}
}