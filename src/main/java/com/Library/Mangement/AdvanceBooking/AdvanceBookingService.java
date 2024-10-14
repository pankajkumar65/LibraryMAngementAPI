package com.Library.Mangement.AdvanceBooking;

import com.Library.Mangement.BorrowBook.BorrowingRepository;
import com.Library.Mangement.GlobalExceptioon.BookAlreadyBorrowedException;
import com.Library.Mangement.GlobalExceptioon.ResourceNotFoundException;
import com.Library.Mangement.GlobalExceptioon.UserAlreadyBookedException;
import com.Library.Mangement.book.Book;
import com.Library.Mangement.book.BookRepository;
import com.Library.Mangement.Email.EmailService;
import com.Library.Mangement.user.User;
import com.Library.Mangement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvanceBookingService {

    private final AdvanceBookingRepository advanceBookingRepository;
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final EmailService emailService;

    @Transactional
    public String makeAdvanceBooking(AdvanceBookingRequest advanceBookingRequest) {
        Book book = bookRepository.findById(advanceBookingRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + advanceBookingRequest.getBookId()));

        // Check if the book is already borrowed in the requested date range
        boolean isBookBorrowed = borrowingRepository.existsByBookIdAndBorrowedDateBetween(
                advanceBookingRequest.getBookId(),
                advanceBookingRequest.getStartDate(),
                advanceBookingRequest.getEndDate());

        if (isBookBorrowed) {
            throw new BookAlreadyBorrowedException("The book is already borrowed during this date range.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Check if the user already has an advance booking for the same book
        boolean userAlreadyBooked = advanceBookingRepository.existsByUserAndBook(user, book);
        if (userAlreadyBooked) {
            throw new UserAlreadyBookedException("You have already made an advance booking for this book.");
        }

        // Create the booking and mark it as unnotified
        Advancebooking advanceBooking = Advancebooking.builder()
                .book(book)
                .user(user)
                .startDate(advanceBookingRequest.getStartDate())
                .endDate(advanceBookingRequest.getEndDate())
                .isNotified(false)
                .build();

        advanceBooking = advanceBookingRepository.save(advanceBooking);

        // Optionally notify users if the book is currently available
        if (!borrowingRepository.existsByBookId(advanceBookingRequest.getBookId())) {
            notifyUsersForAvailableBook(book);
        }

        return "Your booking has been done successfully.";
    }

    private void notifyUsersForAvailableBook(Book book) {
        List<Advancebooking> advanceBookings = advanceBookingRepository.findAllByBook(book);

        for (Advancebooking booking : advanceBookings) {
            User user = booking.getUser();
            if (!booking.getIsNotified()) {
                String emailMessage = String.format("Dear %s, the book '%s' is now available for borrowing.",
                        user.getFirstname() + " " + user.getLastname(), book.getBookName());

                emailService.sendEmail(user.getEmail(), "Book Available for Borrowing", emailMessage);

                // Mark user as notified
                booking.setIsNotified(true);
                advanceBookingRepository.save(booking);
            }
        }
    }
}
