package com.khanghoang.server.repository;

import com.khanghoang.server.dto.MessageRes;
import com.khanghoang.server.model.Message;
import com.khanghoang.server.repository.interfaces.MessageRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {
    private final DataSource dataSource;

    public MessageRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Message message) {
        String sql = """
            INSERT INTO messages( sender_id, conversation_id, content)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getConversationId() == -1 ? null : message.getConversationId());
            ps.setString(3, message.getContent());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<MessageRes> getMessagesInConversation(String conversationId) {
            String sql = """
            SELECT m.*, u.username
                     FROM messages m
                     JOIN users u ON m.sender_id = u.id
                     WHERE m.conversation_id = ?
                     ORDER BY m.sent_at DESC
                     LIMIT 100;
                     
        """;

            List<MessageRes> messages = new ArrayList<>();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(conversationId));
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    MessageRes msg = new MessageRes(
                            rs.getLong("id"),
                            rs.getInt("sender_id"),
                            rs.getString("username"),
                            rs.getInt("conversation_id"),
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
