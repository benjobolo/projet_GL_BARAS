
package com.m1project.exchange.repository;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.ExchangeStatus;
import com.m1project.exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {

    // Récupérer les demandes envoyées par un utilisateur
    List<ExchangeRequest> findByRequester(User requester);

    // Récupérer les demandes reçues par un propriétaire d'annonce
    List<ExchangeRequest> findByAnnouncementOwner(User owner);

    // Récupérer les demandes pour une annonce spécifique
    List<ExchangeRequest> findByAnnouncement(Announcement announcement);

    // Récupérer les demandes par statut
    List<ExchangeRequest> findByStatus(ExchangeStatus status);
}