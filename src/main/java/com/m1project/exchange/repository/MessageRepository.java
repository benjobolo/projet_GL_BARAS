package com.m1project.exchange.repository;

import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.Message;
import com.m1project.exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Messages envoyés par un utilisateur
    List<Message> findBySender(User sender);

    // Messages reçus par un utilisateur
    List<Message> findByReceiver(User receiver);

    // Messages liés à une demande d'échange
    List<Message> findByExchangeRequest(ExchangeRequest exchangeRequest);

    // Conversation entre deux utilisateurs (dans les deux sens)
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.sentAt ASC")
    List<Message> findConversationBetween(@Param("user1") User user1, @Param("user2") User user2);

    // Messages non lus d'un utilisateur
    List<Message> findByReceiverAndIsReadFalse(User receiver);
}