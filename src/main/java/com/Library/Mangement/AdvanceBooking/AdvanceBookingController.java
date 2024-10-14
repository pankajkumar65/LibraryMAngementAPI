package com.Library.Mangement.AdvanceBooking;

import com.Library.Mangement.GlobalExceptioon.BookAlreadyBorrowedException;
import com.Library.Mangement.GlobalExceptioon.ResourceNotFoundException;
import com.Library.Mangement.GlobalExceptioon.UserAlreadyBookedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdvanceBookingController {

    private final AdvanceBookingService advanceBookingService;

    @PostMapping("/advance-booking")
    public ResponseEntity<String> makeAdvanceBooking(@RequestBody AdvanceBookingRequest advanceBookingRequest) {
        try {
            String successMessage = advanceBookingService.makeAdvanceBooking(advanceBookingRequest);
            return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BookAlreadyBorrowedException | UserAlreadyBookedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
