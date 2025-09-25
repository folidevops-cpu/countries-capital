package com.example.countrycapital.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("countries"));
    }

    @Test
    void shouldReturnCapitalForValidCountry() throws Exception {
        mockMvc.perform(get("/capital").param("country", "USA"))
                .andExpect(status().isOk())
                .andExpect(content().string("Washington, D.C."));
    }

    @Test
    void shouldReturnCapitalForFrance() throws Exception {
        mockMvc.perform(get("/capital").param("country", "France"))
                .andExpect(status().isOk())
                .andExpect(content().string("Paris"));
    }

    @Test
    void shouldReturnCapitalForJapan() throws Exception {
        mockMvc.perform(get("/capital").param("country", "Japan"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tokyo"));
    }

    @Test
    void shouldReturnUnknownForInvalidCountry() throws Exception {
        mockMvc.perform(get("/capital").param("country", "InvalidCountry"))
                .andExpect(status().isOk())
                .andExpect(content().string("Unknown"));
    }

    @Test
    void shouldHandleMissingCountryParameter() throws Exception {
        mockMvc.perform(get("/capital"))
                .andExpect(status().isBadRequest());
    }
}