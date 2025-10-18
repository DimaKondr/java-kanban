import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager;
    HistoryManager historyManager;
    Task task;
    EpicTask epicTask;
    SubTask subTask;

    @BeforeEach
    void createTestingTasks() {
        taskManager = Managers.getDefault();
        historyManager =  taskManager.getHistoryManager();
        task = new Task("Тест Таск", "Тестовый таск", TaskStatus.NEW, 10L);
        epicTask = new EpicTask("Тест Эпик", "Тестовый эпик", TaskStatus.NEW);
        subTask = new SubTask("Тест Сабтаск", "Тестовый сабтаск", TaskStatus.NEW, 2, 15L);
        taskManager.addTask(task);
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask);
        epicTask.addSubTaskID(3);
    }

    //Проверяем корректность получения списка просмотренных задач.
    @Test
    void getHistoryTesting() {
        Task viewedTask = taskManager.getTaskByID(1);
        EpicTask viewedEpicTask = taskManager.getEpicTaskByID(2);
        SubTask viewedSubTask = taskManager.getSubTaskByID(3);

        //Проверим, что объекты существуют.
        assertNotNull(viewedTask, "Объект не существует!");
        assertNotNull(viewedEpicTask, "Объект не существует!");
        assertNotNull(viewedSubTask, "Объект не существует!");

        List<Task> otherTaskViewHistory = new ArrayList<>();
        otherTaskViewHistory.add(viewedTask);
        otherTaskViewHistory.add(viewedEpicTask);
        otherTaskViewHistory.add(viewedSubTask);

        //Проверим, что списки совпадают.
        assertEquals(otherTaskViewHistory, historyManager.getHistory(), "Списки не совпадают");

        List<Task> tasksViewHistory = historyManager.getHistory();

        //Проверим, что в возвращаемом списке объекты с верными классами и в верном порядке просмотра.
        assertInstanceOf(Task.class, tasksViewHistory.get(0), "Класс не совпадает");
        assertInstanceOf(EpicTask.class, tasksViewHistory.get(1), "Класс не совпадает");
        assertInstanceOf(SubTask.class, tasksViewHistory.get(2), "Класс не совпадает");

        Task testedTask = tasksViewHistory.get(0);
        EpicTask testedEpicTask = (EpicTask) tasksViewHistory.get(1);
        SubTask testedSubTask = (SubTask) tasksViewHistory.get(2);

        testedTask.title = "ИзмененоТаск";
        testedTask.description = "ИзмененоТаск";
        testedEpicTask.title = "ИзмененоЭпик";
        testedEpicTask.description = "ИзмененоЭпик";
        testedSubTask.title = "ИзмененоСабтаск";
        testedSubTask.description = "ИзмененоСабтаск";

        //Проверим, что в возвращаемом списке копии объектов.
        assertEquals("Тест Таск", viewedTask.title, "Несовпадение полей!");
        assertEquals("Тестовый таск", viewedTask.description, "Несовпадение полей!");
        assertEquals("Тест Эпик", viewedEpicTask.title, "Несовпадение полей!");
        assertEquals("Тестовый эпик", viewedEpicTask.description, "Несовпадение полей!");
        assertEquals("Тест Сабтаск", viewedSubTask.title, "Несовпадение полей!");
        assertEquals("Тестовый сабтаск", viewedSubTask.description, "Несовпадение полей!");
    }

    //Проверка добавления просмотренной задачи в историю просмотра.
    @Test
    void addToHistoryTesting() {
        Task viewedTask = taskManager.getTaskByID(1);
        EpicTask viewedEpicTask = taskManager.getEpicTaskByID(2);
        SubTask viewedSubTask = taskManager.getSubTaskByID(3);

        //Проверим, что объекты существуют.
        assertNotNull(viewedTask, "Объект не существует!");
        assertNotNull(viewedEpicTask, "Объект не существует!");
        assertNotNull(viewedSubTask, "Объект не существует!");

        viewedTask.title = "ChangedТест Таск";
        viewedTask.description = "ChangedТестовый таск";
        viewedTask.taskStatus = TaskStatus.IN_PROGRESS;
        viewedEpicTask.title = "ChangedТест Эпик";
        viewedEpicTask.description = "ChangedТестовый эпик";
        SubTask otherSubTask = new SubTask("Саб2", "Саб22", TaskStatus.IN_PROGRESS, 2, 20L);
        taskManager.addSubTask(otherSubTask);
        viewedEpicTask.addSubTaskID(4);
        viewedSubTask.title = "ChangedТест Сабтаск";
        viewedSubTask.description = "ChangedТестовый сабтаск";
        viewedSubTask.taskStatus = TaskStatus.IN_PROGRESS;
        EpicTask otherEpicTask = new EpicTask("Эпик2", "Эпик22", TaskStatus.NEW);
        taskManager.addEpicTask(otherEpicTask);
        viewedSubTask.epicTaskID = 5;

        taskManager.updateTask(viewedTask);
        taskManager.updateEpicTask(viewedEpicTask);
        taskManager.updateSubTask(viewedSubTask);
        List<Task> tasksList = taskManager.getTasksList();
        List<EpicTask> epicTasksList = taskManager.getEpicTasksList();
        List<SubTask> subTasksList = taskManager.getSubTasksList();
        Task viewedTask2 = tasksList.get(0);
        EpicTask viewedEpicTask2 = epicTasksList.get(0);
        SubTask viewedSubTask2 = subTasksList.get(0);

        //Проверяем, что таски совпадают по всем полям.
        assertEquals("ChangedТест Таск", viewedTask2.title, "Несовпадение полей!");
        assertEquals("ChangedТестовый таск", viewedTask2.description, "Несовпадение полей!");
        assertEquals(TaskStatus.IN_PROGRESS, viewedTask2.taskStatus, "Несовпадение полей!");
        assertEquals(1, viewedTask2.taskID, "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(10), viewedTask2.getDuration(), "Длительность не совпадает!");

        //Проверяем, что эпики совпадают по всем полям.
        assertEquals("ChangedТест Эпик", viewedEpicTask2.title, "Несовпадение полей!");
        assertEquals("ChangedТестовый эпик", viewedEpicTask2.description, "Несовпадение полей!");
        assertEquals(TaskStatus.IN_PROGRESS, viewedEpicTask2.taskStatus, "Несовпадение полей!");
        assertEquals(2, viewedEpicTask2.taskID, "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(35), viewedEpicTask2.getDuration(), "Длительность не совпадает!");
        assertTrue(viewedEpicTask2.getEpicSubTaskIDList().contains(4));
        assertTrue(viewedEpicTask2.getEpicSubTaskIDList().contains(3));

        //Проверяем, что сабтаски совпадают по всем полям.
        assertEquals("ChangedТест Сабтаск", viewedSubTask2.title, "Несовпадение полей!");
        assertEquals("ChangedТестовый сабтаск", viewedSubTask2.description, "Несовпадение полей!");
        assertEquals(TaskStatus.IN_PROGRESS, viewedSubTask2.taskStatus, "Несовпадение полей!");
        assertEquals(3, viewedSubTask2.taskID, "Несовпадение полей!");
        assertEquals(5, viewedSubTask2.getEpicTaskID(), "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(15), viewedSubTask2.getDuration(), "Длительность не совпадает!");

        //Проверяем, что сохраненная в истории просмотра задача не изменится, в случае ее обновления в менеджере задач.
        List<Task> historyList = historyManager.getHistory();
        Task testedTask = historyList.get(0);
        EpicTask testedEpicTask = (EpicTask) historyList.get(1);
        SubTask testedSubTask = (SubTask) historyList.get(2);

        //Поля тасков не изменились.
        assertEquals("Тест Таск", testedTask.title, "Несовпадение полей!");
        assertEquals("Тестовый таск", testedTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedTask.taskStatus, "Несовпадение полей!");
        assertEquals(1, testedTask.taskID, "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(10), testedTask.getDuration(), "Длительность не совпадает!");

        //Поля эпиков не изменились.
        assertEquals("Тест Эпик", testedEpicTask.title, "Несовпадение полей!");
        assertEquals("Тестовый эпик", testedEpicTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedEpicTask.taskStatus, "Несовпадение полей!");
        assertEquals(2, testedEpicTask.taskID, "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(15), testedEpicTask.getDuration(), "Длительность не совпадает!");
        assertFalse(testedEpicTask.getEpicSubTaskIDList().contains(4));
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(3));

        //Поля сабтасков не изменились.
        assertEquals("Тест Сабтаск", testedSubTask.title, "Несовпадение полей!");
        assertEquals("Тестовый сабтаск", testedSubTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedSubTask.taskStatus, "Несовпадение полей!");
        assertEquals(3, testedSubTask.taskID, "Несовпадение полей!");
        assertEquals(2, testedSubTask.getEpicTaskID(), "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(15), testedSubTask.getDuration(), "Длительность не совпадает!");
    }

    //Проверяем корректность добавления задачи в конец двусвязного списка,
    //и что при повторном просмотре задачи из истории удаляется ее ранее просмотренная версия,
    //и что порядок оставшихся задач не нарушен.
    @Test
    void linkLastAndRemoveFromHistoryAndRemoveNodeTesting() {
        Task viewedTask = taskManager.getTaskByID(1);
        EpicTask viewedEpicTask = taskManager.getEpicTaskByID(2);
        SubTask viewedSubTask = taskManager.getSubTaskByID(3);

        List<Task> historyList = historyManager.getHistory();

        List<Task> testingList = new ArrayList<>();
        testingList.add(task);
        testingList.add(epicTask);
        testingList.add(subTask);

        //Проверяем, что список существует, что списки совпадают по размеру и порядку элементов.
        assertNotNull(historyList, "Список не существует!");
        assertEquals(testingList, historyList, "Списки не равны!");

        Task lastViewedTask = taskManager.getTaskByID(1);
        List<Task> changedHistoryList = historyManager.getHistory();
        List<Task> changedTestingList = new ArrayList<>();
        changedTestingList.add(0,epicTask);
        changedTestingList.add(1,subTask);
        changedTestingList.add(2,task);

        //Проверяем, что вновь просмотренный элемент добавился в конец истории и был удален его дубликат.
        assertEquals(changedTestingList, changedHistoryList, "Списки не равны!");

        //Удаляем объект из середины истории просмотра.
        historyManager.removeFromHistory(3);

        List<Task> changedHistoryList2 = historyManager.getHistory();
        List<Task> changedTestingList2 = new ArrayList<>();
        changedTestingList2.add(epicTask);
        changedTestingList2.add(task);

        //Проверяем, что корректно работает метод удаления задачи из истории просмотра,
        //и что порядок остальных задач не изменился.
        assertFalse(changedHistoryList2.contains(subTask), "Объект не удален из истории просмотра");
        assertEquals(changedTestingList2, changedHistoryList2, "Списки не равны!");

        //Удаляем "голову" двусвязного списка.
        SubTask lastViewedSubTask = taskManager.getSubTaskByID(3);
        historyManager.removeFromHistory(2);

        List<Task> changedHistoryList3 = historyManager.getHistory();
        List<Task> changedTestingList3 = new ArrayList<>();
        changedTestingList3.add(task);
        changedTestingList3.add(subTask);

        //Проверяем, что удалена "голова" двусвязного списка, и что порядок истории не изменился.
        assertFalse(changedHistoryList3.contains(epicTask), "Объект не удален из истории просмотра");
        assertEquals(changedTestingList3, changedHistoryList3, "Списки не равны!");

        //Удаляем "хвост" двусвязного списка.
        EpicTask lastViewedEpicSubTask = taskManager.getEpicTaskByID(2);
        SubTask lastViewedSubTask2 = taskManager.getSubTaskByID(3);
        historyManager.removeFromHistory(3);

        List<Task> changedHistoryList4 = historyManager.getHistory();
        List<Task> changedTestingList4 = new ArrayList<>();
        changedTestingList4.add(task);
        changedTestingList4.add(epicTask);

        //Проверяем, что удален "хвост" двусвязного списка, и что порядок истории не изменился.
        assertFalse(changedHistoryList4.contains(subTask), "Объект не удален из истории просмотра");
        assertEquals(changedTestingList4, changedHistoryList4, "Списки не равны!");

        //Полностью очищаем двусвязный список.
        historyManager.removeFromHistory(1);
        historyManager.removeFromHistory(2);
        List<Task> changedHistoryList5 = historyManager.getHistory();

        //Проверяем, что двусвязный список пуст.
        assertTrue(changedHistoryList5.isEmpty());
    }

}