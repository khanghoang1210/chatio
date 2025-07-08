package com.khanghoang.repository;

import com.khanghoang.database.DatabaseProvider;
import com.khanghoang.model.Message;
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
            INSERT INTO messages(type, sender_id, receiver_id, group_id, content, timestamp)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, message.getType());
            ps.setString(2, message.getFrom());
            ps.setString(3, message.getTo().isEmpty() ? null : message.getTo());
            ps.setString(4, message.getRoomId().isEmpty() ? null : message.getRoomId());
            ps.setString(5, message.getContent());
            ps.setTimestamp(6, new Timestamp(message.getTimestamp()));

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
