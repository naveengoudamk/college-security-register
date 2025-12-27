package com.college.security.service;

import com.college.security.model.EntryLog;
import com.college.security.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendLateEntryAlert(Student student, EntryLog entryLog) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("warden@college.edu");
            message.setSubject("Late Entry Alert - " + student.getName());
            message.setText(String.format(
                    "Student %s (Roll No: %s) from %s, Room %s entered the hostel late at %s.\n\n" +
                            "Hostel: %s\n" +
                            "Entry Time: %s\n\n" +
                            "This is an automated alert from the College Security System.",
                    student.getName(),
                    student.getRollNumber(),
                    student.getHostel().getName(),
                    student.getRoomNumber(),
                    entryLog.getEntryTime(),
                    student.getHostel().getName(),
                    entryLog.getEntryTime()));

            mailSender.send(message);
            log.info("Late entry alert sent for student: {}", student.getRollNumber());
        } catch (Exception e) {
            log.error("Failed to send late entry alert: {}", e.getMessage());
        }
    }

    public void sendVisitorNotification(Student student, String visitorName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(student.getEmail());
            message.setSubject("Visitor Notification");
            message.setText(String.format(
                    "Dear %s,\n\n" +
                            "You have a visitor: %s\n" +
                            "Please come to the security cabin.\n\n" +
                            "College Security System",
                    student.getName(),
                    visitorName));

            mailSender.send(message);
            log.info("Visitor notification sent to student: {}", student.getRollNumber());
        } catch (Exception e) {
            log.error("Failed to send visitor notification: {}", e.getMessage());
        }
    }
}
