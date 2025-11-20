
package com.m1project.exchange.repository;

import com.m1project.exchange.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    private User reviewer;
    private User reviewedUser;
    private ExchangeRequest exchangeRequest;

    @BeforeEach
    void setUp() {
        // Créer les utilisateurs
        reviewer = new User("reviewer@example.com", "Reviewer", "Paris");
        reviewedUser = new User("reviewed@example.com", "Reviewed", "Lyon");
        entityManager.persistAndFlush(reviewer);
        entityManager.persistAndFlush(reviewedUser);

        // Créer une annonce
        Announcement announcement = new Announcement("Perceuse", "Description", AnnouncementType.OBJET, reviewedUser);
        entityManager.persistAndFlush(announcement);

        // Créer une demande d'échange
        exchangeRequest = new ExchangeRequest(announcement, reviewer, "Je propose ma ponceuse");
        exchangeRequest.setStatus(ExchangeStatus.ACCEPTED);
        entityManager.persistAndFlush(exchangeRequest);
    }

    @Test
    void testSaveAndFindReview() {
        // Arrange
        Review review = new Review(reviewer, reviewedUser, exchangeRequest, 5);
        review.setComment("Excellent échange !");

        // Act
        Review saved = entityManager.persistAndFlush(review);
        Review found = reviewRepository.findById(saved.getId()).orElse(null);

        // Assert
        assertNotNull(found);
        assertEquals(5, found.getRating());
        assertEquals("Excellent échange !", found.getComment());
    }

    @Test
    void testFindByReviewer() {
        // Arrange
        Review review = new Review(reviewer, reviewedUser, exchangeRequest, 4);
        entityManager.persistAndFlush(review);

        // Act
        List<Review> reviews = reviewRepository.findByReviewer(reviewer);

        // Assert
        assertEquals(1, reviews.size());
        assertEquals(reviewer.getId(), reviews.get(0).getReviewer().getId());
    }

    @Test
    void testFindByReviewedUser() {
        // Arrange
        Review review1 = new Review(reviewer, reviewedUser, exchangeRequest, 5);
        entityManager.persistAndFlush(review1);

        // Act
        List<Review> reviews = reviewRepository.findByReviewedUser(reviewedUser);

        // Assert
        assertTrue(reviews.size() >= 1);
        assertEquals(reviewedUser.getId(), reviews.get(0).getReviewedUser().getId());
    }

    @Test
    void testFindByExchangeRequest() {
        // Arrange
        Review review = new Review(reviewer, reviewedUser, exchangeRequest, 5);
        entityManager.persistAndFlush(review);

        // Act
        List<Review> reviews = reviewRepository.findByExchangeRequest(exchangeRequest);

        // Assert
        assertEquals(1, reviews.size());
        assertEquals(exchangeRequest.getId(), reviews.get(0).getExchangeRequest().getId());
    }

    @Test
    void testCalculateAverageRating() {
        // Arrange
        Review review1 = new Review(reviewer, reviewedUser, exchangeRequest, 5);
        Review review2 = new Review(reviewer, reviewedUser, exchangeRequest, 3);
        entityManager.persistAndFlush(review1);
        entityManager.persistAndFlush(review2);

        // Act
        Double average = reviewRepository.calculateAverageRating(reviewedUser);

        // Assert
        assertNotNull(average);
        assertEquals(4.0, average, 0.1); // Moyenne de 5 et 3 = 4
    }

    @Test
    void testCalculateAverageRating_NoReviews() {
        // Arrange
        User userWithoutReviews = new User("norevi@example.com", "No Reviews", "Nice");
        entityManager.persistAndFlush(userWithoutReviews);

        // Act
        Double average = reviewRepository.calculateAverageRating(userWithoutReviews);

        // Assert
        assertNull(average); // Pas d'avis = null
    }

    @Test
    void testExistsByReviewerAndExchangeRequest() {
        // Arrange
        Review review = new Review(reviewer, reviewedUser, exchangeRequest, 5);
        entityManager.persistAndFlush(review);

        // Act
        boolean exists = reviewRepository.existsByReviewerAndExchangeRequest(reviewer, exchangeRequest);

        // Créer un nouvel échange pour tester le cas négatif
        Announcement newAnnouncement = new Announcement("Autre", "Desc", AnnouncementType.OBJET, reviewedUser);
        entityManager.persistAndFlush(newAnnouncement);
        ExchangeRequest newRequest = new ExchangeRequest(newAnnouncement, reviewer, "Autre offre");
        entityManager.persistAndFlush(newRequest);
        boolean notExists = reviewRepository.existsByReviewerAndExchangeRequest(reviewer, newRequest);

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }
}