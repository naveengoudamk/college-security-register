package com.college.security.repository;

import com.college.security.model.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long> {

    Optional<Hostel> findByName(String name);

    List<Hostel> findByType(Hostel.HostelType type);

    List<Hostel> findByActive(Boolean active);
}
