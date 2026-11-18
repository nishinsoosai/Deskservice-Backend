package com.example.Deskservice.service.config;

import com.example.Deskservice.Entity.*;
import com.example.Deskservice.Repository.*;



import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class CategoryInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.findByName("General").isEmpty()) {

            categoryRepository.save(new Category("General"));

            System.out.println("General category inserted");
        }

        if (categoryRepository.findByName("Equipment").isEmpty()) {

            categoryRepository.save(new Category("Equipment"));

            System.out.println("Equipment category inserted");
        }
    }
}