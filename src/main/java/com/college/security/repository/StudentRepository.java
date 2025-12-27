package com.college.security.repository;

import com.college.security.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNumber(String rollNumber);

    List<Student> findByHostelId(Long hostelId);

    List<Student> findByHostelIdAndActive(Long hostelId, Boolean active);

    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(String name);

    List<Student> findByDepartmentAndYear(String department, Integer year);

    Boolean existsByRollNumber(String rollNumber);

    Boolean existsByEmail(String email);
}
