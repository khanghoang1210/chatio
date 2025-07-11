package com.khanghoang.server.network.rest.controller;

import com.khanghoang.server.model.Message;
import com.khanghoang.server.service.MessageService;
import com.khanghoang.protocol.MessageFrame;
import io.javalin.http.Context;

import java.util.List;

public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public void handleGetMessagesByConversation(Context ctx) {
        try {
            String conversationId = ctx.pathParam("conversationId");
            List<Message> messages = messageService.getMessagesByConversationId(conversationId);
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(400).result("Invalid conversation ID: " + e.getMessage());
        }
    }

    public void handlePostMessage(Context ctx) {
        try {
            MessageFrame frame = ctx.bodyAsClass(MessageFrame.class);
            messageService.saveMessage(frame);
            ctx.status(201).result("Message saved");
        } catch (Exception e) {
            ctx.status(400).result("Invalid request: " + e.getMessage());
        }
    }
}
