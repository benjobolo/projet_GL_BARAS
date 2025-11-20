package com.m1project.exchange.service;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.AnnouncementType;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    // Récupérer toutes les annonces
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    // Récupérer une annonce par ID
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }

    // Récupérer les annonces d'un utilisateur
    public List<Announcement> getAnnouncementsByOwner(User owner) {
        return announcementRepository.findByOwner(owner);
    }

    // Récupérer les annonces par type
    public List<Announcement> getAnnouncementsByType(AnnouncementType type) {
        return announcementRepository.findByType(type);
    }

    // Récupérer seulement les annonces disponibles
    public List<Announcement> getAvailableAnnouncements() {
        return announcementRepository.findByAvailableTrue();
    }

    // Rechercher des annonces par mot-clé
    public List<Announcement> searchAnnouncements(String keyword) {
        return announcementRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Créer une annonce
    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    // Mettre à jour une annonce
    public Announcement updateAnnouncement(Long id, Announcement announcementDetails) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annonce non trouvée"));

        announcement.setTitle(announcementDetails.getTitle());
        announcement.setDescription(announcementDetails.getDescription());
        announcement.setType(announcementDetails.getType());
        announcement.setCategory(announcementDetails.getCategory());
        announcement.setImageUrl(announcementDetails.getImageUrl());
        announcement.setAvailable(announcementDetails.isAvailable());
        announcement.setExchangeComment(announcementDetails.getExchangeComment());

        return announcementRepository.save(announcement);
    }

    // Supprimer une annonce
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}