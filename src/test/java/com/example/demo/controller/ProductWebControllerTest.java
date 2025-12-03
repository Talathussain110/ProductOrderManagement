package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductWebController.class)
class ProductWebControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testStatus200() throws Exception {
        mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
    }
}