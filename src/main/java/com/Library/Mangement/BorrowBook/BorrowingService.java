package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.AdvanceBooking.AdvanceBookingRepository;
import com.Library.Mangement.AdvanceBooking.Advancebooking;
import com.Library.Mangement.Email.EmailService;
import com.Library.Mangement.book.Book;
import com.Library.Mangement.book.BookRepository;
import com.Library.Mangement.user.User;
import com.Library.Mangement.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AdvanceBookingRepository advanceBookingRepository;
    private final EmailService emailService;

    public String borrowBook(Long bookId) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Check if the user is allowed to borrow more books
        long currentBorrowedBooksCount = borrowingRepository.findAllByUserId(user.getId()).size();
        if (currentBorrowedBooksCount >= 20) {
            throw new IllegalArgumentException("User cannot borrow more than 20 books at a time.");
        }

        // Find the book to borrow
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        boolean alreadyBorrowed = borrowingRepository.findAllByUserId(user.getId())
                .stream()
                .anyMatch(borrowing -> borrowing.getBook().getId().equals(book.getId()));

        if (alreadyBorrowed) {
            throw new IllegalArgumentException("This book is already borrowed!.");
        }

        LocalDate borrowedDate = LocalDate.now();
        LocalDate returnDate = borrowedDate.plusDays(15);

        // Create a new borrowing record
        Borrowing borrowing = Borrowing.builder()
                .user(user)
                .book(book)
                .returnDate(returnDate)
                .borrowedDate(LocalDate.now())
                .build();

        borrowingRepository.save(borrowing);

        // Return a success message
        return String.format("The book '%s' has been borrowed by '%s' successfully.", book.getBookName(), user.getFirstname() + " " + user.getLastname());
    }

    @Transactional
    public String releaseBook(Long bookId) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Find the borrowing record by book ID and user ID
        Borrowing borrowing = borrowingRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("No borrowing record found for this book and user."));

        // Set release date as today
        borrowing.setReleaseDate(LocalDate.now());
        borrowingRepository.save(borrowing);

        // Check for advance bookings for the same book
        List<Advancebooking> advanceBookings = advanceBookingRepository.findAllByBook(borrowing.getBook());
        if (!advanceBookings.isEmpty()) {
            Advancebooking topBooking = advanceBookings.get(0); // Get the top booking
            User topUser = topBooking.getUser();

            // Notify the user via email
            String emailMessage = String.format("Dear %s, the book '%s' is now available for borrowing.",
                    topUser.getFirstname(), borrowing.getBook().getBookName());
            emailService.sendEmail(topUser.getEmail(), "Book Available for Borrowing", emailMessage);

            // Mark the user as notified
            topBooking.setIsNotified(true);
            advanceBookingRepository.save(topBooking);
        }

        return "Book released successfully.";
    }

    public List<BorrowedRequest> getBorrowedBooks(Long userId) {
        List<Borrowing> borrowings = borrowingRepository.findAllByUserId(userId);

        // Map Borrowing entities to BorrowedRequest DTOs
        return borrowings.stream()
                .map(borrowing -> new BorrowedRequest(
                        borrowing.getId(),
                        borrowing.getBook(),
                        borrowing.getBorrowedDate(),
                        borrowing.getReturnDate()))
                .collect(Collectors.toList());
    }
}
