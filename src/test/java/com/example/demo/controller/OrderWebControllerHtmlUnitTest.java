package com.example.demo.controller;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.services.OrderService;
import com.example.demo.services.ProductService;

@WebMvcTest(controllers = OrderWebController.class)
class OrderWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockitoBean
	private OrderService orderService;

	@MockitoBean
	private ProductService productService;

	@Test
	void test_HomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/orders");
		assertThat(page.getTitleText()).isEqualTo("Orders");
	}

	@Test
	void testHomePageWithNoOrders() throws Exception {
		when(orderService.getAllOrders()).thenReturn(emptyList());
		HtmlPage page = webClient.getPage("/orders");
		assertThat(page.getBody().getTextContent()).contains("No order");
	}

	@Test
	void test_HomePage_ShouldProvideALinkForCreatingANewOrder() throws Exception {
		HtmlPage page = webClient.getPage("/orders");
		HtmlAnchor newLink = page.getAnchorByText("New order");
		assertThat(newLink.getHrefAttribute()).isEqualTo("/orders/new");
	}

	@Test
	void test_HomePageWithOrders_ShowsTable() throws Exception {
		Product p1 = new Product(1L, "Mouse", 20);
		Product p2 = new Product(2L, "Laptop", 1500);

		Order o = new Order(1L, LocalDate.parse("2025-08-10"));

		o.setProducts(new java.util.LinkedHashSet<>(asList(p1, p2)));

		when(orderService.getAllOrders()).thenReturn(asList(o));

		HtmlPage page = webClient.getPage("/orders");
		HtmlTable table = page.getHtmlElementById("order_table");

		String normalized = removeWindowsCR(table.asNormalizedText());

		assertThat(normalized).isEqualTo(
				"Orders\n" + "ID\tDate\tProducts\tEdit\tDelete\n" + "1\t2025-08-10\tMouse, Laptop\tEdit\tDelete");
	}

	@Test
	void testEditNonExistentOrder() throws Exception {
		when(orderService.getOrderById(1L)).thenReturn(null);
		HtmlPage page = webClient.getPage("/orders/edit/1");
		assertThat(page.getBody().getTextContent()).contains("No order found with id: 1");
	}

	@Test
	void testEditExistingOrder() throws Exception {
		Order o = new Order(1L, LocalDate.parse("2025-05-15"));
		when(orderService.getOrderById(1L)).thenReturn(o);

		HtmlPage page = webClient.getPage("/orders/edit/1");
		HtmlForm form = page.getFormByName("order_record");

		form.getInputByValue("2025-05-15").setValueAttribute("2025-06-01");
		form.getButtonByName("btn_submit").click();

		verify(orderService).updateOrderById(1L, new Order(1L, LocalDate.parse("2025-06-01")));
	}

	@Test
	void testEditNewOrder() throws Exception {
		HtmlPage page = webClient.getPage("/orders/new");
		HtmlForm form = page.getFormByName("order_record");

		form.getInputByName("orderDate").setValueAttribute("2025-09-09");
		form.getButtonByName("btn_submit").click();
		
		verify(orderService).insertNewOrder(new Order(null, LocalDate.parse("2025-09-09")));
	}

	@Test
	void testDeleteOrder_MessageShown() throws Exception {
		doNothing().when(orderService).deleteOrderById(4L);

		HtmlPage page = webClient.getPage("/orders/delete/4");

		verify(orderService).deleteOrderById(4L);

		assertThat(page.getBody().getTextContent()).contains("Order with ID 4 was deleted.");

		HtmlButton newButton = page.getElementByName("btn_new_order");
		assertThat(newButton).isNotNull();

		HtmlButton allButton = page.getElementByName("btn_all_orders");
		assertThat(allButton).isNotNull();
	}

	private String removeWindowsCR(String s) {
		return s.replace("\r", "");
	}
}