package com.Library.Mangement.book;

import com.Library.Mangement.user.Role;
import com.Library.Mangement.user.User;
import com.Library.Mangement.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Book addBook(BookRequest bookRequest) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User author = (User) authentication.getPrincipal();
        String authorFullName = author.getFirstname() + " " + author.getLastname();

        Book book = Book.builder()
                .bookName(bookRequest.getBookName())
                .tags(bookRequest.getTags())
                .category(bookRequest.getCategory())
                .pdfFile(bookRequest.getPdfFile())
                .description(bookRequest.getDescription())
                .author(author)
                .AuthorName(authorFullName)
                .build();

        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("A book with the same name already exists.");
        }
    }
    // Fetch books added by the currently authenticated author
    public List<Book> getBooksByAuthenticatedAuthor() {
        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract the User object (the authenticated user)
        User user = (User) authentication.getPrincipal();

        // Check if the user has the AUTHOR role
        if (user.getRole().equals(Role.AUTHOR)) {
            // Return the list of books added by this author
            return bookRepository.findAllByAuthor(user);
        } else {
            throw new IllegalArgumentException("The user with email " + user.getEmail() + " is not an author.");
        }
    }
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
