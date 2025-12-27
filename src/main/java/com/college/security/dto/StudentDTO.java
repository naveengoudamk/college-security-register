package com.college.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String rollNumber;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Integer year;
    private String hostelName;
    private String roomNumber;
    private Boolean active;
}
