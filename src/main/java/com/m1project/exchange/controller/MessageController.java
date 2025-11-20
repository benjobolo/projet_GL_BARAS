package com.m1project.exchange.controller;

import com.m1project.exchange.entity.ExchangeRequest;
import com.m1project.exchange.entity.Message;
import com.m1project.exchange.entity.User;
import com.m1project.exchange.service.ExchangeRequestService;
import com.m1project.exchange.service.MessageService;
import com.m1project.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    // Afficher la liste des messages
    @GetMapping
    public String listMessages(Model model) {
        model.addAttribute("messages", messageService.getAllMessages());
        return "messages";
    }

    // Afficher le formulaire de création
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("message", new Message());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("exchangeRequests", exchangeRequestService.getAllRequests());
        return "message-form";
    }

    // Créer un nouveau message
    @PostMapping
    public String createMessage(@ModelAttribute Message message,
                                @RequestParam Long senderId,
                                @RequestParam Long receiverId,
                                @RequestParam(required = false) Long exchangeRequestId) {
        User sender = userService.getUserById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur non trouvé"));
        User receiver = userService.getUserById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire non trouvé"));

        message.setSender(sender);
        message.setReceiver(receiver);

        if (exchangeRequestId != null) {
            ExchangeRequest exchangeRequest = exchangeRequestService.getRequestById(exchangeRequestId)
                    .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée"));
            message.setExchangeRequest(exchangeRequest);
        }

        messageService.createMessage(message);
        return "redirect:/messages";
    }

    // Marquer un message comme lu
    @GetMapping("/mark-read/{id}")
    public String markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return "redirect:/messages";
    }

    // Supprimer un message
    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return "redirect:/messages";
    }

    // Afficher une conversation entre deux utilisateurs
    @GetMapping("/conversation")
    public String showConversation(@RequestParam Long user1Id, @RequestParam Long user2Id, Model model) {
        User user1 = userService.getUserById(user1Id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur 1 non trouvé"));
        User user2 = userService.getUserById(user2Id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur 2 non trouvé"));

        model.addAttribute("user1", user1);
        model.addAttribute("user2", user2);
        model.addAttribute("messages", messageService.getConversationBetween(user1, user2));
        return "conversation";
    }
}