package com.example.countrycapital.controller;package com.example    @Test

    void shouldReturnIndexPage() throws Exception {

import org.junit.jupiter.api.Test;        mockMvc.perform(get("/"))

import org.springframework.beans.factory.annotation.Autowired;                .andExpect(status().isOk())

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;                .andExpect(view().name("index"))

import org.springframework.test.web.servlet.MockMvc;                .andExpect(model().attributeExists("countries"));rycapital.controller;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = CountryController.class, excludeAutoConfiguration = {import org.springframework.context.annotation.Import;

    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,import org.springframework.security.test.context.support.WithMockUser;

    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.classimport org.springframework.test.web.servlet.MockMvc;

})

class CountryControllerTest {import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @Autowired

    private MockMvc mockMvc;@WebMvcTest(controllers = CountryController.class, excludeAutoConfiguration = {

    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,

    @Test    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class

    void shouldReturnIndexPage() throws Exception {})

        mockMvc.perform(get("/"))class CountryControllerTest {

                .andExpect(status().isOk())

                .andExpect(view().name("index"))    @Autowired

                .andExpect(model().attributeExists("countries"));    private MockMvc mockMvc;

    }

    @Test

    @Test    void shouldReturnIndexPage() throws Exception {

    void shouldReturnCapitalForValidCountry() throws Exception {        mockMvc.perform(get("/"))

        mockMvc.perform(get("/capital").param("country", "USA"))                .andExpect(status().isOk())

                .andExpect(status().isOk())                .andExpect(view().name("index"))

                .andExpect(content().string("Washington, D.C."));                .andExpect(model().attributeExists("countries"));

    }    }



    @Test    @Test

    void shouldReturnCapitalForFrance() throws Exception {    void shouldReturnCapitalForValidCountry() throws Exception {

        mockMvc.perform(get("/capital").param("country", "France"))        mockMvc.perform(get("/capital").param("country", "USA"))

                .andExpect(status().isOk())                .andExpect(status().isOk())

                .andExpect(content().string("Paris"));                .andExpect(content().string("Washington, D.C."));

    }    }



    @Test    @Test

    void shouldReturnCapitalForJapan() throws Exception {    void shouldReturnCapitalForFrance() throws Exception {

        mockMvc.perform(get("/capital").param("country", "Japan"))        mockMvc.perform(get("/capital").param("country", "France"))

                .andExpect(status().isOk())                .andExpect(status().isOk())

                .andExpect(content().string("Tokyo"));                .andExpect(content().string("Paris"));

    }    }



    @Test    @Test

    void shouldReturnUnknownForInvalidCountry() throws Exception {    void shouldReturnCapitalForJapan() throws Exception {

        mockMvc.perform(get("/capital").param("country", "InvalidCountry"))        mockMvc.perform(get("/capital").param("country", "Japan"))

                .andExpect(status().isOk())                .andExpect(status().isOk())

                .andExpect(content().string("Unknown"));                .andExpect(content().string("Tokyo"));

    }    }



    @Test    @Test

    void shouldHandleMissingCountryParameter() throws Exception {    void shouldReturnUnknownForInvalidCountry() throws Exception {

        mockMvc.perform(get("/capital"))        mockMvc.perform(get("/capital").param("country", "InvalidCountry"))

                .andExpect(status().isBadRequest());                .andExpect(status().isOk())

    }                .andExpect(content().string("Unknown"));

}    }

    @Test
    void shouldHandleMissingCountryParameter() throws Exception {
        mockMvc.perform(get("/capital"))
                .andExpect(status().isBadRequest());
    }
}