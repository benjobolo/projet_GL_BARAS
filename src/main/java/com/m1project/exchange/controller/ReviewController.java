package com.m1project.exchange.controller;

import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.Review;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.service.ExchangeRequestService;
import com.m1project.exchange.service.ReviewService;
import com.m1project.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    // Afficher la liste des avis
    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "reviews";
    }

    // Afficher le formulaire de création
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("exchangeRequests", exchangeRequestService.getAllRequests());
        return "review-form";
    }

    // Créer un nouvel avis
    @PostMapping
    public String createReview(@ModelAttribute Review review,
                               @RequestParam Long reviewerId,
                               @RequestParam Long reviewedUserId,
                               @RequestParam Long exchangeRequestId,
                               Model model) {  // ← Ajout du paramètre Model
        User reviewer = userService.getUserById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer non trouvé"));
        User reviewedUser = userService.getUserById(reviewedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur évalué non trouvé"));
        ExchangeRequest exchangeRequest = exchangeRequestService.getRequestById(exchangeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande d'échange non trouvée"));

        review.setReviewer(reviewer);
        review.setReviewedUser(reviewedUser);
        review.setExchangeRequest(exchangeRequest);

        try {
            reviewService.createReview(review);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("review", review);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("exchangeRequests", exchangeRequestService.getAllRequests());
            return "review-form";
        }

        return "redirect:/reviews";
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Review review = reviewService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Avis invalide : " + id));
        model.addAttribute("review", review);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("exchangeRequests", exchangeRequestService.getAllRequests());
        return "review-form";
    }

    // Mettre à jour un avis
    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable Long id,
                               @ModelAttribute Review review,
                               @RequestParam Long reviewerId,
                               @RequestParam Long reviewedUserId,
                               @RequestParam Long exchangeRequestId) {
        User reviewer = userService.getUserById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Reviewer non trouvé"));
        User reviewedUser = userService.getUserById(reviewedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur évalué non trouvé"));
        ExchangeRequest exchangeRequest = exchangeRequestService.getRequestById(exchangeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Demande d'échange non trouvée"));

        review.setReviewer(reviewer);
        review.setReviewedUser(reviewedUser);
        review.setExchangeRequest(exchangeRequest);

        reviewService.updateReview(id, review);
        return "redirect:/reviews";
    }

    // Supprimer un avis
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/reviews";
    }

    // Afficher les avis d'un utilisateur
    @GetMapping("/user/{userId}")
    public String showUserReviews(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviewService.getReviewsForUser(user));
        model.addAttribute("averageRating", reviewService.getAverageRating(user));
        return "user-reviews";
    }
}