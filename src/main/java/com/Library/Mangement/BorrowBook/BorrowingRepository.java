package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    boolean existsByBookIdAndBorrowedDateBetween(Long bookId, LocalDate startDate, LocalDate endDate);
    List<Borrowing> findAllByUserId(Long userId);
    boolean existsByBookId(Long bookId);
    Optional<Borrowing> findByBookIdAndUserId(Long bookId, Long userId);
    // Find the latest borrowing record by return date
    Optional<Borrowing> findFirstByBookOrderByReturnDateDesc(Book book);

}

