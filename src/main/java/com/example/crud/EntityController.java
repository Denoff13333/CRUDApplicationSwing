package com.example.crud;

import java.util.List;

public class EntityController {
    private final EntityDAO dao;

    public EntityController() {
        this.dao = new EntityDAOImpl();
    }

    public EntityController(EntityDAO dao) {
        this.dao = dao;
    }

    public List<Entity> getAllEntities() {
        return dao.readAll();
    }

    public void createEntity(String id, String name, String description) {
        Entity entity = new Entity(id, name, description, null, null);
        dao.create(entity);
    }

    public void updateEntity(String id, String name, String description) {
        Entity entity = new Entity(id, name, description, null, null);
        dao.update(entity);
    }

    public void deleteEntity(String id) {
        dao.delete(id);
    }
}
