package vehiclerentalsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vehiclerentalsystem.dto.LoginRequest;
import vehiclerentalsystem.dto.LoginResponse;
import vehiclerentalsystem.services.AuthService;

/**
 * Authentication Controller.
 * 
 * Handles user login and authentication.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login endpoint.
     * 
     * POST /api/auth/login
     * Body: { "email": "...", "password": "..." }
     * 
     * Returns: { "userId": "...", "name": "...", "role": "...", "token": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.email(), request.password());
        return ResponseEntity.ok(response);
    }
}
