package com.college.security.controller;

import com.college.security.dto.StudentDTO;
import com.college.security.model.Student;
import com.college.security.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> registerStudent(@RequestBody Student student, @RequestParam Long hostelId) {
        try {
            StudentDTO dto = studentService.registerStudent(student, hostelId);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{rollNumber}")
    public ResponseEntity<?> getStudent(@PathVariable String rollNumber) {
        try {
            StudentDTO dto = studentService.getStudentByRollNumber(rollNumber);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{rollNumber}/qr")
    public ResponseEntity<?> getQRCode(@PathVariable String rollNumber) {
        try {
            String qrCode = studentService.generateQRCodeForStudent(rollNumber);
            return ResponseEntity.ok().body(new QRCodeResponse(qrCode));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/hostel/{hostelId}")
    public ResponseEntity<List<StudentDTO>> getStudentsByHostel(@PathVariable Long hostelId) {
        List<StudentDTO> students = studentService.getStudentsByHostel(hostelId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(@RequestParam String name) {
        List<StudentDTO> students = studentService.searchStudents(name);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            StudentDTO dto = studentService.updateStudent(id, student);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    record QRCodeResponse(String qrCodeBase64) {
    }
}
