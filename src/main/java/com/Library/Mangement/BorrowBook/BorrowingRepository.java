package com.Library.Mangement.BorrowBook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    boolean existsByBookIdAndBorrowedDateBetween(Long bookId, LocalDate startDate, LocalDate endDate);
    List<Borrowing> findAllByUserId(Long userId);
    boolean existsByBookId(Long bookId);


}

