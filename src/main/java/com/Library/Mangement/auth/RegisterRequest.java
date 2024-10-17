package com.Library.Mangement.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters.")
    private String firstname;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters.")
    private String lastname;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    private String password;
}
