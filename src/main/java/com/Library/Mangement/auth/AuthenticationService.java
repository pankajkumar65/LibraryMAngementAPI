package com.Library.Mangement.auth;

import com.Library.Mangement.config.JwtService;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())  // Assign role (USER, AUTHOR)
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();
        userRepository.save(user);
        return "Registration Successful!";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Perform authentication using the provided email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();  // Get authenticated user details

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
