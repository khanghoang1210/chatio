package com.khanghoang.server.processor;

import com.khanghoang.server.model.Message;

public interface MessageProcessor {
    void handleIncomingMessage(Message message);
}
