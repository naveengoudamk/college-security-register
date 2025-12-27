package com.college.security.controller;

import com.college.security.model.SecurityGuard;
import com.college.security.model.Warden;
import com.college.security.repository.SecurityGuardRepository;
import com.college.security.repository.WardenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final SecurityGuardRepository guardRepository;
    private final WardenRepository wardenRepository;

    @PostMapping("/login/guard")
    public ResponseEntity<?> guardLogin(@RequestBody LoginRequest request) {
        return guardRepository.findByGuardId(request.username())
                .filter(guard -> guard.getPassword().equals(request.password()))
                .filter(SecurityGuard::getActive)
                .map(guard -> {
                    guard.setShiftStartTime(LocalDateTime.now());
                    guardRepository.save(guard);

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("userType", "GUARD");
                    response.put("id", guard.getId());
                    response.put("name", guard.getName());
                    response.put("shift", guard.getShift());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid credentials")));
    }

    @PostMapping("/login/warden")
    public ResponseEntity<?> wardenLogin(@RequestBody LoginRequest request) {
        return wardenRepository.findByWardenId(request.username())
                .filter(warden -> warden.getPassword().equals(request.password()))
                .filter(Warden::getActive)
                .map(warden -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("userType", warden.getRole().toString());
                    response.put("id", warden.getId());
                    response.put("name", warden.getName());
                    response.put("email", warden.getEmail());
                    if (warden.getHostelAssigned() != null) {
                        response.put("hostel", warden.getHostelAssigned().getName());
                    }
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid credentials")));
    }

    @PostMapping("/logout/guard/{guardId}")
    public ResponseEntity<?> guardLogout(@PathVariable Long guardId) {
        return guardRepository.findById(guardId)
                .map(guard -> {
                    guard.setShiftEndTime(LocalDateTime.now());
                    guardRepository.save(guard);
                    return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("success", false, "message", "Guard not found")));
    }

    record LoginRequest(String username, String password) {
    }
}
