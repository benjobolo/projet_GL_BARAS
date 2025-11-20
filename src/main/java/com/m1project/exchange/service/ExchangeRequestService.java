package com.m1project.exchange.service;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.ExchangeStatus;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.repository.ExchangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRequestService {

    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    // Récupérer toutes les demandes
    public List<ExchangeRequest> getAllRequests() {
        return exchangeRequestRepository.findAll();
    }

    // Récupérer une demande par ID
    public Optional<ExchangeRequest> getRequestById(Long id) {
        return exchangeRequestRepository.findById(id);
    }

    // Récupérer les demandes envoyées par un utilisateur
    public List<ExchangeRequest> getRequestsByRequester(User requester) {
        return exchangeRequestRepository.findByRequester(requester);
    }

    // Récupérer les demandes reçues par un propriétaire
    public List<ExchangeRequest> getRequestsReceivedByOwner(User owner) {
        return exchangeRequestRepository.findByAnnouncementOwner(owner);
    }

    // Récupérer les demandes pour une annonce
    public List<ExchangeRequest> getRequestsByAnnouncement(Announcement announcement) {
        return exchangeRequestRepository.findByAnnouncement(announcement);
    }

    // Créer une demande
    public ExchangeRequest createRequest(ExchangeRequest request) {
        return exchangeRequestRepository.save(request);
    }

    // Accepter une demande
    public ExchangeRequest acceptRequest(Long id) {
        ExchangeRequest request = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée"));
        request.setStatus(ExchangeStatus.ACCEPTED);
        request.setRespondedAt(LocalDateTime.now());
        return exchangeRequestRepository.save(request);
    }

    // Refuser une demande
    public ExchangeRequest refuseRequest(Long id) {
        ExchangeRequest request = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée"));
        request.setStatus(ExchangeStatus.REFUSED);
        request.setRespondedAt(LocalDateTime.now());
        return exchangeRequestRepository.save(request);
    }

    // Mettre à jour une demande
    public ExchangeRequest updateRequest(Long id, ExchangeRequest requestDetails) {
        ExchangeRequest request = exchangeRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée"));

        request.setOfferDescription(requestDetails.getOfferDescription());
        request.setProposedDate(requestDetails.getProposedDate());

        return exchangeRequestRepository.save(request);
    }

    // Supprimer une demande
    public void deleteRequest(Long id) {
        exchangeRequestRepository.deleteById(id);
    }
}