package com.example.matching.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    private String content;
    private UUID parentCommentId;  // Used to specify if this comment is a reply
}

