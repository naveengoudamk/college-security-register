package com.college.security.repository;

import com.college.security.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    @Query("SELECT v FROM Visitor v WHERE v.exitTime IS NULL")
    List<Visitor> findActiveVisitors();

    List<Visitor> findByStudentToMeetId(Long studentId);

    @Query("SELECT v FROM Visitor v WHERE v.entryTime BETWEEN :startDate AND :endDate")
    List<Visitor> findByEntryTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Visitor> findByBlacklisted(Boolean blacklisted);

    @Query("SELECT v FROM Visitor v WHERE v.phone = :phone ORDER BY v.entryTime DESC")
    List<Visitor> findByPhoneOrderByEntryTimeDesc(String phone);

    @Query("SELECT v FROM Visitor v WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Visitor> searchByName(String name);
}
