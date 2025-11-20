package com.m1project.exchange.service;

import com.m1project.exchange.entity.*;
import com.m1project.exchange.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User reviewer;
    private User reviewedUser;
    private ExchangeRequest exchangeRequest;
    private Review testReview;

    @BeforeEach
    void setUp() {
        reviewer = new User("reviewer@example.com", "Reviewer", "Paris");
        reviewer.setId(1L);

        reviewedUser = new User("reviewed@example.com", "Reviewed User", "Lyon");
        reviewedUser.setId(2L);

        Announcement announcement = new Announcement("Perceuse", "Description", AnnouncementType.OBJET, reviewedUser);
        announcement.setId(1L);

        exchangeRequest = new ExchangeRequest(announcement, reviewer, "Je propose ma ponceuse");
        exchangeRequest.setId(1L);
        exchangeRequest.setStatus(ExchangeStatus.ACCEPTED);

        testReview = new Review(reviewer, reviewedUser, exchangeRequest, 5);
        testReview.setId(1L);
        testReview.setComment("Excellent échange !");
    }

    @Test
    void testGetAllReviews() {
        // Arrange
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(testReview));

        // Act
        List<Review> result = reviewService.getAllReviews();

        // Assert
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetReviewById() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        // Act
        Optional<Review> result = reviewService.getReviewById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(5, result.get().getRating());
    }

    @Test
    void testCreateReview_Success() {
        // Arrange
        when(reviewRepository.existsByReviewerAndExchangeRequest(reviewer, exchangeRequest)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // Act
        Review result = reviewService.createReview(testReview);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getRating());
        verify(reviewRepository, times(1)).save(testReview);
    }

    @Test
    void testCreateReview_InvalidRating_TooLow() {
        // Arrange
        Review invalidReview = new Review(reviewer, reviewedUser, exchangeRequest, 0);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(invalidReview);
        });

        assertEquals("La note doit être entre 1 et 5", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCreateReview_InvalidRating_TooHigh() {
        // Arrange
        Review invalidReview = new Review(reviewer, reviewedUser, exchangeRequest, 6);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(invalidReview);
        });

        assertEquals("La note doit être entre 1 et 5", exception.getMessage());
    }

    @Test
    void testCreateReview_AlreadyReviewed() {
        // Arrange
        when(reviewRepository.existsByReviewerAndExchangeRequest(reviewer, exchangeRequest)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(testReview);
        });

        assertEquals("Vous avez déjà laissé un avis pour cet échange", exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testGetAverageRating() {
        // Arrange
        when(reviewRepository.calculateAverageRating(reviewedUser)).thenReturn(4.5);

        // Act
        Double result = reviewService.getAverageRating(reviewedUser);

        // Assert
        assertEquals(4.5, result);
    }

    @Test
    void testGetAverageRating_NoReviews() {
        // Arrange
        when(reviewRepository.calculateAverageRating(reviewedUser)).thenReturn(null);

        // Act
        Double result = reviewService.getAverageRating(reviewedUser);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void testGetReviewsForUser() {
        // Arrange
        when(reviewRepository.findByReviewedUser(reviewedUser)).thenReturn(Arrays.asList(testReview));

        // Act
        List<Review> result = reviewService.getReviewsForUser(reviewedUser);

        // Assert
        assertEquals(1, result.size());
        assertEquals(reviewedUser, result.get(0).getReviewedUser());
    }

    @Test
    void testUpdateReview_Success() {
        // Arrange
        Review updatedReview = new Review(reviewer, reviewedUser, exchangeRequest, 4);
        updatedReview.setComment("Bon échange");
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        // Act
        Review result = reviewService.updateReview(1L, updatedReview);

        // Assert
        assertEquals(4, result.getRating());
        assertEquals("Bon échange", result.getComment());
    }

    @Test
    void testDeleteReview() {
        // Arrange
        doNothing().when(reviewRepository).deleteById(1L);

        // Act
        reviewService.deleteReview(1L);

        // Assert
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}