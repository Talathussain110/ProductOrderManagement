package com.example.demo.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.model.Product;

@DataJpaTest
public class ProductJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testJpaMapping() {
        Product saved = entityManager.persistFlushFind(new Product(null, "Laptop", 1500.00));

    }
}