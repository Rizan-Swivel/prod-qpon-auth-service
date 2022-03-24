package com.swivel.cc.auth.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class tests the {@link BaseController} class.
 */
class BaseControllerTest {

    private static final String URI = "/";

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        initMocks(this);
        BaseController baseController = new BaseController();
        mockMvc = MockMvcBuilders.standaloneSetup(baseController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnOk() throws Exception {
        String html = "<h1>QPon Auth Service</h1>";
        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(content().string(html));
    }

}