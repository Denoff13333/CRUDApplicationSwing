package com.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntityDAOImpl implements EntityDAO {

    @Override
    public void create(Entity entity) {
        if (entity == null || entity.getName() == null || entity.getName().length() < 3 || entity.getName().length() > 50) {
            throw new RuntimeException("Invalid entity data: Name must be between 3 and 50 characters.");
        }
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO entities (id, name, description, createdAt, updatedAt) " +
                             "VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")) {
            stmt.setString(1, entity.getId());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while inserting entity", e);
        }
    }



    @Override
    public List<Entity> readAll() {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entities.add(new Entity(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public void update(Entity entity) {
        String sql = "UPDATE entities SET name = ?, description = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setString(3, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM entities WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
