package com.example.crud;

import java.util.List;

public interface EntityDAO {
    void create(Entity entity);
    List<Entity> readAll();
    void update(Entity entity);
    void delete(String id);
}
