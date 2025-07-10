package com.khanghoang.server.repository;

import com.khanghoang.server.model.Conversation;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConversationRepostoryImpl implements ConversationRepository {
    private final DataSource dataSource;

    public ConversationRepostoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Conversation conversation) {
        String sql = "INSERT INTO conversations (name, is_group, created_by) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, conversation.getName());
            ps.setBoolean(2, conversation.isGroup());
            ps.setInt(3, conversation.getCreatedBy());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                conversation.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Conversation> getConversationsForUser(String userId) {
        String sql = "SELECT c.* FROM conversations c " +
                "JOIN participants p ON c.id = p.conversation_id " +
                "WHERE p.user_id = ?";

        List<Conversation> conversations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(userId));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                conversations.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversations;
    }

    @Override
    public List<Conversation> getConversationsInGroup(String groupId) {
        String sql = "SELECT * FROM conversations WHERE is_group = true AND id = ?";

        List<Conversation> conversations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(groupId));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                conversations.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversations;
    }

    @Override
    public Conversation getConversationById(String conversationId) {
        String sql = "SELECT * FROM conversations WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(conversationId));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getLatestInsertedId() {
        String sql = "SELECT id FROM conversations ORDER BY id DESC LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Conversation map(ResultSet rs) throws SQLException {
        Conversation conversation = new Conversation();
        conversation.setId(rs.getInt("id"));
        conversation.setName(rs.getString("name"));
        conversation.setGroup(rs.getBoolean("is_group"));
        conversation.setCreatedBy(rs.getInt("created_by"));
        return conversation;
    }
}
