package com.Library.Mangement.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/book/add")
    public ResponseEntity<String> addBook(@RequestBody BookRequest bookRequest) {
        try {
            Book createdBook = bookService.addBook(bookRequest);
            String successMessage = "A new book titled '" + createdBook.getBookName() +
                    "' added successfully by " + createdBook.getAuthorName() + ".";
            return new ResponseEntity<>(successMessage, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to add the book due to a server error.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/book/list")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // API endpoint to fetch all books by the currently authenticated author
    @GetMapping("/author/books")
    public ResponseEntity<?> getBooksByAuthenticatedAuthor() {
        try {
            // Fetch books by the authenticated author
            List<Book> books = bookService.getBooksByAuthenticatedAuthor();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        String deletemsg = "book deleted successfully!";
        return new ResponseEntity<>(deletemsg,HttpStatus.OK);
    }
}
