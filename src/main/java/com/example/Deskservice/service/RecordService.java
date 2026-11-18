package com.example.Deskservice.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.Deskservice.Entity.Category;
import com.example.Deskservice.Entity.DeskRecord;
import com.example.Deskservice.Entity.Status;

import com.example.Deskservice.Repository.CategoryRepository;
import com.example.Deskservice.Repository.DeskRecordRepository;

import com.example.Deskservice.client.PolicyClient;

import com.example.Deskservice.dto.CloseRecordRequest;
import com.example.Deskservice.dto.RecordResponse;
import com.example.Deskservice.dto.WriteRecord;
import com.example.Deskservice.dto.PageResponse;
import com.example.Deskservice.dto.PolicyResponse;

@Service
@Transactional
public class RecordService {

    private final DeskRecordRepository repository;
    private final CategoryRepository categoryRepository;
    private final PolicyClient policyClient;

    // CONSTRUCTOR INJECTION
    public RecordService(
            DeskRecordRepository repository,
            CategoryRepository categoryRepository,
            PolicyClient policyClient) {

        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.policyClient = policyClient;
    }

    // CREATE RECORD
    public RecordResponse create(WriteRecord request) {

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Category not found with ID: " + request.getCategoryId()));

        DeskRecord record = new DeskRecord();

        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setAmount(request.getAmount());
        record.setCategory(category);
        record.setStatus(Status.OPEN);

        DeskRecord savedRecord = repository.save(record);

        return convertToResponse(savedRecord);
    }

    // GET ALL RECORDS WITH PAGINATION AND FILTERING
    public PageResponse<RecordResponse> getAll(
            Status status,
            Long categoryId,
            int page,
            int size) {

        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 50) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be between 1 and 50");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        Page<DeskRecord> records;

        if (status != null && categoryId != null) {

            records = repository.findByStatusAndCategory_Id(
                    status, categoryId, pageable);

        } else if (status != null) {

            records = repository.findByStatus(status, pageable);

        } else if (categoryId != null) {

            records = repository.findByCategory_Id(categoryId, pageable);

        } else {

            records = repository.findAll(pageable);
        }

        Page<RecordResponse> responsePage =
                records.map(this::convertToResponse);

        return new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages()
        );
    }

    // GET RECORD BY ID
    public RecordResponse getById(Long id) {

        DeskRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Record not found with ID: " + id));

        return convertToResponse(record);
    }

    // GET REVIEW ROUTE FROM POLICYSERVICE
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PolicyResponse getReviewRoute(Long id) {

        // Fetch the record from Deskservice database
        DeskRecord record = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Record not found with ID: " + id));

        // Read the stored amount
        BigDecimal amount = record.getAmount();

        // Call Policyservice after the service transaction is suspended
        return policyClient.getReviewRoute(amount);
    }

    // UPDATE RECORD WITH VERSION CHECK
    public RecordResponse update(Long id, WriteRecord request) {

        DeskRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Record not found with ID: " + id));

        if (request.getVersion() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Version is required for updating a record");
        }

        if (!request.getVersion().equals(record.getVersion())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This record has been modified by someone else. Please reload before editing.");
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Category not found"));

        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setAmount(request.getAmount());
        record.setCategory(category);

        DeskRecord updatedRecord = repository.save(record);

        return convertToResponse(updatedRecord);
    }

    // CLOSE RECORD
    public RecordResponse close(
            Long id,
            CloseRecordRequest request) {

        DeskRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Record not found with ID: " + id));

        record.setStatus(request.getStatus());

        DeskRecord updatedRecord = repository.save(record);

        return convertToResponse(updatedRecord);
    }

    // DELETE RECORD
    public void delete(Long id) {

        DeskRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Record not found with ID: " + id));

        repository.delete(record);
    }

    // CONVERT ENTITY TO RESPONSE DTO
    private RecordResponse convertToResponse(DeskRecord record) {

        RecordResponse response = new RecordResponse();

        response.setId(record.getId());
        response.setTitle(record.getTitle());
        response.setDescription(record.getDescription());
        response.setAmount(record.getAmount());
        response.setStatus(record.getStatus());

        if (record.getCategory() != null) {
            response.setCategoryId(record.getCategory().getId());
            response.setCategoryName(record.getCategory().getName());
        }

        response.setVersion(record.getVersion());

        return response;
    }
}