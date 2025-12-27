package com.college.security.repository;

import com.college.security.model.SecurityGuard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityGuardRepository extends JpaRepository<SecurityGuard, Long> {

    Optional<SecurityGuard> findByGuardId(String guardId);

    List<SecurityGuard> findByShift(SecurityGuard.Shift shift);

    List<SecurityGuard> findByActive(Boolean active);

    Boolean existsByGuardId(String guardId);
}
