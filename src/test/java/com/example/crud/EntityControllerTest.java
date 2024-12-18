package com.example.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EntityControllerTest {

    private EntityController controller;
    private EntityDAO mockDao;

    @BeforeEach
    void setUp() {
        mockDao = Mockito.mock(EntityDAO.class);
        controller = new EntityController(mockDao);
    }

    @Test
    void testGetAllEntities() {
        List<Entity> mockEntities = Arrays.asList(
                new Entity("1", "Name1", "Description1", null, null),
                new Entity("2", "Name2", "Description2", null, null)
        );

        when(mockDao.readAll()).thenReturn(mockEntities);

        List<Entity> result = controller.getAllEntities();

        assertEquals(2, result.size());
        assertEquals("Name1", result.get(0).getName());

        verify(mockDao, times(1)).readAll();
    }

    @Test
    void testCreateEntity() {
        controller.createEntity("1", "TestName", "TestDescription");

        verify(mockDao, times(1)).create(any(Entity.class));
    }

    @Test
    void testUpdateEntity() {
        controller.updateEntity("1", "UpdatedName", "UpdatedDescription");

        verify(mockDao, times(1)).update(any(Entity.class));
    }

    @Test
    void testDeleteEntity() {
        controller.deleteEntity("1");

        verify(mockDao, times(1)).delete("1");
    }
}
