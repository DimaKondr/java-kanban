import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTestingTesting() {
        TaskManager taskManager = Managers.getDefault();

        //Проверяем, что объект существует.
        assertNotNull(taskManager);
        //Проверяем класс объекта.
        assertInstanceOf(TaskManager.class, taskManager, "Тип не совпадает!");

        //Проверяем, что проинициализированы пустые HashMap для хранения задач и общий счетчик ID задач.
        assertNotNull(((InMemoryTaskManager) taskManager).tasksList, "Хранилище тасков отсутствует!");
        assertNotNull(((InMemoryTaskManager) taskManager).epicTasksList, "Хранилище эпиков отсутствует!");
        assertNotNull(((InMemoryTaskManager) taskManager).subTasksList, "Хранилище сабтасков отсутствует!");
        assertTrue(((InMemoryTaskManager) taskManager).tasksList.isEmpty(),
                "Хранилище тасков должно быть пустым!");
        assertTrue(((InMemoryTaskManager) taskManager).epicTasksList.isEmpty(),
                "Хранилище эпиков должно быть пустым!");
        assertTrue(((InMemoryTaskManager) taskManager).subTasksList.isEmpty(),
                "Хранилище сабтасков должно быть пустым!");
        assertEquals(0, ((InMemoryTaskManager) taskManager).generalTasksCount,
                "общий счетчик ID задач должен быть равен 0!");
    }

    @Test
    void getDefaultHistoryTesting() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        //Проверяем, что объект существует.
        assertNotNull(historyManager);
        //Проверяем класс объекта.
        assertInstanceOf(HistoryManager.class, historyManager, "Тип не совпадает!");

        //Проверяем, что проинициализирован пустой ArrayList для хранения списка просмотренных задач.
        assertNotNull(historyManager.getHistory(), "Список отсутствует!");
        assertTrue(historyManager.getHistory().isEmpty(), "Список должен быть пустым!");
    }
}