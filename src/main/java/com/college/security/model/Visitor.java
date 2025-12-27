package com.college.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String idType;

    private String idNumber;

    @Column(nullable = false)
    private String purpose;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student studentToMeet;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private String photoPath;

    @Column(nullable = false)
    private Boolean blacklisted = false;

    @ManyToOne
    @JoinColumn(name = "guard_id")
    private SecurityGuard guard;

    private String remarks;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
