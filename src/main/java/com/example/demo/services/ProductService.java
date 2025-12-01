package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;

@Service
public class ProductService {

    private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";

    public List<Product> getAllProducts() {
        throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
    }
}
