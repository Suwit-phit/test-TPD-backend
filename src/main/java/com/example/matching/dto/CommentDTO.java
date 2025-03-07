package com.example.matching.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private UUID id;
    private UUID candidateId;
    private UUID authorId;
    private String authorUsername;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentDTO> replies;
}
