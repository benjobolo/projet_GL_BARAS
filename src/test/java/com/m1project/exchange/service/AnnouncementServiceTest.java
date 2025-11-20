package com.m1project.exchange.service;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.AnnouncementType;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.repository.AnnouncementRepository;
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
class AnnouncementServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementService announcementService;

    private User testUser;
    private Announcement testAnnouncement;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "Test User", "Paris");
        testUser.setId(1L);

        testAnnouncement = new Announcement("Perceuse électrique", "Perceuse en bon état", AnnouncementType.OBJET, testUser);
        testAnnouncement.setId(1L);
        testAnnouncement.setCategory("Bricolage");
        testAnnouncement.setAvailable(true);
    }

    @Test
    void testGetAllAnnouncements() {
        // Arrange
        List<Announcement> announcements = Arrays.asList(
                testAnnouncement,
                new Announcement("Cours de guitare", "Débutants bienvenus", AnnouncementType.COMPETENCE, testUser)
        );
        when(announcementRepository.findAll()).thenReturn(announcements);

        // Act
        List<Announcement> result = announcementService.getAllAnnouncements();

        // Assert
        assertEquals(2, result.size());
        verify(announcementRepository, times(1)).findAll();
    }

    @Test
    void testGetAnnouncementById_Success() {
        // Arrange
        when(announcementRepository.findById(1L)).thenReturn(Optional.of(testAnnouncement));

        // Act
        Optional<Announcement> result = announcementService.getAnnouncementById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Perceuse électrique", result.get().getTitle());
    }

    @Test
    void testCreateAnnouncement() {
        // Arrange
        when(announcementRepository.save(any(Announcement.class))).thenReturn(testAnnouncement);

        // Act
        Announcement result = announcementService.createAnnouncement(testAnnouncement);

        // Assert
        assertNotNull(result);
        assertEquals("Perceuse électrique", result.getTitle());
        verify(announcementRepository, times(1)).save(testAnnouncement);
    }

    @Test
    void testGetAnnouncementsByType() {
        // Arrange
        List<Announcement> objets = Arrays.asList(testAnnouncement);
        when(announcementRepository.findByType(AnnouncementType.OBJET)).thenReturn(objets);

        // Act
        List<Announcement> result = announcementService.getAnnouncementsByType(AnnouncementType.OBJET);

        // Assert
        assertEquals(1, result.size());
        assertEquals(AnnouncementType.OBJET, result.get(0).getType());
    }

    @Test
    void testGetAvailableAnnouncements() {
        // Arrange
        when(announcementRepository.findByAvailableTrue()).thenReturn(Arrays.asList(testAnnouncement));

        // Act
        List<Announcement> result = announcementService.getAvailableAnnouncements();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    @Test
    void testSearchAnnouncements() {
        // Arrange
        when(announcementRepository.findByTitleContainingIgnoreCase("perceuse")).thenReturn(Arrays.asList(testAnnouncement));

        // Act
        List<Announcement> result = announcementService.searchAnnouncements("perceuse");

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().toLowerCase().contains("perceuse"));
    }

    @Test
    void testUpdateAnnouncement_Success() {
        // Arrange
        Announcement updatedAnnouncement = new Announcement("Perceuse professionnelle", "Très puissante", AnnouncementType.OBJET, testUser);
        when(announcementRepository.findById(1L)).thenReturn(Optional.of(testAnnouncement));
        when(announcementRepository.save(any(Announcement.class))).thenReturn(testAnnouncement);

        // Act
        Announcement result = announcementService.updateAnnouncement(1L, updatedAnnouncement);

        // Assert
        assertEquals("Perceuse professionnelle", result.getTitle());
        verify(announcementRepository, times(1)).save(testAnnouncement);
    }

    @Test
    void testDeleteAnnouncement() {
        // Arrange
        doNothing().when(announcementRepository).deleteById(1L);

        // Act
        announcementService.deleteAnnouncement(1L);

        // Assert
        verify(announcementRepository, times(1)).deleteById(1L);
    }
}