package com.example.matching.controller;

import lombok.RequiredArgsConstructor;

import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.matching.dto.CommentDTO;
import com.example.matching.model.Candidate;
import com.example.matching.model.Comment;
import com.example.matching.model.User;
import com.example.matching.repository.CommentRepository;
import com.example.matching.repository.UserRepository;
import com.example.matching.service.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsWithReplies() {
        return ResponseEntity.ok(commentService.getAllCommentsWithReplies());
    }

    @GetMapping("/candidate/{candidateId}")
    public List<CommentDTO> getCommentsForCandidate(@PathVariable UUID candidateId) {
        return commentService.getCommentsForCandidate(candidateId);
    }

    @GetMapping("/{commentId}/replies")
    public List<CommentDTO> getRepliesForComment(@PathVariable UUID commentId) {
        return commentService.getRepliesForComment(commentId);
    }

    @PostMapping("/candidate/{candidateId}")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable UUID candidateId,
            @RequestParam UUID authorId,
            @RequestBody String content) {

        // Retrieve the author (User) based on authorId
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create the Comment with the author's username
        Comment comment = Comment.builder()
                .candidate(Candidate.builder().id(candidateId).build())
                .author(author) // Assign the author
                .content(content)
                .build();

        CommentDTO savedCommentDTO = commentService.createComment(comment);
        return ResponseEntity.ok(savedCommentDTO);
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<CommentDTO> replyToComment(
            @PathVariable UUID commentId,
            @RequestParam UUID authorId,
            @RequestBody String content) {

        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment reply = Comment.builder()
                .parentComment(parentComment)
                .author(author)
                .content(content)
                .candidate(parentComment.getCandidate()) // if candidate is needed for replies
                .build();

        CommentDTO replyDTO = commentService.createComment(reply);
        return ResponseEntity.ok(replyDTO);
    }

    //? Update a Comment and reply comment
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable UUID commentId,
            @RequestBody String content) {

        return ResponseEntity.ok(commentService.updateComment(commentId, content));
    }

    //? Delete a Comment and reply comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

// @PostMapping("/{commentId}/reply")
    // public ResponseEntity<CommentDTO> replyToComment(
    // @PathVariable UUID commentId,
    // @RequestParam UUID authorId,
    // @RequestBody String content) {

    // Comment reply = Comment.builder()
    // .parentComment(Comment.builder().id(commentId).build())
    // .author(User.builder().id(authorId).build())
    // .content(content)
    // .build();

    // CommentDTO replyDTO = commentService.createComment(reply);
    // return ResponseEntity.ok(replyDTO);
    // }

// ! End
// package com.example.matching.controller;

// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.matching.dto.CommentDTO;
// import com.example.matching.model.Candidate;
// import com.example.matching.model.Comment;
// import com.example.matching.model.User;
// import com.example.matching.repository.UserRepository;
// import com.example.matching.service.CommentService;

// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/comments")
// @RequiredArgsConstructor
// public class CommentController {

// private final CommentService commentService;

// @GetMapping("/all")
// public ResponseEntity<List<CommentDTO>> getAllCommentsWithReplies() {
// return ResponseEntity.ok(commentService.getAllCommentsWithReplies());
// }

// @GetMapping("/candidate/{candidateId}")
// public List<CommentDTO> getCommentsForCandidate(@PathVariable UUID
// candidateId) {
// return commentService.getCommentsForCandidate(candidateId);
// }

// @GetMapping("/{commentId}/replies")
// public List<CommentDTO> getRepliesForComment(@PathVariable UUID commentId) {
// return commentService.getRepliesForComment(commentId);
// }

// private final UserRepository userRepository;

// public CommentController(CommentService commentService, UserRepository
// userRepository) {
// this.commentService = commentService;
// this.userRepository = userRepository;
// }

// @PostMapping("/candidate/{candidateId}")
// public ResponseEntity<CommentDTO> createComment(
// @PathVariable UUID candidateId,
// @RequestParam UUID authorId,
// @RequestBody String content) {

// // Retrieve the author (User) based on authorId
// User author = userRepository.findById(authorId)
// .orElseThrow(() -> new IllegalArgumentException("User not found"));

// // Create the Comment with the author's username
// Comment comment = Comment.builder()
// .candidate(Candidate.builder().id(candidateId).build())
// .author(author) // Assign the author
// .content(content)
// .build();

// Comment savedComment = commentService.createComment(comment);

// // Map to CommentDTO and include the author's username
// CommentDTO commentDTO = CommentDTO.builder()
// .id(savedComment.getId())
// .candidateId(savedComment.getCandidate().getId())
// .authorId(savedComment.getAuthor().getId())
// .authorUsername(savedComment.getAuthor().getUsername()) // Set authorUsername
// here
// .content(savedComment.getContent())
// .createdAt(savedComment.getCreatedAt())
// .replies(null) // or fetch replies if needed
// .build();

// return ResponseEntity.ok(commentDTO);
// }

// @PostMapping("/{commentId}/reply")
// public ResponseEntity<CommentDTO> replyToComment(
// @PathVariable UUID commentId,
// @RequestParam UUID authorId,
// @RequestBody String content) {
// Comment reply = Comment.builder()
// .parentComment(Comment.builder().id(commentId).build())
// .author(User.builder().id(authorId).build())
// .content(content)
// .build();
// return ResponseEntity.ok(commentService.createComment(reply));
// }

// @PutMapping("/{commentId}")
// public ResponseEntity<CommentDTO> updateComment(
// @PathVariable UUID commentId,
// @RequestBody String content) {
// return ResponseEntity.ok(commentService.updateComment(commentId, content));
// }

// @DeleteMapping("/{commentId}")
// public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
// commentService.deleteComment(commentId);
// return ResponseEntity.noContent().build();
// }
// }

// ! End
// @PostMapping("/candidate/{candidateId}")
// public ResponseEntity<CommentDTO> createComment(
// @PathVariable UUID candidateId,
// @RequestParam UUID authorId,
// @RequestBody String content) {
// Comment comment = Comment.builder()
// .candidate(Candidate.builder().id(candidateId).build())
// .author(User.builder().id(authorId).build())
// .content(content)
// .build();
// return ResponseEntity.ok(commentService.createComment(comment));
// }
