package com.Meter.MeterReading.Controller;

import com.Meter.MeterReading.Model.User;
import com.Meter.MeterReading.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://mymeterreading.netlify.app"
})public class AuthController {

    private final UserRepository userRepository;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // =========================================================
    // USER LOGIN — by phone number
    // =========================================================
    //
    // POST /api/auth/user/login
    // Body: { "phoneNumber": "9876543210" }
    //
    // Response:
    // {
    //   "success": true,
    //   "role": "user",
    //   "userId": 1,
    //   "name": "John Doe",
    //   "phoneNumber": "9876543210"
    // }
    //
    // =========================================================

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(
            @RequestBody Map<String, String> body) {

        String phoneNumber = body.get("phoneNumber");

        if (phoneNumber == null || phoneNumber.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Phone number is required"
                    ));
        }

        Optional<User> optionalUser =
                userRepository.findByPhoneNumber(phoneNumber.trim());

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "success", false,
                            "message", "No account found with this phone number. Please contact the administrator."
                    ));
        }

        User user = optionalUser.get();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "role", "user",
                "userId", user.getUserId(),
                "name", user.getName() != null ? user.getName() : "",
                "phoneNumber", user.getPhoneNumber(),
                "email", user.getEmail() != null ? user.getEmail() : "",
                "address", user.getAddress() != null ? user.getAddress() : ""
        ));
    }


    // =========================================================
    // ADMIN LOGIN — by username + password
    // =========================================================
    //
    // POST /api/auth/admin/login
    // Body: { "username": "admin", "password": "admin123" }
    //
    // Response:
    // {
    //   "success": true,
    //   "role": "admin",
    //   "username": "admin"
    // }
    //
    // =========================================================

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(
            @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Username and password are required"
                    ));
        }

        boolean validCredentials =
                adminUsername.equals(username.trim()) &&
                adminPassword.equals(password.trim());

        if (!validCredentials) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid username or password"
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "role", "admin",
                "username", username.trim()
        ));
    }


    // =========================================================
    // LOGOUT — clears nothing server-side (stateless)
    // =========================================================

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }
}
