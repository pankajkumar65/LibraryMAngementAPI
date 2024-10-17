package com.Library.Mangement.AdvanceBooking;

import com.Library.Mangement.book.Book;
import com.Library.Mangement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "advance_bookingg")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Advancebooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
//    @NotBlank(message = "Book is required.")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
//    @NotBlank(message = "User is required.")
    private User user;

    @Column(nullable = false)
//    @NotBlank(message = "Start date is required.")
//    @FutureOrPresent(message = "Start date must be today or a future date.")
    private LocalDate startDate;

    @Column(nullable = false)
//    @NotBlank(message = "End date is required.")
//    @FutureOrPresent(message = "End date must be today or a future date.")
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isNotified = false;  // to track if the user has been notified
}
