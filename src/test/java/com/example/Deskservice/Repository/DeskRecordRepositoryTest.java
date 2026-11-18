
package com.example.Deskservice.Repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.Deskservice.Entity.Category;
import com.example.Deskservice.Entity.DeskRecord;
import com.example.Deskservice.Entity.Status;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeskRecordRepositoryTest {

    @Autowired
    private DeskRecordRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndRetrieveRecord() {

        Category category = categoryRepository.save(
                new Category("Test-" + UUID.randomUUID()));

        DeskRecord record = new DeskRecord();

        record.setTitle("Keyboard");
        record.setDescription("Mechanical Keyboard");
        record.setAmount(new BigDecimal("1500.50"));
        record.setStatus(Status.OPEN);
        record.setCategory(category);

        DeskRecord saved = repository.save(record);

        assertNotNull(saved.getId());

        DeskRecord found = repository.findById(saved.getId())
                .orElseThrow();

        assertEquals("Keyboard", found.getTitle());
        assertEquals("Mechanical Keyboard", found.getDescription());
        assertEquals(Status.OPEN, found.getStatus());
        assertEquals(new BigDecimal("1500.50"), found.getAmount());
    }

    @Test
    void shouldFilterByStatusAndApplyPagination() {

        Category category = categoryRepository.save(
                new Category("Test-" + UUID.randomUUID()));

        // Create 3 OPEN records
        for (int i = 1; i <= 3; i++) {

            DeskRecord record = new DeskRecord();

            record.setTitle("Open Record " + i);
            record.setAmount(BigDecimal.valueOf(100));
            record.setStatus(Status.OPEN);
            record.setCategory(category);

            repository.save(record);
        }

        // Create 1 CLOSED record
        DeskRecord closed = new DeskRecord();

        closed.setTitle("Closed Record");
        closed.setAmount(BigDecimal.valueOf(200));
        closed.setStatus(Status.CLOSED);
        closed.setCategory(category);

        repository.save(closed);

        // Page 0, size 2
        PageRequest pageable = PageRequest.of(
                0,
                2,
                Sort.by(Sort.Direction.DESC, "id"));

        Page<DeskRecord> result =
                repository.findByStatus(Status.OPEN, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
    }
}