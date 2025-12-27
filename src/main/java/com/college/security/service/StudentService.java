package com.college.security.service;

import com.college.security.dto.StudentDTO;
import com.college.security.model.Hostel;
import com.college.security.model.Student;
import com.college.security.repository.HostelRepository;
import com.college.security.repository.StudentRepository;
import com.college.security.util.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final HostelRepository hostelRepository;
    private final QRCodeGenerator qrCodeGenerator;

    @Transactional
    public StudentDTO registerStudent(Student student, Long hostelId) throws Exception {
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            throw new RuntimeException("Student with roll number already exists");
        }

        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new RuntimeException("Hostel not found"));

        student.setHostel(hostel);
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());

        String qrData = generateQRData(student);
        student.setQrCodeData(qrData);

        Student savedStudent = studentRepository.save(student);

        return convertToDTO(savedStudent);
    }

    public StudentDTO getStudentByRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return convertToDTO(student);
    }

    public String generateQRCodeForStudent(String rollNumber) throws Exception {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getQrCodeData() == null) {
            String qrData = generateQRData(student);
            student.setQrCodeData(qrData);
            studentRepository.save(student);
        }

        return qrCodeGenerator.generateQRCodeBase64(student.getQrCodeData());
    }

    public List<StudentDTO> getStudentsByHostel(Long hostelId) {
        return studentRepository.findByHostelId(hostelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> searchStudents(String name) {
        return studentRepository.searchByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentDTO updateStudent(Long id, Student updatedStudent) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());
        student.setRoomNumber(updatedStudent.getRoomNumber());
        student.setUpdatedAt(LocalDateTime.now());

        Student saved = studentRepository.save(student);
        return convertToDTO(saved);
    }

    private String generateQRData(Student student) {
        return String.format("STUDENT|%s|%s|%s",
                student.getRollNumber(),
                student.getName(),
                student.getHostel().getName());
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setDepartment(student.getDepartment());
        dto.setYear(student.getYear());
        dto.setHostelName(student.getHostel().getName());
        dto.setRoomNumber(student.getRoomNumber());
        dto.setActive(student.getActive());
        return dto;
    }
}
