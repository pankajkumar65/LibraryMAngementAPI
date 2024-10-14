package com.Library.Mangement.Email;
import com.Library.Mangement.AdvanceBooking.AdvanceBookingRepository;
import com.Library.Mangement.book.Book;
import com.Library.Mangement.BorrowBook.BorrowingRepository;
import com.Library.Mangement.AdvanceBooking.Advancebooking;
import com.Library.Mangement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AdvanceBookingRepository advanceBookingRepository;
    private final BorrowingRepository borrowingRepository;
    private final EmailService emailService;

    // This method will be called every day at noon
    @Scheduled(cron = "0 40 15 * * ?") // Every day at noon
    // Method to notify all users who booked the book when it becomes available
    private void notifyUsersForAvailableBook(Book book) {
        List<Advancebooking> advanceBookings = advanceBookingRepository.findAllByBook(book);

        // Loop through each advance booking and send an email notification
        for (Advancebooking booking : advanceBookings) {
            User user = booking.getUser();
            if (!booking.getIsNotified()) {  // Check if user has already been notified
                // Send email notification
                String emailMessage = String.format("Dear %s, the book '%s' is now available for borrowing.",
                        user.getFirstname() + " " + user.getLastname(),
                        book.getBookName());

                emailService.sendEmail(user.getEmail(), "Book Available for Borrowing", emailMessage);

                // Mark as notified
                booking.setIsNotified(true);
                advanceBookingRepository.save(booking);
            }
        }
    }
}


