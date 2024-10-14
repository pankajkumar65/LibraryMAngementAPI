package com.Library.Mangement.book;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    private String bookName;
    private String tags;
    private String category;
    private byte[]  pdfFile; // For uploading the PDF
    private String description;
}

