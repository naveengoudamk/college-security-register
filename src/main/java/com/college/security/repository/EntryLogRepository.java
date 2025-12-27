package com.college.security.repository;

import com.college.security.model.EntryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {

    List<EntryLog> findByStudentId(Long studentId);

    @Query("SELECT e FROM EntryLog e WHERE e.student.id = :studentId AND e.exitTime IS NULL")
    Optional<EntryLog> findActiveEntryByStudentId(Long studentId);

    @Query("SELECT e FROM EntryLog e WHERE e.entryTime BETWEEN :startDate AND :endDate")
    List<EntryLog> findByEntryTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM EntryLog e WHERE e.lateEntry = true AND e.entryTime BETWEEN :startDate AND :endDate")
    List<EntryLog> findLateEntriesBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM EntryLog e WHERE e.student.hostel.id = :hostelId AND e.entryTime BETWEEN :startDate AND :endDate")
    List<EntryLog> findByHostelAndDateRange(Long hostelId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM EntryLog e WHERE e.exitTime IS NULL")
    List<EntryLog> findAllActiveEntries();

    @Query("SELECT COUNT(e) FROM EntryLog e WHERE e.student.hostel.id = :hostelId AND e.exitTime IS NULL")
    Long countActiveEntriesByHostel(Long hostelId);
}
