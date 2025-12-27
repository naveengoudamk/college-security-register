package com.college.security.config;

import com.college.security.model.Hostel;
import com.college.security.model.SecurityGuard;
import com.college.security.model.Student;
import com.college.security.model.Warden;
import com.college.security.repository.HostelRepository;
import com.college.security.repository.SecurityGuardRepository;
import com.college.security.repository.StudentRepository;
import com.college.security.repository.WardenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final HostelRepository hostelRepository;
    private final SecurityGuardRepository guardRepository;
    private final WardenRepository wardenRepository;
    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        if (hostelRepository.count() == 0) {
            loadDemoData();
        }
    }

    private void loadDemoData() {
        Hostel boysHostel = new Hostel();
        boysHostel.setName("Boys Hostel A");
        boysHostel.setType(Hostel.HostelType.BOYS);
        boysHostel.setTotalRooms(100);
        boysHostel.setCapacity(200);
        boysHostel.setLateEntryTime(LocalTime.of(22, 0));
        boysHostel.setActive(true);
        hostelRepository.save(boysHostel);

        Hostel girlsHostel = new Hostel();
        girlsHostel.setName("Girls Hostel A");
        girlsHostel.setType(Hostel.HostelType.GIRLS);
        girlsHostel.setTotalRooms(80);
        girlsHostel.setCapacity(160);
        girlsHostel.setLateEntryTime(LocalTime.of(21, 30));
        girlsHostel.setActive(true);
        hostelRepository.save(girlsHostel);

        SecurityGuard guard1 = new SecurityGuard();
        guard1.setGuardId("GUARD001");
        guard1.setName("Ramesh Kumar");
        guard1.setPhone("9876543210");
        guard1.setPassword("guard123");
        guard1.setShift(SecurityGuard.Shift.MORNING);
        guard1.setActive(true);
        guard1.setCreatedAt(LocalDateTime.now());
        guardRepository.save(guard1);

        SecurityGuard guard2 = new SecurityGuard();
        guard2.setGuardId("GUARD002");
        guard2.setName("Suresh Patel");
        guard2.setPhone("9876543211");
        guard2.setPassword("guard123");
        guard2.setShift(SecurityGuard.Shift.NIGHT);
        guard2.setActive(true);
        guard2.setCreatedAt(LocalDateTime.now());
        guardRepository.save(guard2);

        Warden admin = new Warden();
        admin.setWardenId("ADMIN001");
        admin.setName("Dr. Priya Sharma");
        admin.setEmail("admin@college.edu");
        admin.setPhone("9876543212");
        admin.setPassword("admin123");
        admin.setRole(Warden.Role.ADMIN);
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        wardenRepository.save(admin);

        Warden warden = new Warden();
        warden.setWardenId("WARDEN001");
        warden.setName("Mr. Rajesh Verma");
        warden.setEmail("warden@college.edu");
        warden.setPhone("9876543213");
        warden.setPassword("warden123");
        warden.setRole(Warden.Role.WARDEN);
        warden.setHostelAssigned(boysHostel);
        warden.setActive(true);
        warden.setCreatedAt(LocalDateTime.now());
        wardenRepository.save(warden);

        Student student1 = new Student();
        student1.setRollNumber("CS2021001");
        student1.setName("Amit Patel");
        student1.setEmail("amit@student.college.edu");
        student1.setPhone("9876543214");
        student1.setDepartment("Computer Science");
        student1.setYear(3);
        student1.setHostel(boysHostel);
        student1.setRoomNumber("101");
        student1.setActive(true);
        student1.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setRollNumber("EC2021002");
        student2.setName("Priya Singh");
        student2.setEmail("priya@student.college.edu");
        student2.setPhone("9876543215");
        student2.setDepartment("Electronics");
        student2.setYear(2);
        student2.setHostel(girlsHostel);
        student2.setRoomNumber("201");
        student2.setActive(true);
        student2.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student2);

        System.out.println("Demo data loaded successfully!");
        System.out.println("Guard Login: GUARD001 / guard123");
        System.out.println("Admin Login: ADMIN001 / admin123");
        System.out.println("Warden Login: WARDEN001 / warden123");
    }
}
