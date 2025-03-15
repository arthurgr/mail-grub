package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngredientController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/ingredient")
	public Ingredient ingredient(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Ingredient(counter.incrementAndGet(), String.format(template, name));
	}
}