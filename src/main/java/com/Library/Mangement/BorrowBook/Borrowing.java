package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.book.Book;
import com.Library.Mangement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Borrowing")
public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Book cannot be null")
    @ManyToOne
    @JoinColumn(name = "bbook_id", nullable = false)
    private Book book;

    @NotNull(message = "Borrowed date cannot be null")
    @FutureOrPresent(message = "Borrowed date cannot be in the past")
    @Column(nullable = false)
    private LocalDate borrowedDate;

    @FutureOrPresent(message = "Return date cannot be in the past")
    @Column
    private LocalDate returnDate; // Optional if you want to track when the book is returned

    @FutureOrPresent(message = "Release date cannot be in the past")
    @Column
    private LocalDate releaseDate; // Date when the book is released (returned)
}
