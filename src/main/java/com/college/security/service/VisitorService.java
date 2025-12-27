package com.college.security.service;

import com.college.security.dto.VisitorDTO;
import com.college.security.model.SecurityGuard;
import com.college.security.model.Student;
import com.college.security.model.Visitor;
import com.college.security.repository.SecurityGuardRepository;
import com.college.security.repository.StudentRepository;
import com.college.security.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final StudentRepository studentRepository;
    private final SecurityGuardRepository securityGuardRepository;

    @Transactional
    public VisitorDTO registerVisitor(Visitor visitor, String studentRollNumber, Long guardId) {
        Student student = studentRepository.findByRollNumber(studentRollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        SecurityGuard guard = securityGuardRepository.findById(guardId)
                .orElseThrow(() -> new RuntimeException("Guard not found"));

        List<Visitor> previousVisits = visitorRepository.findByPhoneOrderByEntryTimeDesc(visitor.getPhone());
        if (!previousVisits.isEmpty() && previousVisits.get(0).getBlacklisted()) {
            throw new RuntimeException("Visitor is blacklisted");
        }

        visitor.setStudentToMeet(student);
        visitor.setGuard(guard);
        visitor.setEntryTime(LocalDateTime.now());
        visitor.setBlacklisted(false);
        visitor.setCreatedAt(LocalDateTime.now());

        Visitor saved = visitorRepository.save(visitor);
        return convertToDTO(saved);
    }

    @Transactional
    public VisitorDTO recordVisitorExit(Long visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        if (visitor.getExitTime() != null) {
            throw new RuntimeException("Visitor has already exited");
        }

        visitor.setExitTime(LocalDateTime.now());
        Visitor saved = visitorRepository.save(visitor);

        return convertToDTO(saved);
    }

    public List<VisitorDTO> getActiveVisitors() {
        return visitorRepository.findActiveVisitors().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VisitorDTO> getTodayVisitors() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        return visitorRepository.findByEntryTimeBetween(startOfDay, endOfDay).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VisitorDTO> getVisitorsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return visitorRepository.findByEntryTimeBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VisitorDTO> getVisitorHistory(String phone) {
        return visitorRepository.findByPhoneOrderByEntryTimeDesc(phone).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitorDTO blacklistVisitor(Long visitorId, String reason) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        visitor.setBlacklisted(true);
        visitor.setRemarks(reason);
        Visitor saved = visitorRepository.save(visitor);

        return convertToDTO(saved);
    }

    public List<VisitorDTO> getBlacklistedVisitors() {
        return visitorRepository.findByBlacklisted(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VisitorDTO convertToDTO(Visitor visitor) {
        VisitorDTO dto = new VisitorDTO();
        dto.setId(visitor.getId());
        dto.setName(visitor.getName());
        dto.setPhone(visitor.getPhone());
        dto.setIdType(visitor.getIdType());
        dto.setIdNumber(visitor.getIdNumber());
        dto.setPurpose(visitor.getPurpose());

        if (visitor.getStudentToMeet() != null) {
            dto.setStudentToMeet(visitor.getStudentToMeet().getName());
            dto.setStudentRollNumber(visitor.getStudentToMeet().getRollNumber());
        }

        dto.setEntryTime(visitor.getEntryTime());
        dto.setExitTime(visitor.getExitTime());
        dto.setBlacklisted(visitor.getBlacklisted());
        dto.setGuardName(visitor.getGuard() != null ? visitor.getGuard().getName() : null);
        dto.setRemarks(visitor.getRemarks());

        return dto;
    }
}
