package com.example.Deskservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.Deskservice.Repository.CategoryRepository;
import com.example.Deskservice.Repository.DeskRecordRepository;
import com.example.Deskservice.dto.WriteRecord;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private DeskRecordRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RecordService service;

    @Test
    void shouldRejectInvalidCategory() {

        // Arrange
        WriteRecord request = new WriteRecord();
        request.setTitle("Laptop");
        request.setCategoryId(999L);

        when(categoryRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act and Assert
        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> service.create(request)
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                exception.getStatusCode()
        );

        // Verify record is not saved
        verify(repository, never()).save(any());
    }
}