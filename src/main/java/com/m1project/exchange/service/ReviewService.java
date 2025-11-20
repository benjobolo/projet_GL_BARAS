
package com.m1project.exchange.service;

import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.Review;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Récupérer tous les avis
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Récupérer un avis par ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Récupérer les avis laissés par un utilisateur
    public List<Review> getReviewsByReviewer(User reviewer) {
        return reviewRepository.findByReviewer(reviewer);
    }

    // Récupérer les avis reçus par un utilisateur
    public List<Review> getReviewsForUser(User user) {
        return reviewRepository.findByReviewedUser(user);
    }

    // Récupérer les avis liés à une demande d'échange
    public List<Review> getReviewsByExchangeRequest(ExchangeRequest exchangeRequest) {
        return reviewRepository.findByExchangeRequest(exchangeRequest);
    }

    // Calculer la note moyenne d'un utilisateur
    public Double getAverageRating(User user) {
        Double average = reviewRepository.calculateAverageRating(user);
        return average != null ? average : 0.0;
    }

    // Créer un avis
    public Review createReview(Review review) {
        // Vérifier que la note est valide
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }

        // Vérifier qu'un avis n'a pas déjà été laissé
        if (reviewRepository.existsByReviewerAndExchangeRequest(review.getReviewer(), review.getExchangeRequest())) {
            throw new IllegalArgumentException("Vous avez déjà laissé un avis pour cet échange");
        }

        return reviewRepository.save(review);
    }

    // Mettre à jour un avis
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avis non trouvé"));

        if (reviewDetails.getRating() < 1 || reviewDetails.getRating() > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }

        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());

        return reviewRepository.save(review);
    }

    // Supprimer un avis
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}