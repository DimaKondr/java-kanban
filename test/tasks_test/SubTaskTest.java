package tasks_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.SubTask;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    SubTask subTask;

    @BeforeEach
    void createSubTask() {
        subTask = new SubTask("Тест", "Тестовый сабтаск", TaskStatus.NEW, 15, 20L);
        subTask.setTaskID(22);
    }

    //Проверяем получения ID эпика, к которому относится сабтаск.
    @Test
    void getEpicTaskIDTesting() {
        assertEquals(15, subTask.getEpicTaskID(), "Неверный epicTaskID.");
    }

    //Тестируем корректность метода Equals родительского класса.
    @Test
    void testParentEquals() {
        SubTask subTask1 = new SubTask("Тест-1", "Тестовый сабтаск-1", TaskStatus.DONE, 4, 17L);
        subTask1.setTaskID(22);
        SubTask subTask2 = new SubTask("Тест", "Тестовый сабтаск", TaskStatus.NEW, 15, 21L);
        subTask2.setTaskID(33);

        assertTrue(subTask.equals(subTask1));
        assertTrue(subTask1.equals(subTask));

        assertFalse(subTask.equals(subTask2));
        assertFalse(subTask2.equals(subTask));
    }

    //Тестируем корректность метода HashCode родительского класса.
    @Test
    void testParentHashCode() {
        SubTask subTask1 = new SubTask("Тест-1", "Тестовый сабтаск-1", TaskStatus.DONE, 4, 23L);
        subTask1.setTaskID(22);
        SubTask subTask2 = new SubTask("Тест", "Тестовый сабтаск", TaskStatus.NEW, 15, 31L);
        subTask2.setTaskID(33);

        assertEquals(subTask, subTask1);
        assertNotEquals(subTask, subTask2);

        assertEquals(subTask.hashCode(), subTask1.hashCode());
        assertNotEquals(subTask.hashCode(), subTask2.hashCode());
    }

    //toString() не тестируем, так как он используется только для отладки в Main.
}