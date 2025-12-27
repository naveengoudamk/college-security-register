package com.college.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDTO {
    private Long id;
    private String name;
    private String phone;
    private String idType;
    private String idNumber;
    private String purpose;
    private String studentToMeet;
    private String studentRollNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Boolean blacklisted;
    private String guardName;
    private String remarks;
}
