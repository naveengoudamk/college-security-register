package com.college.security.service;

import com.college.security.dto.EntryLogDTO;
import com.college.security.model.EntryLog;
import com.college.security.model.SecurityGuard;
import com.college.security.model.Student;
import com.college.security.repository.EntryLogRepository;
import com.college.security.repository.SecurityGuardRepository;
import com.college.security.repository.StudentRepository;
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
public class EntryLogService {

    private final EntryLogRepository entryLogRepository;
    private final StudentRepository studentRepository;
    private final SecurityGuardRepository securityGuardRepository;
    private final NotificationService notificationService;

    @Transactional
    public EntryLogDTO recordEntry(String rollNumber, Long guardId) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        entryLogRepository.findActiveEntryByStudentId(student.getId())
                .ifPresent(entry -> {
                    throw new RuntimeException("Student already has an active entry");
                });

        SecurityGuard guard = securityGuardRepository.findById(guardId)
                .orElseThrow(() -> new RuntimeException("Guard not found"));

        EntryLog entryLog = new EntryLog();
        entryLog.setStudent(student);
        entryLog.setGuard(guard);
        entryLog.setEntryTime(LocalDateTime.now());

        LocalTime lateEntryTime = student.getHostel().getLateEntryTime();
        boolean isLate = LocalTime.now().isAfter(lateEntryTime);
        entryLog.setLateEntry(isLate);
        entryLog.setCreatedAt(LocalDateTime.now());

        EntryLog saved = entryLogRepository.save(entryLog);

        if (isLate) {
            notificationService.sendLateEntryAlert(student, saved);
        }

        return convertToDTO(saved);
    }

    @Transactional
    public EntryLogDTO recordExit(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        EntryLog entryLog = entryLogRepository.findActiveEntryByStudentId(student.getId())
                .orElseThrow(() -> new RuntimeException("No active entry found for student"));

        entryLog.setExitTime(LocalDateTime.now());
        EntryLog saved = entryLogRepository.save(entryLog);

        return convertToDTO(saved);
    }

    public List<EntryLogDTO> getTodayEntries() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        return entryLogRepository.findByEntryTimeBetween(startOfDay, endOfDay).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryLogDTO> getLateEntries(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return entryLogRepository.findLateEntriesBetween(startOfDay, endOfDay).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryLogDTO> getStudentHistory(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return entryLogRepository.findByStudentId(student.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryLogDTO> getActiveEntries() {
        return entryLogRepository.findAllActiveEntries().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryLogDTO> getEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return entryLogRepository.findByEntryTimeBetween(start, end).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EntryLogDTO convertToDTO(EntryLog log) {
        EntryLogDTO dto = new EntryLogDTO();
        dto.setId(log.getId());
        dto.setStudentRollNumber(log.getStudent().getRollNumber());
        dto.setStudentName(log.getStudent().getName());
        dto.setHostelName(log.getStudent().getHostel().getName());
        dto.setRoomNumber(log.getStudent().getRoomNumber());
        dto.setEntryTime(log.getEntryTime());
        dto.setExitTime(log.getExitTime());
        dto.setLateEntry(log.getLateEntry());
        dto.setGuardName(log.getGuard() != null ? log.getGuard().getName() : null);
        dto.setRemarks(log.getRemarks());
        return dto;
    }
}
