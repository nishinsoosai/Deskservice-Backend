package com.example.Deskservice.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Deskservice.Entity.Status;
import com.example.Deskservice.dto.*;
import com.example.Deskservice.service.RecordService;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService service;

    public RecordController(RecordService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RecordResponse> create(
            @Valid @RequestBody WriteRecord request) {

        RecordResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(response);
    }

    // GET ALL + FILTER + PAGINATION
    @GetMapping
    public ResponseEntity<PageResponse<RecordResponse>> getAll(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                service.getAll(status, categoryId, page, size)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // GET REVIEW ROUTE FROM POLICYSERVICE
    @GetMapping("/{id}/review-route")
    public ResponseEntity<PolicyResponse> getReviewRoute(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getReviewRoute(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RecordResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody WriteRecord request) {

        return ResponseEntity.ok(
                service.update(id, request));
    }

    // CLOSE
    @PatchMapping("/{id}")
    public ResponseEntity<RecordResponse> close(
            @PathVariable Long id,
            @Valid @RequestBody CloseRecordRequest request) {

        return ResponseEntity.ok(
                service.close(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}