package com.example.Deskservice.service.config;

import com.example.Deskservice.Entity.*;
import com.example.Deskservice.Repository.*;



import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;

@Component
public class CategoryAssignmentInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final DeskRecordRepository deskRecordRepository;

    public CategoryAssignmentInitializer(
            CategoryRepository categoryRepository,
            DeskRecordRepository deskRecordRepository) {

        this.categoryRepository = categoryRepository;
        this.deskRecordRepository = deskRecordRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        Category general = categoryRepository.findByName("General")
                .orElseThrow();

        List<DeskRecord> records =
                deskRecordRepository.findByCategoryIsNull();

        for (DeskRecord record : records) {
            record.setCategory(general);
        }

        deskRecordRepository.saveAll(records);

        System.out.println("Existing records assigned to General");
    }
}