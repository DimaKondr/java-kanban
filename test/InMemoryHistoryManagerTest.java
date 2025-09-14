import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    void createTestingTasks() {
        taskManager = Managers.getDefault();
        historyManager = taskManager.getHistoryManager();
        Task task = new Task("Тест Таск", "Тестовый таск", TaskStatus.NEW);
        EpicTask epicTask = new EpicTask("Тест Эпик", "Тестовый эпик", TaskStatus.NEW);
        SubTask subTask = new SubTask("Тест Сабтаск", "Тестовый сабтаск", TaskStatus.NEW, 2);
        taskManager.addTask(task);
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask);
        subTask.epicTaskID = 2;
        epicTask.addSubTaskID(3);
    }

    //Проверка добавления просмотренной задачи в историю просмотра.
    @Test
    void addToHistoryTesting() {
        Task updatedTask = taskManager.getTaskByID(1);
        EpicTask updatedEpicTask = taskManager.getEpicTaskByID(2);
        SubTask updatedSubTask = taskManager.getSubTaskByID(3);

        //Проверим, что объекты существуют.
        assertNotNull(updatedTask, "Объект не существует!");
        assertNotNull(updatedEpicTask, "Объект не существует!");
        assertNotNull(updatedSubTask, "Объект не существует!");

        updatedTask.title = "ChangedТест Таск";
        updatedTask.description = "ChangedТестовый таск";
        updatedTask.taskStatus = TaskStatus.IN_PROGRESS;
        updatedEpicTask.title = "ChangedТест Эпик";
        updatedEpicTask.description = "ChangedТестовый эпик";
        SubTask otherSubTask = new SubTask("Саб2", "Саб22", TaskStatus.IN_PROGRESS, 2);
        taskManager.addSubTask(otherSubTask);
        updatedEpicTask.clearEpicSubTaskIDList();
        updatedEpicTask.addSubTaskID(4);
        updatedSubTask.title = "ChangedТест Сабтаск";
        updatedSubTask.description = "ChangedТестовый сабтаск";
        updatedSubTask.taskStatus = TaskStatus.IN_PROGRESS;
        EpicTask otherEpicTask = new EpicTask("Эпик2", "Эпик22", TaskStatus.NEW);
        taskManager.addEpicTask(otherEpicTask);
        updatedSubTask.epicTaskID = 5;

        taskManager.updateTask(updatedTask);
        taskManager.updateEpicTask(updatedEpicTask);
        taskManager.updateSubTask(updatedSubTask);

        //Проверяем, что таски совпадают по всем полям.
        assertEquals("ChangedТест Таск", updatedTask.title, "Несовпадение полей!");
        assertEquals("ChangedТестовый таск", updatedTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.taskStatus, "Несовпадение полей!");
        assertEquals(1, updatedTask.taskID, "Несовпадение полей!");

        //Проверяем, что эпики совпадают по всем полям.
        assertEquals("ChangedТест Эпик", updatedEpicTask.title, "Несовпадение полей!");
        assertEquals("ChangedТестовый эпик", updatedEpicTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpicTask.taskStatus, "Несовпадение полей!");
        assertEquals(2, updatedEpicTask.taskID, "Несовпадение полей!");
        assertTrue(updatedEpicTask.getEpicSubTaskIDList().contains(4));
        assertFalse(updatedEpicTask.getEpicSubTaskIDList().contains(3));

        //Проверяем, что сабтаски совпадают по всем полям.
        assertEquals("ChangedТест Сабтаск", updatedSubTask.title, "Несовпадение полей!");
        assertEquals("ChangedТестовый сабтаск", updatedSubTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.IN_PROGRESS, updatedSubTask.taskStatus, "Несовпадение полей!");
        assertEquals(3, updatedSubTask.taskID, "Несовпадение полей!");
        assertEquals(5, updatedSubTask.getEpicTaskID(), "Несовпадение полей!");

        //Проверяем, что сохраненная в истории просмотра задача не изменится, в случае ее обновления в менеджере задач.
        ArrayList<Task> historyList = historyManager.getHistory();
        Task testedTask = historyList.get(0);
        EpicTask testedEpicTask = (EpicTask) historyList.get(1);
        SubTask testedSubTask = (SubTask) historyList.get(2);

        //Поля тасков не изменились.
        assertEquals("Тест Таск", testedTask.title, "Несовпадение полей!");
        assertEquals("Тестовый таск", testedTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedTask.taskStatus, "Несовпадение полей!");
        assertEquals(1, testedTask.taskID, "Несовпадение полей!");

        //Поля эпиков не изменились.
        assertEquals("Тест Эпик", testedEpicTask.title, "Несовпадение полей!");
        assertEquals("Тестовый эпик", testedEpicTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedEpicTask.taskStatus, "Несовпадение полей!");
        assertEquals(2, testedEpicTask.taskID, "Несовпадение полей!");
        assertFalse(testedEpicTask.getEpicSubTaskIDList().contains(4));
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(3));

        //Поля сабтасков не изменились.
        assertEquals("Тест Сабтаск", testedSubTask.title, "Несовпадение полей!");
        assertEquals("Тестовый сабтаск", testedSubTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedSubTask.taskStatus, "Несовпадение полей!");
        assertEquals(3, testedSubTask.taskID, "Несовпадение полей!");
        assertEquals(2, testedSubTask.getEpicTaskID(), "Несовпадение полей!");
    }

    //Проверяем корректность получения списка просмотренных задач.
    @Test
    void getHistoryTesting() {
        Task updatedTask = taskManager.getTaskByID(1);
        EpicTask updatedEpicTask = taskManager.getEpicTaskByID(2);
        SubTask updatedSubTask = taskManager.getSubTaskByID(3);

        //Проверим, что объекты существуют.
        assertNotNull(updatedTask, "Объект не существует!");
        assertNotNull(updatedEpicTask, "Объект не существует!");
        assertNotNull(updatedSubTask, "Объект не существует!");

        ArrayList<Task> otherTaskViewHistory = new ArrayList<>();
        otherTaskViewHistory.add(updatedTask);
        otherTaskViewHistory.add(updatedEpicTask);
        otherTaskViewHistory.add(updatedSubTask);

        //Проверим, что списки совпадают.
        assertEquals(otherTaskViewHistory, historyManager.getHistory(), "Списки не совпадают");
    }

    //Проверяем, что в список просмотренных задач не добавиться более 10 объектов.
    @Test
    void shouldBeNoMoreThan10ElementsInViewHistoryList() {
        Task updatedTask1 = taskManager.getTaskByID(1);
        EpicTask updatedEpicTask2 = taskManager.getEpicTaskByID(2);
        SubTask updatedSubTask3 = taskManager.getSubTaskByID(3);
        Task updatedTask4 = taskManager.getTaskByID(1);
        EpicTask updatedEpicTask5 = taskManager.getEpicTaskByID(2);
        SubTask updatedSubTask6 = taskManager.getSubTaskByID(3);
        Task updatedTask7 = taskManager.getTaskByID(1);
        EpicTask updatedEpicTask8 = taskManager.getEpicTaskByID(2);
        SubTask updatedSubTask9 = taskManager.getSubTaskByID(3);
        Task updatedTask10 = taskManager.getTaskByID(1);
        EpicTask updatedEpicTask11 = taskManager.getEpicTaskByID(2);
        SubTask updatedSubTask12 = taskManager.getSubTaskByID(3);

        ArrayList<Task> history = historyManager.getHistory();
        ArrayList<Task> historyForTest = new ArrayList<>();
        historyForTest.add(updatedSubTask3);
        historyForTest.add(updatedTask4);
        historyForTest.add(updatedEpicTask5);
        historyForTest.add(updatedSubTask6);
        historyForTest.add(updatedTask7);
        historyForTest.add(updatedEpicTask8);
        historyForTest.add(updatedSubTask9);
        historyForTest.add(updatedTask10);
        historyForTest.add(updatedEpicTask11);
        historyForTest.add(updatedSubTask12);

        //Проверяем, что список имеет не более 10 объектов.
        assertEquals(10, history.size(), "Нужно проверить количество элементов");

        //Проверяем, что при превышении количества элементов 10 штук, новые добавляются в конец,
        // а старые удаляются с самого начала.
        assertEquals(historyForTest, history, "Списки не совпадают");
    }

}