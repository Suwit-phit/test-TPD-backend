package com.example.matching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.matching.dto.CommentDTO;
import com.example.matching.model.Comment;
import com.example.matching.repository.CommentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentDTO> getAllCommentsWithReplies() {
        return commentRepository.findByParentCommentIsNull()
                .stream()
                .map(this::mapToDTOWithReplies)
                .collect(Collectors.toList());
    }

    private CommentDTO mapToDTOWithReplies(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .candidateId(comment.getCandidate().getId())
                .authorId(comment.getAuthor().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .replies(comment.getReplies() != null
                        ? comment.getReplies().stream().map(this::mapToDTOWithReplies).collect(Collectors.toList())
                        : null)
                .build();
    }

    public List<CommentDTO> getCommentsForCandidate(UUID candidateId) {
        // return commentRepository.findByCandidateId(candidateId)
        // .stream()
        // .map(this::mapToDTO)
        // .collect(Collectors.toList());
        // Fetch only top-level comments (parentCommentId is null)
        return commentRepository.findByCandidateIdAndParentCommentIdIsNull(candidateId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getRepliesForComment(UUID commentId) {
        return commentRepository.findByParentCommentId(commentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        return mapToDTO(savedComment);
    }

    @Transactional
    public CommentDTO updateComment(UUID commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentDTO mapToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .candidateId(comment.getCandidate().getId())
                .authorId(comment.getAuthor().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .replies(comment.getReplies() != null
                        ? comment.getReplies().stream().map(this::mapToDTO).collect(Collectors.toList())
                        : null)
                .build();
    }
}

// @Service
// @RequiredArgsConstructor
// public class CommentService {

// private final CommentRepository commentRepository;

// public List<CommentDTO> getAllCommentsWithReplies() {
// return commentRepository.findByParentCommentIsNull()
// .stream()
// .map(this::mapToDTOWithReplies)
// .collect(Collectors.toList());
// }

// private CommentDTO mapToDTOWithReplies(Comment comment) {
// return CommentDTO.builder()
// .id(comment.getId())
// .candidateId(comment.getCandidate().getId())
// .authorId(comment.getAuthor().getId())
// .authorUsername(comment.getAuthor().getUsername())
// .content(comment.getContent())
// .createdAt(comment.getCreatedAt())
// .replies(comment.getReplies() != null
// ?
// comment.getReplies().stream().map(this::mapToDTOWithReplies).collect(Collectors.toList())
// : null)
// .build();
// }

// public List<CommentDTO> getCommentsForCandidate(UUID candidateId) {
// return commentRepository.findByCandidateId(candidateId)
// .stream()
// .map(this::mapToDTO)
// .collect(Collectors.toList());
// }

// public List<CommentDTO> getRepliesForComment(UUID commentId) {
// return commentRepository.findByParentCommentId(commentId)
// .stream()
// .map(this::mapToDTO)
// .collect(Collectors.toList());
// }

// public CommentDTO createComment(Comment comment) {
// Comment savedComment = commentRepository.save(comment);
// return mapToDTO(savedComment);
// }

// @Transactional
// public CommentDTO updateComment(UUID commentId, String content) {
// Comment comment = commentRepository.findById(commentId)
// .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
// comment.setContent(content);
// Comment updatedComment = commentRepository.save(comment);
// return mapToDTO(updatedComment);
// }

// public void deleteComment(UUID commentId) {
// commentRepository.deleteById(commentId);
// }

// private CommentDTO mapToDTO(Comment comment) {
// return CommentDTO.builder()
// .id(comment.getId())
// .candidateId(comment.getCandidate().getId())
// .authorId(comment.getAuthor().getId())
// .authorUsername(comment.getAuthor().getUsername()) // Map author username
// .content(comment.getContent())
// .createdAt(comment.getCreatedAt())
// .replies(comment.getReplies() != null
// ?
// comment.getReplies().stream().map(this::mapToDTO).collect(Collectors.toList())
// : null)
// .build();
// }
// }

// ! End
// package com.example.matching.service;

// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.example.matching.model.Comment;
// import com.example.matching.repository.CommentRepository;

// import java.util.List;
// import java.util.UUID;

// @Service
// @RequiredArgsConstructor
// public class CommentService {

// private final CommentRepository commentRepository;

// public List<Comment> getCommentsForCandidate(UUID candidateId) {
// return commentRepository.findByCandidateId(candidateId);
// }

// public List<Comment> getRepliesForComment(UUID commentId) {
// return commentRepository.findByParentCommentId(commentId);
// }

// public Comment createComment(Comment comment) {
// return commentRepository.save(comment);
// }

// @Transactional
// public Comment updateComment(UUID commentId, String content) {
// Comment comment = commentRepository.findById(commentId)
// .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
// comment.setContent(content);
// return commentRepository.save(comment);
// }

// public void deleteComment(UUID commentId) {
// commentRepository.deleteById(commentId);
// }
// }
