package com.khanghoang.server.network.rest.controller;

import com.khanghoang.server.model.Conversation;
import com.khanghoang.server.service.ConversationService;
import io.javalin.http.Context;

import java.util.List;

public class ConversationController {
    private final ConversationService service;

    public ConversationController(ConversationService service) {
        this.service = service;
    }

    public void handleCreateConversation(Context ctx) {
        try {
            // Giả định body là Conversation object + participantIds
            CreateConversationRequest request = ctx.bodyAsClass(CreateConversationRequest.class);

            Conversation conversation = service.createConversation(
                    request.name,
                    request.isGroup,
                    request.createdBy,
                    request.participantIds
            );

            ctx.status(201).json(conversation);
        } catch (Exception e) {
            ctx.status(400).result("Invalid request: " + e.getMessage());
        }
    }

    public void handleGetConversationsForUser(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            List<Conversation> conversations = service.getConversationsForUser(userId);
            ctx.json(conversations);
        } catch (Exception e) {
            ctx.status(400).result("Invalid user ID: " + e.getMessage());
        }
    }

    public void handleGetConversationById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("conversationId"));
            Conversation conversation = service.getConversationById(id);
            if (conversation != null) {
                ctx.json(conversation);
            } else {
                ctx.status(404).result("Conversation not found");
            }
        } catch (Exception e) {
            ctx.status(400).result("Invalid ID: " + e.getMessage());
        }
    }

    // DTO nội bộ cho yêu cầu tạo cuộc trò chuyện
    public static class CreateConversationRequest {
        public String name;
        public boolean isGroup;
        public int createdBy;
        public List<Integer> participantIds;
    }
}
