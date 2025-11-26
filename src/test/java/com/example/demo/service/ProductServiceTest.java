package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.services.ProductService;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1L, "Laptop", 1200.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(1200.0, result.getPrice());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product result = productService.getProductById(1L);

        assertNull(result);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testInsertNewProduct() {
        Product product = new Product(null, "Laptop", 1200.0);
        Product savedProduct = new Product(1L, "Laptop", 1200.0);

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.insertNewProduct(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(1200.0, result.getPrice());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProduct() {
        Product existingProduct = new Product(1L, "Laptop", 1200.0);
        Product updatedProduct = new Product(1L, "Laptop Pro", 1500.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProductById(1L, updatedProduct);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop Pro", result.getName());
        assertEquals(1500.0, result.getPrice());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    public void testDeleteProduct() {
        long id = 1L;
        doNothing().when(productRepository).deleteById(id);

        productService.deleteProductById(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product(1L, "Laptop", 1200.0);
        Product product2 = new Product(2L, "Smartphone", 800.0);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Laptop", products.get(0).getName());
        assertEquals("Smartphone", products.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }
}