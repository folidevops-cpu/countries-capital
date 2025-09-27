package com.example.countrycapital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class CountryController {

    private static final Map<String, String> countries = new LinkedHashMap<>();
    static {
        countries.put("USA", "Washington, D.C.");
        countries.put("France", "Paris");
        countries.put("Germany", "Berlin");
        countries.put("Japan", "Tokyo");
        countries.put("India", "New Delhi");
        countries.put("Canada", "Ottawa");
        countries.put("Brazil", "Brasília");
        countries.put("Australia", "Canberra");
        countries.put("China", "Beijing");
        countries.put("Italy", "Rome");
        countries.put("United Kingdom", "London");
        countries.put("South Africa", "Pretoria");
        countries.put("Mexico", "Mexico City");
        countries.put("Russia", "Moscow");
        countries.put("Spain", "Madrid");
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("countries", countries.keySet());
        return "index";
    }
    
    @GetMapping("/countries")
    public String countries(Model model) {
        model.addAttribute("countries", countries.keySet());
        return "countries";
    }

    @ResponseBody
    @GetMapping("/capital")
    public String getCapital(@RequestParam String country) {
        return countries.getOrDefault(country, "Unknown");
    }
}
