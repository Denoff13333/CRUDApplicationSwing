package com.example.crud;

import org.junit.jupiter.api.*;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntityDAOImplTest {

    private static EntityDAO dao;
    private static String testId;

    @BeforeAll
    static void setUp() {
        DatabaseConnection.createTableIfNotExists();
        dao = new EntityDAOImpl();
    }

    @Test
    @Order(1)
    void testCreate() {
        testId = UUID.randomUUID().toString();
        Entity entity = new Entity(testId, "Test Name", "Test Description", null, null);
        dao.create(entity);

        List<Entity> entities = dao.readAll();
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(testId)),
                "Entity should be created in the database.");
    }

    @Test
    @Order(2)
    void testReadAll() {
        List<Entity> entities = dao.readAll();
        assertNotNull(entities, "The list of entities should not be null.");
    }

    @Test
    @Order(3)
    void testUpdate() {
        String updatedName = "Updated Name";
        String updatedDescription = "Updated Description";

        Entity updatedEntity = new Entity(testId, updatedName, updatedDescription, null, null);
        dao.update(updatedEntity);

        List<Entity> entities = dao.readAll();
        Entity entity = entities.stream().filter(e -> e.getId().equals(testId)).findFirst().orElse(null);
        assertNotNull(entity, "Updated entity should exist.");
        assertEquals(updatedName, entity.getName(), "Name should be updated.");
        assertEquals(updatedDescription, entity.getDescription(), "Description should be updated.");
    }

    @Test
    @Order(4)
    void testDelete() {
        dao.delete(testId);

        List<Entity> entities = dao.readAll();
        assertFalse(entities.stream().anyMatch(e -> e.getId().equals(testId)),
                "Entity should be deleted from the database.");
    }

    @Test
    @Order(5)
    void testReadEmptyDatabase() {
        dao.delete(testId); // Удалим все данные
        List<Entity> entities = dao.readAll();
        assertTrue(entities.isEmpty(), "The list should be empty when no data is in the database.");
    }

    @Test
    @Order(6)
    void testCreateWithInvalidData() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            dao.create(new Entity(null, null, null, null, null));
        });
        assertNotNull(exception, "Exception should be thrown when invalid data is provided.");
    }

    @Test
    @Order(7)
    void testUpdateNonExistentEntity() {
        String nonExistentId = UUID.randomUUID().toString();
        Entity entity = new Entity(nonExistentId, "Non Existent", "No Desc", null, null);
        dao.update(entity);

        List<Entity> entities = dao.readAll();
        assertFalse(entities.stream().anyMatch(e -> e.getId().equals(nonExistentId)),
                "Non-existent entity should not be updated.");
    }

    @Test
    @Order(8)
    void testDoubleDelete() {
        dao.delete(testId);
        assertDoesNotThrow(() -> dao.delete(testId), "Deleting a non-existent entity should not throw an exception.");
    }
}
