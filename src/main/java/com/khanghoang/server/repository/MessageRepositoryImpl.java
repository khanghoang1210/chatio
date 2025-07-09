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
    public List<Message> getMessagesForUser(String userId) {
        String sql = """
            SELECT * FROM messages
            WHERE sender_id = ? OR receiver_id = ?
            ORDER BY timestamp DESC
            LIMIT 100
        """;

        List<Message> messages = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                        rs.getLong("id"),
                        rs.getString("type"),
                        rs.getString("sender_id"),
                        rs.getString("receiver_id"),
                        rs.getString("conversation_id"),
                        rs.getString("content"),
                        rs.getTimestamp("timestamp")
                );
                messages.add(msg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<Message> getMessagesInGroup(String groupId) {
            String sql = """
            SELECT * FROM messages
            WHERE group_id = ?
            ORDER BY timestamp DESC
            LIMIT 100
        """;

            List<Message> messages = new ArrayList<>();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, groupId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Message msg = new Message(
                            rs.getLong("id"),
                            rs.getString("type"),
                            rs.getString("sender_id"),
                            rs.getString("receiver_id"),
                            rs.getString("group_id"),
                            rs.getString("content"),
                            rs.getTimestamp("timestamp")
                    );
                    messages.add(msg);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return messages;
        }
}
