package com.Library.Mangement.BorrowBook;

import com.Library.Mangement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId) {
        try {
            String successMessage = borrowingService.borrowBook(bookId);
            return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/borrowed-books")
    public ResponseEntity<List<BorrowedRequest>> getBorrowedBooks() {
        // Get the authenticated user's ID
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<BorrowedRequest> borrowedBooks = borrowingService.getBorrowedBooks(userId);
        return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
    }

    @PostMapping("/release/{bookId}")
    public ResponseEntity<String> releaseBook(@PathVariable Long bookId) {
        try {
            String response = borrowingService.releaseBook(bookId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
