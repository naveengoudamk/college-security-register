package com.college.security.controller;

import com.college.security.dto.VisitorDTO;
import com.college.security.model.Visitor;
import com.college.security.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping
    public ResponseEntity<?> registerVisitor(
            @RequestBody Visitor visitor,
            @RequestParam String studentRollNumber,
            @RequestParam Long guardId) {
        try {
            VisitorDTO dto = visitorService.registerVisitor(visitor, studentRollNumber, guardId);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/exit")
    public ResponseEntity<?> recordExit(@PathVariable Long id) {
        try {
            VisitorDTO dto = visitorService.recordVisitorExit(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<VisitorDTO>> getActiveVisitors() {
        List<VisitorDTO> visitors = visitorService.getActiveVisitors();
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/today")
    public ResponseEntity<List<VisitorDTO>> getTodayVisitors() {
        List<VisitorDTO> visitors = visitorService.getTodayVisitors();
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/range")
    public ResponseEntity<List<VisitorDTO>> getVisitorsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<VisitorDTO> visitors = visitorService.getVisitorsByDateRange(startDate, endDate);
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/history")
    public ResponseEntity<List<VisitorDTO>> getVisitorHistory(@RequestParam String phone) {
        List<VisitorDTO> history = visitorService.getVisitorHistory(phone);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{id}/blacklist")
    public ResponseEntity<?> blacklistVisitor(@PathVariable Long id, @RequestParam String reason) {
        try {
            VisitorDTO dto = visitorService.blacklistVisitor(id, reason);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/blacklisted")
    public ResponseEntity<List<VisitorDTO>> getBlacklistedVisitors() {
        List<VisitorDTO> visitors = visitorService.getBlacklistedVisitors();
        return ResponseEntity.ok(visitors);
    }
}
