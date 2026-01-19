package com.example.mindStreamApplication.Domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long tvShowId;

    private LocalDateTime addedAt;


    public Favorite() {
    }

    public Favorite(Long userId, Long tvShowId) {
        this.userId = userId;
        this.tvShowId = tvShowId;
        this.addedAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(Long tvShowId) {
        this.tvShowId = tvShowId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "addedAt=" + addedAt +
                ", id=" + id +
                ", userId=" + userId +
                ", tvShowId=" + tvShowId +
                '}';
    }
}