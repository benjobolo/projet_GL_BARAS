
package com.m1project.exchange.controller;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.AnnouncementType;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.service.AnnouncementService;
import com.m1project.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private UserService userService;

    // Afficher la liste des annonces
    @GetMapping
    public String listAnnouncements(Model model) {
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        return "announcements";
    }

    // Afficher le formulaire de création
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("announcement", new Announcement());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("types", AnnouncementType.values());
        return "announcement-form";
    }

    // Créer une nouvelle annonce
    @PostMapping
    public String createAnnouncement(@ModelAttribute Announcement announcement, @RequestParam Long ownerId) {
        User owner = userService.getUserById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        announcement.setOwner(owner);
        announcementService.createAnnouncement(announcement);
        return "redirect:/announcements";
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Announcement announcement = announcementService.getAnnouncementById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annonce invalide : " + id));
        model.addAttribute("announcement", announcement);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("types", AnnouncementType.values());
        return "announcement-form";
    }

    // Mettre à jour une annonce
    @PostMapping("/update/{id}")
    public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement announcement, @RequestParam Long ownerId) {
        User owner = userService.getUserById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        announcement.setOwner(owner);
        announcementService.updateAnnouncement(id, announcement);
        return "redirect:/announcements";
    }

    // Supprimer une annonce
    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/announcements";
    }
}