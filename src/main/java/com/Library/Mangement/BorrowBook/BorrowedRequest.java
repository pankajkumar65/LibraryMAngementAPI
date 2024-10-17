package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.book.Book;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedRequest {

    private Long id; // Borrowing ID

    @NotNull(message = "Book details cannot be null")
    private Book book; // Book details

    @NotNull(message = "Borrowed date cannot be null")
    @FutureOrPresent(message = "Borrowed date cannot be in the past")
    private LocalDate borrowedDate; // Borrowed date

    @FutureOrPresent(message = "Return date cannot be in the past")
    private LocalDate returnDate; // Return date (optional)
}
