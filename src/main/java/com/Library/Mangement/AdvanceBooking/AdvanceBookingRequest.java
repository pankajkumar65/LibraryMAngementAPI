package com.Library.Mangement.AdvanceBooking;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceBookingRequest {

    @NotBlank(message = "bookId")
    private Long bookId;
}

