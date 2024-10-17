package com.Library.Mangement.auth;

import com.Library.Mangement.config.JwtService;
import com.Library.Mangement.user.Role;
import com.Library.Mangement.Admin.AdminService;
import com.Library.Mangement.Email.EmailService;
import com.Library.Mangement.user.User;
import com.Library.Mangement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;
    private final EmailService emailService;

    public String registerUser(RegisterRequest request) {
        // Check if the email already exists
        User existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)  // Assign role (USER, AUTHOR)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();
        userRepository.save(user);

        return "Registration Successful!";
    }

    public String registerAuthor(RegisterRequest request) {
        // Check if the email already exists
        User existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PENDING_AUTHOR)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();
        userRepository.save(user);

        // Assign a random admin to approve the accountStatus
        Optional<User> assignedAdmin = adminService.assignApprovalToAdmin();
        if (assignedAdmin.isPresent()) {
            User admin = assignedAdmin.get();
            // Send email notification to the assigned admin
            String emailMessage = String.format("Dear %s,\n\nA new author account request has been created. Please review and approve or reject the request.\n\nAuthor Details:\nName: %s\nEmail: %s",
                    admin.getFirstname(), user.getFirstname() + " " + user.getLastname(), user.getEmail());

            emailService.sendEmail(admin.getEmail(), "New Author Account Request", emailMessage);
        } else {
            // No admin available to handle the request
            return "Registration successful, but no admin available to approve your request at this time.";
        }

        return "Author registration successful. Your request is pending admin approval.";
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Perform authentication using the provided email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();  // Get authenticated user details

        // Check if the user is a pending author and prevent them from logging in
        if (user.getRole().equals(Role.PENDING_AUTHOR)) {
            // Throw an exception or return an error response
            throw new IllegalStateException("Your account is pending approval. Please wait for admin approval.");
        }

        // Prepare extra claims (e.g., the role)
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());  // Include the role as part of the JWT claims

        // Generate JWT token with extra claims
        String jwtToken = jwtService.generateToken(extraClaims, user);

        // Return authentication response with token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

}
