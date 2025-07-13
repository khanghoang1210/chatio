package com.khanghoang.server.repository;

import com.khanghoang.server.repository.interfaces.ParticipantRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantRepositoryImpl implements ParticipantRepository {
    private final DataSource dataSource;

    public ParticipantRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addParticipant(int conversationId, int userId) {
        String sql = "INSERT INTO participants (conversation_id, user_id, joined_at) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, conversationId);
            ps.setInt(2, userId);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getParticipantIds(int conversationId) {
        List<Integer> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM participants WHERE conversation_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, conversationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("user_id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIds;
    }

}
