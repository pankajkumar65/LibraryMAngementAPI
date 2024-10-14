package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.book.Book;
import com.Library.Mangement.user.User;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bbook_id", nullable = false)
    private Book book;

    @Column
    private LocalDate borrowedDate;

    @Column
    private LocalDate returnDate; // Optional if you want to track when the book is returned
}
