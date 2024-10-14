package com.Library.Mangement.book;

import com.Library.Mangement.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @Column(unique = true, nullable = false)
    private String bookName;
    @Column
    private String tags;
    @Column
    private String category;
    @Lob
    @Column
    private byte[] pdfFile; // To store the PDF as a byte array
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private User author; // The user who created the book

    @JoinColumn(name = "author_Name", nullable = false)
    private String AuthorName;
}
