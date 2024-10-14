package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedRequest {
    private Long id; // Borrowing ID
    private Book book; // Book details
    private LocalDate borrowedDate; // Borrowed date
    private LocalDate returnDate; // Return date
}
