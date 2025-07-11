package com.khanghoang.server.repository;

import com.khanghoang.server.model.Message;
import com.khanghoang.protocol.MessageFrame;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository{
    private final DataSource dataSource;

    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(MessageFrame message) {
        String sql = """
            INSERT INTO messages( sender_id, conversation_id, content)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, message.getFrom());
            ps.setString(2, message.getRoomId().isEmpty() ? null : message.getRoomId());
            ps.setString(3, message.getContent());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Message> getMessagesInConversation(String conversationId) {
            String sql = """
            SELECT * FROM messages
            WHERE conversation_id = ?
            ORDER BY timestamp DESC
            LIMIT 100
        """;

            List<Message> messages = new ArrayList<>();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, conversationId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Message msg = new Message(
                            rs.getLong("id"),
                            rs.getString("sender_id"),
                            rs.getString("conversation_id"),
                            rs.getString("content"),
                            rs.getTimestamp("sent_at")
                    );
                    messages.add(msg);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return messages;
        }
}
