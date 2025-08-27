import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task;

    @BeforeEach
    void createTask() {
        task = new Task("Тест", "Тестовый таск", TaskStatus.NEW);
        task.setTaskID(15);
    }

    //Проверяем корректность получаемого ID.
    @Test
    void getTaskIDTesting() {
        assertEquals(15, task.getTaskID(), "ID не совпадают.");
    }

    //Проверяем корректность назначения ID.
    @Test
    void setTaskIDTesting() {
        task.setTaskID(11);
        assertEquals(11, task.getTaskID(), "ID не совпадают.");
    }

    //Проверяем корректность получаемого статуса.
    @Test
    void getTaskStatusTesting() {
        assertEquals(TaskStatus.NEW, task.getTaskStatus(), "Статусы не совпадают.");
    }

    //Проверяем корректность назначения статуса.
    @Test
    void setTaskStatusTesting() {
        task.setTaskStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getTaskStatus(), "Статусы не совпадают.");
    }

    //Тестируем корректность метода Equals.
    @Test
    void testEquals() {
        Task task1 = new Task("Тест-1", "Тестовый таск-1", TaskStatus.DONE);
        task1.setTaskID(15);
        Task task2 = new Task("Тест", "Тестовый таск", TaskStatus.NEW);
        task2.setTaskID(7);

        assertTrue(task.equals(task1));
        assertTrue(task1.equals(task));

        assertFalse(task.equals(task2));
        assertFalse(task2.equals(task));
    }

    //Тестируем корректность метода HashCode.
    @Test
    void testHashCode() {
        Task task1 = new Task("Тест-1", "Тестовый таск-1", TaskStatus.DONE);
        task1.setTaskID(15);
        Task task2 = new Task("Тест", "Тестовый таск", TaskStatus.NEW);
        task2.setTaskID(7);

        assertEquals(task, task1);
        assertNotEquals(task, task2);

        assertEquals(task.hashCode(), task1.hashCode());
        assertNotEquals(task.hashCode(), task2.hashCode());
    }

    //toString() не тестируем, так как он используется только для отладки в Main.
}