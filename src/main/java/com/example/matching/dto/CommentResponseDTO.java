package com.example.matching.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private UUID id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private List<CommentResponseDTO> replies;
}
