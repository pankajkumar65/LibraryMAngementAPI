package com.Library.Mangement.book;
import com.Library.Mangement.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Find all books by a specific author (user)
    List<Book> findAllByAuthor(User author);

}

