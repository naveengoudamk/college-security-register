package com.college.security.controller;

import com.college.security.dto.EntryLogDTO;
import com.college.security.service.EntryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/entry")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EntryLogController {

    private final EntryLogService entryLogService;

    @PostMapping
    public ResponseEntity<?> recordEntry(
            @RequestParam String rollNumber,
            @RequestParam Long guardId) {
        try {
            EntryLogDTO dto = entryLogService.recordEntry(rollNumber, guardId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<?> recordExit(@RequestParam String rollNumber) {
        try {
            EntryLogDTO dto = entryLogService.recordExit(rollNumber);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/today")
    public ResponseEntity<List<EntryLogDTO>> getTodayEntries() {
        List<EntryLogDTO> entries = entryLogService.getTodayEntries();
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EntryLogDTO>> getActiveEntries() {
        List<EntryLogDTO> entries = entryLogService.getActiveEntries();
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/late")
    public ResponseEntity<List<EntryLogDTO>> getLateEntries(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate queryDate = date != null ? date : LocalDate.now();
        List<EntryLogDTO> entries = entryLogService.getLateEntries(queryDate);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/student/{rollNumber}")
    public ResponseEntity<?> getStudentHistory(@PathVariable String rollNumber) {
        try {
            List<EntryLogDTO> history = entryLogService.getStudentHistory(rollNumber);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/range")
    public ResponseEntity<List<EntryLogDTO>> getEntriesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<EntryLogDTO> entries = entryLogService.getEntriesByDateRange(startDate, endDate);
        return ResponseEntity.ok(entries);
    }
}
