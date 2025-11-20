package com.m1project.exchange.repository;

import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.Review;
import com.m1project.exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Avis laissés par un utilisateur
    List<Review> findByReviewer(User reviewer);

    // Avis reçus par un utilisateur
    List<Review> findByReviewedUser(User reviewedUser);

    // Avis liés à une demande d'échange
    List<Review> findByExchangeRequest(ExchangeRequest exchangeRequest);

    // Calculer la note moyenne d'un utilisateur
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedUser = :user")
    Double calculateAverageRating(@Param("user") User user);

    // Vérifier si un utilisateur a déjà laissé un avis pour un échange donné
    boolean existsByReviewerAndExchangeRequest(User reviewer, ExchangeRequest exchangeRequest);
}