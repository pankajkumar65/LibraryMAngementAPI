package com.Library.Mangement.auth;

import com.Library.Mangement.config.LogoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final LogoutService logoutService;

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.registerUser(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(409).body(ex.getMessage());  // 409 Conflict for duplicate email
        }
    }

    @PostMapping("/register/Author")
    public ResponseEntity<String> registerAuthor(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.registerAuthor(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(409).body(ex.getMessage());  // 409 Conflict for duplicate email
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(403).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred during authentication.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Successfully logged out.");
    }
}
