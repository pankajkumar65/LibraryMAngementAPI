package com.Library.Mangement.AdvanceBooking;

import com.Library.Mangement.book.Book;
import com.Library.Mangement.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdvanceBookingRepository extends JpaRepository<Advancebooking, Long> {
    List<Advancebooking> findByBookId(Long bookId);
    List<Advancebooking> findByBookIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long bookId, LocalDate startDate, LocalDate endDate);
    boolean existsByUserAndBook(User user, Book book);
    List<Advancebooking> findAllByBook(Book book);
}
