package com.example.matching.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments_fixCanVan")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

// @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    // private UUID id;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "candidate_id", nullable = false)
    // private Candidate candidate;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User author;

    // @Column(nullable = false, length = 500)
    // private String content;

    // @Column(name = "created_at", nullable = false, updatable = false)
    // private LocalDateTime createdAt = LocalDateTime.now();

    // @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL,
    // orphanRemoval = true)
    // private List<Comment> replies;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "parent_comment_id")
    // private Comment parentComment;
