package com.college.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryLogDTO {
    private Long id;
    private String studentRollNumber;
    private String studentName;
    private String hostelName;
    private String roomNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Boolean lateEntry;
    private String guardName;
    private String remarks;
}
