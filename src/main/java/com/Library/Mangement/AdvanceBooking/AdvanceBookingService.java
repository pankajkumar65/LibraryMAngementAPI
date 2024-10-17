package com.Library.Mangement.AdvanceBooking;

import com.Library.Mangement.BorrowBook.Borrowing;
import com.Library.Mangement.BorrowBook.BorrowingRepository;
import com.Library.Mangement.GlobalExceptioon.ResourceNotFoundException;
import com.Library.Mangement.book.Book;
import com.Library.Mangement.book.BookRepository;
import com.Library.Mangement.Email.EmailService;
import com.Library.Mangement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvanceBookingService {

    private final AdvanceBookingRepository advanceBookingRepository;
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final EmailService emailService;

    @Transactional
    public String makeAdvanceBooking(Long bookId) {
        // Retrieve the book using the provided book ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Check if the user already has an advance booking for the same book
        boolean hasExistingBooking = advanceBookingRepository.existsByUserAndBook(user, book);
        if (hasExistingBooking) {
            return "You already have an advance booking for this book.";
        }

        // Fetch the current borrowing record for the book, if any
        Optional<Borrowing> currentBorrowing = borrowingRepository.findFirstByBookOrderByReturnDateDesc(book);

        LocalDate startDate;
        LocalDate endDate;

        if (currentBorrowing.isPresent()) {
            // If the book is currently borrowed, set the advance booking from the return date
            startDate = currentBorrowing.get().getReturnDate();
            endDate = startDate.plusDays(15);

            // Create and save the advance booking for future borrowing
            Advancebooking advanceBooking = Advancebooking.builder()
                    .book(book)
                    .user(user)
                    .startDate(startDate)
                    .endDate(endDate)
                    .isNotified(false)
                    .build();
            advanceBookingRepository.save(advanceBooking);

            return "The book is currently borrowed. Your advance booking has been created for the period "
                    + startDate + " to " + endDate + ".";
        } else {
            // If the book is not currently borrowed, automatically borrow it for the user
            borrowBookForUser(user, book);
            return "The book was available and has been successfully borrowed for 15 days.";
        }
    }

    private void borrowBookForUser(User user, Book book) {
        Borrowing newBorrowing = Borrowing.builder()
                .user(user)
                .book(book)
                .borrowedDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(15)) // Assuming 15 days borrowing
                .build();
        borrowingRepository.save(newBorrowing);
    }
}
