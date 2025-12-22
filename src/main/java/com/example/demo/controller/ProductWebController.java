package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Product;
import com.example.demo.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductWebController {

	@Autowired
	private ProductService productService;

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String PRODUCT_ATTRIBUTE = "product";
	private static final String PRODUCTS_ATTRIBUTE = "products";

	@GetMapping
	public String listProducts(Model model) {
		List<Product> allProducts = productService.getAllProducts();
		model.addAttribute(PRODUCTS_ATTRIBUTE, allProducts);
		model.addAttribute(MESSAGE_ATTRIBUTE, allProducts.isEmpty() ? "No product" : "");
		return "product";
	}

	@GetMapping("/edit/{id}")
	public String editProduct(@PathVariable long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute(PRODUCT_ATTRIBUTE, product);
		model.addAttribute(MESSAGE_ATTRIBUTE, product == null ? "No product found with id: " + id : "");
		return "edit_product";
	}

	@GetMapping("/new")
	public String newProduct(Model model) {
		model.addAttribute(PRODUCT_ATTRIBUTE, new Product());
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		return "edit_product";
	}

	@PostMapping("/save")
	public String saveProduct(Product product) {
		if (product.getId() == null) {
			productService.insertNewProduct(product);
		} else {
			productService.updateProductById(product.getId(), product);
		}
		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable long id, Model model) {
		productService.deleteProductById(id);
		model.addAttribute("deletedId", id);
		return "delete_product";
	}
}