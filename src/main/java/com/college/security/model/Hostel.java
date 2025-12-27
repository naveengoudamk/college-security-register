package com.college.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "hostels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HostelType type;

    @Column(nullable = false)
    private Integer totalRooms;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private LocalTime lateEntryTime = LocalTime.of(22, 0);

    @Column(nullable = false)
    private Boolean active = true;

    public enum HostelType {
        BOYS, GIRLS
    }
}
