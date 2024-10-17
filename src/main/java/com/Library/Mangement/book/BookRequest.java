package com.Library.Mangement.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Book name is required")
    @Size(max = 255, message = "Book name cannot exceed 255 characters")
    private String bookName;

    @Size(max = 255, message = "Tags cannot exceed 255 characters")
    private String tags;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    @NotNull(message = "PDF file is required")
    private byte[] pdfFile; // For uploading the PDF

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
