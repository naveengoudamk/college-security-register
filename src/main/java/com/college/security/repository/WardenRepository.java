package com.college.security.repository;

import com.college.security.model.Warden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardenRepository extends JpaRepository<Warden, Long> {

    Optional<Warden> findByWardenId(String wardenId);

    Optional<Warden> findByEmail(String email);

    List<Warden> findByRole(Warden.Role role);

    List<Warden> findByHostelAssignedId(Long hostelId);

    List<Warden> findByActive(Boolean active);

    Boolean existsByWardenId(String wardenId);

    Boolean existsByEmail(String email);
}
