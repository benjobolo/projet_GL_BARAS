package com.m1project.exchange.repository;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.AnnouncementType;
import com.m1project.exchange.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnnouncementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnnouncementRepository announcementRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("owner@example.com", "Owner", "Paris");
        entityManager.persistAndFlush(testUser);
    }

    @Test
    void testSaveAndFindAnnouncement() {
        // Arrange
        Announcement announcement = new Announcement("Perceuse", "Perceuse électrique", AnnouncementType.OBJET, testUser);
        announcement.setCategory("Bricolage");

        // Act
        Announcement saved = entityManager.persistAndFlush(announcement);
        Optional<Announcement> found = announcementRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Perceuse", found.get().getTitle());
        assertEquals(AnnouncementType.OBJET, found.get().getType());
    }

    @Test
    void testFindByOwner() {
        // Arrange
        Announcement ann1 = new Announcement("Perceuse", "Description", AnnouncementType.OBJET, testUser);
        Announcement ann2 = new Announcement("Cours guitare", "Description", AnnouncementType.COMPETENCE, testUser);
        entityManager.persistAndFlush(ann1);
        entityManager.persistAndFlush(ann2);

        // Act
        List<Announcement> announcements = announcementRepository.findByOwner(testUser);

        // Assert
        assertEquals(2, announcements.size());
    }

    @Test
    void testFindByType() {
        // Arrange
        Announcement objet = new Announcement("Vélo", "VTT", AnnouncementType.OBJET, testUser);
        Announcement competence = new Announcement("Jardinage", "Aide jardinage", AnnouncementType.COMPETENCE, testUser);
        entityManager.persistAndFlush(objet);
        entityManager.persistAndFlush(competence);

        // Act
        List<Announcement> objets = announcementRepository.findByType(AnnouncementType.OBJET);
        List<Announcement> competences = announcementRepository.findByType(AnnouncementType.COMPETENCE);

        // Assert
        assertTrue(objets.size() >= 1);
        assertTrue(competences.size() >= 1);
        assertEquals(AnnouncementType.OBJET, objets.get(0).getType());
    }

    @Test
    void testFindByAvailableTrue() {
        // Arrange
        Announcement available = new Announcement("Disponible", "Description", AnnouncementType.OBJET, testUser);
        available.setAvailable(true);
        Announcement unavailable = new Announcement("Non disponible", "Description", AnnouncementType.OBJET, testUser);
        unavailable.setAvailable(false);
        entityManager.persistAndFlush(available);
        entityManager.persistAndFlush(unavailable);

        // Act
        List<Announcement> availableAnnouncements = announcementRepository.findByAvailableTrue();

        // Assert
        assertTrue(availableAnnouncements.size() >= 1);
        assertTrue(availableAnnouncements.stream().allMatch(Announcement::isAvailable));
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        // Arrange
        Announcement ann1 = new Announcement("Perceuse électrique", "Description", AnnouncementType.OBJET, testUser);
        Announcement ann2 = new Announcement("Ponceuse manuelle", "Description", AnnouncementType.OBJET, testUser);
        entityManager.persistAndFlush(ann1);
        entityManager.persistAndFlush(ann2);

        // Act
        List<Announcement> results = announcementRepository.findByTitleContainingIgnoreCase("perceuse");

        // Assert
        assertEquals(1, results.size());
        assertTrue(results.get(0).getTitle().toLowerCase().contains("perceuse"));
    }

    @Test
    void testDeleteAnnouncement() {
        // Arrange
        Announcement announcement = new Announcement("À supprimer", "Description", AnnouncementType.OBJET, testUser);
        Announcement saved = entityManager.persistAndFlush(announcement);
        Long id = saved.getId();

        // Act
        announcementRepository.deleteById(id);
        Optional<Announcement> deleted = announcementRepository.findById(id);

        // Assert
        assertFalse(deleted.isPresent());
    }
}