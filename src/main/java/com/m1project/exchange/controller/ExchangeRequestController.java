
package com.m1project.exchange.controller;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.service.AnnouncementService;
import com.m1project.exchange.service.ExchangeRequestService;
import com.m1project.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exchange-requests")
public class ExchangeRequestController {

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnnouncementService announcementService;

    // Afficher la liste des demandes
    @GetMapping
    public String listRequests(Model model) {
        model.addAttribute("requests", exchangeRequestService.getAllRequests());
        return "exchange-requests";
    }

    // Afficher le formulaire de création
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("request", new ExchangeRequest());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        return "exchange-request-form";
    }

    // Créer une nouvelle demande
    @PostMapping
    public String createRequest(@ModelAttribute ExchangeRequest request,
                                @RequestParam Long requesterId,
                                @RequestParam Long announcementId) {
        User requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        Announcement announcement = announcementService.getAnnouncementById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("Annonce non trouvée"));

        request.setRequester(requester);
        request.setAnnouncement(announcement);
        exchangeRequestService.createRequest(request);
        return "redirect:/exchange-requests";
    }

    // Accepter une demande
    @GetMapping("/accept/{id}")
    public String acceptRequest(@PathVariable Long id) {
        exchangeRequestService.acceptRequest(id);
        return "redirect:/exchange-requests";
    }

    // Refuser une demande
    @GetMapping("/refuse/{id}")
    public String refuseRequest(@PathVariable Long id) {
        exchangeRequestService.refuseRequest(id);
        return "redirect:/exchange-requests";
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ExchangeRequest request = exchangeRequestService.getRequestById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande invalide : " + id));
        model.addAttribute("request", request);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        return "exchange-request-form";
    }

    // Mettre à jour une demande
    @PostMapping("/update/{id}")
    public String updateRequest(@PathVariable Long id,
                                @ModelAttribute ExchangeRequest request,
                                @RequestParam Long requesterId,
                                @RequestParam Long announcementId) {
        User requester = userService.getUserById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        Announcement announcement = announcementService.getAnnouncementById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("Annonce non trouvée"));

        request.setRequester(requester);
        request.setAnnouncement(announcement);
        exchangeRequestService.updateRequest(id, request);
        return "redirect:/exchange-requests";
    }

    // Supprimer une demande
    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id) {
        exchangeRequestService.deleteRequest(id);
        return "redirect:/exchange-requests";
    }
}