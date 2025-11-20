
package com.m1project.exchange.service;

import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.Message;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Récupérer tous les messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Récupérer un message par ID
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Récupérer les messages envoyés par un utilisateur
    public List<Message> getMessagesSentBy(User sender) {
        return messageRepository.findBySender(sender);
    }

    // Récupérer les messages reçus par un utilisateur
    public List<Message> getMessagesReceivedBy(User receiver) {
        return messageRepository.findByReceiver(receiver);
    }

    // Récupérer une conversation entre deux utilisateurs
    public List<Message> getConversationBetween(User user1, User user2) {
        return messageRepository.findConversationBetween(user1, user2);
    }

    // Récupérer les messages liés à une demande d'échange
    public List<Message> getMessagesByExchangeRequest(ExchangeRequest exchangeRequest) {
        return messageRepository.findByExchangeRequest(exchangeRequest);
    }

    // Récupérer les messages non lus d'un utilisateur
    public List<Message> getUnreadMessages(User receiver) {
        return messageRepository.findByReceiverAndIsReadFalse(receiver);
    }

    // Créer un message
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    // Marquer un message comme lu
    public Message markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message non trouvé"));
        message.setRead(true);
        return messageRepository.save(message);
    }

    // Supprimer un message
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}