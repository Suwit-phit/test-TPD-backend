package com.example.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.matching.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    
    // Find comments by candidate ID
    List<Comment> findByCandidateId(UUID candidateId);
    
    // Find comments by parent comment ID
    List<Comment> findByParentCommentId(UUID parentCommentId);
    
    // Fetch top-level comments (comments without a parent comment)
    List<Comment> findByParentCommentIsNull();

    // Fetch top-level comments by candidate ID (comments without a parent comment)
    // List<Comment> findByCandidateIdAndParentCommentIsNull(UUID candidateId);
    List<Comment> findByCandidateIdAndParentCommentIdIsNull(UUID candidateId);
    
    // Delete all comments associated with a specific candidate
    void deleteAllByCandidateId(UUID candidateId);
}


// package com.example.matching.repository;

// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.matching.model.Comment;

// import java.util.List;
// import java.util.UUID;

// public interface CommentRepository extends JpaRepository<Comment, UUID> {
//     List<Comment> findByCandidateId(UUID candidateId);
    
//     List<Comment> findByParentCommentId(UUID parentCommentId);

//     // New method to fetch top-level comments
//     List<Comment> findByParentCommentIsNull();
// }

//! End
// public interface CommentRepository extends JpaRepository<Comment, UUID> {
//     List<Comment> findByCandidateId(UUID candidateId);

//     List<Comment> findByParentCommentId(UUID parentCommentId);
// }
