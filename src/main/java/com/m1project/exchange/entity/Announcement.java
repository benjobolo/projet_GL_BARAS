
package com.m1project.exchange.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnnouncementType type; // OBJET ou COMPETENCE

    private String category; // ex: "Bricolage", "Jardinage", "Électronique"

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean available = true; // Disponible ou non

    @Column(length = 500)
    private String exchangeComment; // Ce que l'utilisateur souhaite en échange

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner; // Le créateur de l'annonce

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructeurs
    public Announcement() {}

    public Announcement(String title, String description, AnnouncementType type, User owner) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.owner = owner;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AnnouncementType getType() {
        return type;
    }

    public void setType(AnnouncementType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getExchangeComment() {
        return exchangeComment;
    }

    public void setExchangeComment(String exchangeComment) {
        this.exchangeComment = exchangeComment;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}