package tasks_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    Task task;

    @BeforeEach
    void createTask() {
        task = new Task("Тест", "Тестовый таск", TaskStatus.NEW, 60L);
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

    //Проверяем корректность назначения типа задачи.
    @Test
    void setTaskTypeTesting() {
        task.setTaskType(TaskType.TASK);

        assertEquals(TaskType.TASK, task.taskType, "Типы не совпадают.");
    }

    //Проверяем корректность назначения и получения времени начала выполнения задачи.
    @Test
    void setAndGetStartTimeTesting() {
        task.setStartTime(LocalDateTime.of(2025, 10, 31, 15, 0));
        LocalDateTime dateTimeTest = task.getStartTime();

        assertEquals(LocalDateTime.of(2025, 10, 31, 15, 0), dateTimeTest, "Время не совпадает.");
    }

    //Проверяем корректность назначения и получения длительности выполнения задачи.
    @Test
    void setAndGetDurationTesting() {
        task.setDuration(Duration.ofMinutes(65));
        Duration durationTest = task.getDuration();

        assertEquals(Duration.ofMinutes(65), durationTest, "Длительность не совпадает.");
    }

    //Проверяем корректность получения времени окончания выполнения задачи.
    @Test
    void getEndTimeTesting() {
        LocalDateTime dateTimeTest = task.getEndTime();

        assertNull(dateTimeTest, "Значение не null.");

        Task task1 = new Task("Тест-1", "Тестовый таск-1", TaskStatus.DONE, 40L);
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 15, 0));
        LocalDateTime newDateTimeTest = task1.getEndTime();

        assertEquals(LocalDateTime.of(2025, 10, 31, 15, 40), newDateTimeTest, "Время не совпадает.");
    }

    //Тестируем корректность метода Equals.
    @Test
    void testEquals() {
        Task task1 = new Task("Тест-1", "Тестовый таск-1", TaskStatus.DONE, 30L);
        task1.setTaskID(15);
        Task task2 = new Task("Тест", "Тестовый таск", TaskStatus.NEW, 35L);
        task2.setTaskID(7);

        assertTrue(task.equals(task1));
        assertTrue(task1.equals(task));

        assertFalse(task.equals(task2));
        assertFalse(task2.equals(task));
    }

    //Тестируем корректность метода HashCode.
    @Test
    void testHashCode() {
        Task task1 = new Task("Тест-1", "Тестовый таск-1", TaskStatus.DONE, 35L);
        task1.setTaskID(15);
        Task task2 = new Task("Тест", "Тестовый таск", TaskStatus.NEW, 27L);
        task2.setTaskID(7);

        assertEquals(task, task1);
        assertNotEquals(task, task2);

        assertEquals(task.hashCode(), task1.hashCode());
        assertNotEquals(task.hashCode(), task2.hashCode());
    }

    //toString() не тестируем, так как он используется только для отладки в Main.
}