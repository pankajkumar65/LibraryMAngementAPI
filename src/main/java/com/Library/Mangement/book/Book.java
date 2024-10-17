package com.Library.Mangement.book;

import com.Library.Mangement.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BBook")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book name is required")
    @Size(max = 255, message = "Book name cannot exceed 255 characters")
    @Column(unique = true, nullable = false)
    private String bookName;

    @Size(max = 255, message = "Tags cannot exceed 255 characters")
    @Column
    private String tags;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    @Column
    private String category;

    @Lob
    @Column
    private byte[] pdfFile; // Storing the PDF as a byte array, validation for size can be added based on file limits

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column
    private String description;

    @NotNull(message = "Author is required")
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private User author; // The user who created the book

    @JoinColumn(name = "author_Name", nullable = false)
    private String AuthorName;

}
