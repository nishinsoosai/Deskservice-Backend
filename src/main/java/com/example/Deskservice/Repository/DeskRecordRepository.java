package com.example.Deskservice.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Deskservice.Entity.*;

public interface DeskRecordRepository
        extends JpaRepository<DeskRecord, Long> {

    @EntityGraph(attributePaths = "category")
    Page<DeskRecord> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<DeskRecord> findByStatus(Status status, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<DeskRecord> findByCategory_Id(Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<DeskRecord> findByStatusAndCategory_Id(
            Status status,
            Long categoryId,
            Pageable pageable);

    List<DeskRecord> findByCategoryIsNull();
}