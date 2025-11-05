package managers_test;

import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    HistoryManager historyManager;
    Task task1;
    Task task2;
    EpicTask epicTask1;
    EpicTask epicTask2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    SubTask subTask4;

    protected abstract T createTaskManager();

    private void initializeTestData() {
        task1 = new Task("Таск Тест 1", "Тестовый таск-1", TaskStatus.NEW, 15L);
        task2 = new Task("Таск Тест 2", "Тестовый таск-2", TaskStatus.NEW, 20L);
        epicTask1 = new EpicTask("Эпик Тест 1", "Тестовый эпик-1", TaskStatus.NEW);
        epicTask2 = new EpicTask("Эпик Тест 2", "Тестовый эпик-2", TaskStatus.NEW);
        subTask1 = new SubTask("Сабтаск Тест 1", "Тестовый сабтаск-1",TaskStatus.NEW, 3, 10L);
        subTask2 = new SubTask("Сабтаск Тест 2", "Тестовый сабтаск-2",TaskStatus.NEW, 3, 20L);
        subTask3 = new SubTask("Сабтаск Тест 3", "Тестовый сабтаск-3",TaskStatus.NEW, 4, 15L);
        subTask4 = new SubTask("Сабтаск Тест 4", "Тестовый сабтаск-4",TaskStatus.NEW, 4, 25L);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpicTask(epicTask1);
        taskManager.addEpicTask(epicTask2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.addSubTask(subTask4);
        task1 = taskManager.getTaskByID(task1.getTaskID());
        task2 = taskManager.getTaskByID(task2.getTaskID());
        epicTask1 = taskManager.getEpicTaskByID(epicTask1.getTaskID());
        epicTask2 = taskManager.getEpicTaskByID(epicTask2.getTaskID());
        subTask1 = taskManager.getSubTaskByID(subTask1.getTaskID());
        subTask2 = taskManager.getSubTaskByID(subTask2.getTaskID());
        subTask3 = taskManager.getSubTaskByID(subTask3.getTaskID());
        subTask4 = taskManager.getSubTaskByID(subTask4.getTaskID());
    }

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
        initializeTestData();
    }

    //Проверим связь эпиков и сабтасков.
    @Test
    void subTaskAndEpicTaskLinkTesting() {
        //Проверяем, что в эпике хранятся верные ID его сабтасков.
        assertEquals(2, epicTask1.getEpicSubTaskIDList().size(), "Размер списка неверный!");
        assertTrue(epicTask1.getEpicSubTaskIDList().contains(subTask1.getTaskID()), "В списке эпика нет ID сабтаска!");
        assertTrue(epicTask1.getEpicSubTaskIDList().contains(subTask2.getTaskID()), "В списке эпика нет ID сабтаска!");

        //Проверяем, что сабтаск хранит в себе верный ID эпика, к которому относится.
        assertEquals(3, subTask1.getEpicTaskID(), "Сабтаск не хранит ID эпика!");
        assertEquals(3, subTask2.getEpicTaskID(), "Сабтаск не хранит ID эпика!");
    }

    //Проверяем метод, возвращающий список тасков.
    @Test
    void getTasksListTesting() {
        ArrayList<Task> tasksArrayList = new ArrayList<>();
        Task otherTask1 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 15L);
        otherTask1.setTaskID(1);
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW, 20L);
        otherTask2.setTaskID(2);
        tasksArrayList.add(otherTask1);
        tasksArrayList.add(otherTask2);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(taskManager.getTasksList(), "Список не существует!");
        assertEquals(tasksArrayList, taskManager.getTasksList(), "Списки не равны!");
        assertEquals(2, taskManager.getTasksList().size(), "Количество задач не совпадает!");
    }

    //Проверяем метод, возвращающий список эпиков.
    @Test
    void getEpicTasksListTesting() {
        ArrayList<EpicTask> epicTasksArrayList = new ArrayList<>();
        EpicTask otherEpicTask1 = new EpicTask("Тест", "Тестовый эпик-1", TaskStatus.NEW);
        otherEpicTask1.setTaskID(3);
        EpicTask otherEpicTask2 = new EpicTask("Тест", "Тестовый эпик-2", TaskStatus.NEW);
        otherEpicTask2.setTaskID(4);
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 10L);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 20L);
        otherSubTask2.setTaskID(6);
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 15L);
        otherSubTask3.setTaskID(7);
        SubTask otherSubTask4 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 25L);
        otherSubTask4.setTaskID(8);
        otherEpicTask1.addSubTaskID(5);
        otherEpicTask1.addSubTaskID(6);
        otherEpicTask2.addSubTaskID(7);
        otherEpicTask2.addSubTaskID(8);
        epicTasksArrayList.add(otherEpicTask1);
        epicTasksArrayList.add(otherEpicTask2);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(taskManager.getEpicTasksList(), "Список не существует!");
        assertEquals(epicTasksArrayList, taskManager.getEpicTasksList(), "Списки не равны!");
        assertEquals(2, taskManager.getEpicTasksList().size(),
                "Количество эпиков не совпадает!");
    }

    //Проверяем метод, возвращающий список сабтасков.
    @Test
    void getSubTasksListTesting() {
        ArrayList<SubTask> subTasksArrayList = new ArrayList<>();
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 10L);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 20L);
        otherSubTask2.setTaskID(6);
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 15L);
        otherSubTask3.setTaskID(7);
        SubTask otherSubTask4 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 25L);
        otherSubTask4.setTaskID(8);
        subTasksArrayList.add(otherSubTask1);
        subTasksArrayList.add(otherSubTask2);
        subTasksArrayList.add(otherSubTask3);
        subTasksArrayList.add(otherSubTask4);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(taskManager.getSubTasksList(), "Список не существует!");
        assertEquals(subTasksArrayList, taskManager.getSubTasksList(), "Списки не равны!");
        assertEquals(4, taskManager.getSubTasksList().size(),
                "Количество сабтасков не совпадает!");
    }

    //Проверяем метод, возвращающий список сабтасков определенного эпика.
    @Test
    void getSubTasksOfEpicTaskTesting() {
        ArrayList<SubTask> subTasksOfEpicTask = new ArrayList<>();
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 10L);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 20L);
        otherSubTask2.setTaskID(6);
        subTasksOfEpicTask.add(otherSubTask1);
        subTasksOfEpicTask.add(otherSubTask2);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(taskManager.getSubTasksOfEpicTask(3), "Список не существует!");
        assertEquals(subTasksOfEpicTask,
                taskManager.getSubTasksOfEpicTask(3), "Списки не равны!");
        assertEquals(2, taskManager.getSubTasksOfEpicTask(3).size(),
                "Количество сабтасков в эпике не совпадает!");
    }

    //Проверяем метод, возвращающий список задач и подзадач всех Epic-задач, отсортированных по времени начала.
    @Test
    void getPrioritizedTasksTesting() {
        //Проверим, что список пуст
        List<Task> testedList = taskManager.getPrioritizedTasks();
        assertTrue(testedList.isEmpty(), "Список не пуст!");

        //Проверим добавление одной задачи
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addTask(task1);
        List<Task> testedList11  = taskManager.getPrioritizedTasks();

        assertEquals(1, testedList11.size(), "Неверное количество элементов в списке!");
        assertEquals(task1, testedList11.get(0), "Задачи не совпадают!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                testedList11.get(0).getStartTime(), "Время начала не совпадает!");

        //Проверим сортировку нескольких задач
        task2.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 0));
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 8, 0));
        subTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 7, 0));
        taskManager.addTask(task2);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        List<Task> testedList22  = taskManager.getPrioritizedTasks();

        assertEquals(4, testedList22.size(), "Неверное количество элементов в списке!");
        assertEquals(subTask2, testedList22.get(0), "Задачи не совпадают!");
        assertEquals(subTask1, testedList22.get(1), "Задачи не совпадают!");
        assertEquals(task1, testedList22.get(2), "Задачи не совпадают!");
        assertEquals(task2, testedList22.get(3), "Задачи не совпадают!");

        assertEquals(LocalDateTime.of(2025, 10, 31, 7, 0),
                testedList22.get(0).getStartTime(), "Время начала не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 8, 0),
                testedList22.get(1).getStartTime(), "Время начала не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                testedList22.get(2).getStartTime(), "Время начала не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 10, 0),
                testedList22.get(3).getStartTime(), "Время начала не совпадает!");

        //Проверим, что в список не попадут задачи без установленного времени начала.
        Task taskWithoutStartTime = new Task("NewТаск", "WithoutStartTime", TaskStatus.NEW, 34L);
        taskManager.addTask(taskWithoutStartTime);
        SubTask subTaskWithoutStartTime = new SubTask("NewСабТаск", "WithoutStartTime_sub", TaskStatus.NEW, 4, 19L);
        taskManager.addSubTask(subTaskWithoutStartTime);
        List<Task> testedList33  = taskManager.getPrioritizedTasks();

        assertEquals(4, testedList33.size(), "Неверное количество элементов в списке!");
        assertFalse(testedList33.contains(taskWithoutStartTime));
        assertFalse(testedList33.contains(subTaskWithoutStartTime));
        assertEquals(LocalDateTime.of(2025, 10, 31, 7, 0),
                testedList22.get(0).getStartTime(), "Время начала не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 10, 0),
                testedList22.get(3).getStartTime(), "Время начала не совпадает!");

        //Проверим добавление новой задачи с временем начала, как у задачи уже находящейся в отсортированном списке.
        SubTask subTaskWithDuplicateStartTime = new SubTask("NewСабТаск11", "WithDuplicateStartTime_sub",
                TaskStatus.NEW, 4, 49L);
        subTaskWithDuplicateStartTime.setStartTime(LocalDateTime.of(2025, 10, 31, 7, 0));
        taskManager.addSubTask(subTaskWithDuplicateStartTime);
        List<Task> testedList44  = taskManager.getPrioritizedTasks();

        assertEquals(4, testedList44.size(), "Неверное количество элементов в списке!");
        assertFalse(testedList44.contains(subTaskWithDuplicateStartTime));
    }

    //Проверяем метод, полностью очищающий список тасков.
    @Test
    void clearTasksListsTesting() {
        taskManager.clearTasksLists();
        assertTrue(taskManager.getTasksList().isEmpty(), "Список не пуст!");
    }

    //Проверяем метод, полностью очищающий список эпиков, включая список сабтасков.
    @Test
    void clearEpicTasksListsTesting() {
        taskManager.clearEpicTasksLists();

        assertTrue(taskManager.getEpicTasksList().isEmpty(), "Список эпиков не пуст!");
        assertTrue(taskManager.getSubTasksList().isEmpty(), "Список сабтасков не пуст!");
    }

    //Проверяем метод, полностью очищающий список сабтасков, а также очищающий список ID сабтасков всех эпиков.
    @Test
    void clearSubTasksListsTesting() {
        taskManager.clearSubTasksLists();

        assertTrue(taskManager.getSubTasksList().isEmpty(), "Список не пуст!");

        for (EpicTask epicTask : taskManager.getEpicTasksList()) {
            epicTask.clearEpicSubTaskIDList();
            taskManager.updateEpicTask(epicTask);
            assertTrue(taskManager.getEpicTaskByID(epicTask.getTaskID())
                    .getEpicSubTaskIDList().isEmpty(), "Список сабтасков Эпика не пуст!");
        }
    }

    //Проверяем метод для получения таски по ID.
    @Test
    void getTaskByIDTesting() {
        Task testedTask = taskManager.getTaskByID(2);

        //Проверяем, что объект существует.
        assertNotNull(testedTask, "Таск не найден!");

        //Проверяем, что по ID получен нужный таск, с верными заголовком, описанием, статусом и ID.
        assertEquals("Таск Тест 2", testedTask.title, "Заголовки не совпадают!");
        assertEquals("Тестовый таск-2", testedTask.description, "Описания не совпадают!");
        assertEquals(TaskStatus.NEW, testedTask.taskStatus, "Статусы не совпадают!");
        assertEquals(2, testedTask.taskID, "ID не совпадают!");
    }

    //Проверяем метод для получения эпика по ID.
    @Test
    void getEpicTaskByIDTesting() {
        EpicTask testedEpicTask = taskManager.getEpicTaskByID(4);

        //Проверяем, что объект существует.
        assertNotNull(testedEpicTask, "Эпик не найден!");

        //Проверяем, что по ID получен нужный эпик, с верными заголовком, описанием, статусом и ID.
        assertEquals("Эпик Тест 2", testedEpicTask.title, "Заголовки не совпадают!");
        assertEquals("Тестовый эпик-2", testedEpicTask.description, "Описания не совпадают!");
        assertEquals(TaskStatus.NEW, testedEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals(4, testedEpicTask.taskID, "ID не совпадают!");

        //Проверяем, что по ID получен нужный эпик, с верным списком сабтасков.
        ArrayList<Integer> epicSubTaskIDList = new ArrayList<>();
        epicSubTaskIDList.add(7);
        epicSubTaskIDList.add(8);
        assertEquals(epicSubTaskIDList, testedEpicTask.getEpicSubTaskIDList(), "Списки с ID сабтасков не совпадают!");
    }

    //Проверяем метод для получения сабтаски по ID.
    @Test
    void getSubTaskByIDTesting() {
        SubTask testedSubTask = taskManager.getSubTaskByID(7);

        //Проверяем, что объект существует.
        assertNotNull(testedSubTask, "Сабтаск не найден!");

        //Проверяем, что по ID получен нужный сабтаск, с верными заголовком, описанием, статусом и ID,
        //а также, что в этом же сабтаске верно сохранен номер ID эпика, к которому относится сабтаск.
        assertEquals("Сабтаск Тест 3", testedSubTask.title, "Заголовки не совпадают!");
        assertEquals("Тестовый сабтаск-3", testedSubTask.description, "Описания не совпадают!");
        assertEquals(TaskStatus.NEW, testedSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals(7, testedSubTask.taskID, "ID не совпадают!");
        assertEquals(4, testedSubTask.getEpicTaskID(), "ID эпика не совпадают!");
    }

    //Проверяем метод для добавления новой таски.
    @Test
    void addTaskTesting() {
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW, 15L);
        otherTask2.setTaskID(9);
        Task newTask = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW, 20L);
        taskManager.addTask(newTask);

        //Проверяем, что добавленный таск существует.
        assertNotNull(taskManager.getTaskByID(9), "Таск не найден!");
        //Проверяем, что таску присвоен ожидаемый ID, и что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherTask2, taskManager.getTaskByID(9), "Таски не равны!");

        Task otherNewTask = new Task("Это!%тестовая@@$%??таска_sldkfjalewf",
                "&%(#)Тестовый><><sgkwe^^*#)_>>таск-2_sdfjsdfgn", TaskStatus.IN_PROGRESS, 17L);
        taskManager.addTask(otherNewTask);

        Task testedTask = taskManager.getTaskByID(10);

        //Проверяем, что сохранился таск класса tasks.Task.
        assertInstanceOf(Task.class, testedTask, "Объект должен быть класса tasks.Task");

        //Проверяем, что при добавлении таска корректно сохраняются заголовок, описание, статус и ID.
        assertEquals("Это!%тестовая@@$%??таска_sldkfjalewf", testedTask.title,
                "Заголовки не совпадают!");
        assertEquals("&%(#)Тестовый><><sgkwe^^*#)_>>таск-2_sdfjsdfgn", testedTask.description,
                "Описания не совпадают!");
        assertEquals(TaskStatus.IN_PROGRESS, testedTask.taskStatus, "Статусы не совпадают!");
        assertEquals(10, testedTask.taskID, "ID не совпадают!");
    }

    //Проверяем метод для добавления нового эпика.
    @Test
    void addEpicTaskTesting() {
        EpicTask otherEpicTask2 = new EpicTask("Тест", "Тестовый эпик-2", TaskStatus.NEW);
        otherEpicTask2.setTaskID(9);
        EpicTask newEpicTask = new EpicTask("Тест", "Тестовый эпик-2", TaskStatus.NEW);
        taskManager.addEpicTask(newEpicTask);

        //Проверяем, что добавленный таск существует.
        assertNotNull(taskManager.getEpicTaskByID(9), "Эпик не найден!");
        //Проверяем, что таску присвоен ожидаемый ID, и что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherEpicTask2, taskManager.getEpicTaskByID(9), "Эпики не равны!");

        EpicTask otherNewEpicTask = new EpicTask("Это@$^тестовый@@$%??эпик_;lwekpqf<>?",
                "&%(#)Тестовый~!~dfge><><sgkwe^^*#)_>>эпик-2_qr31qf", TaskStatus.DONE);
        taskManager.addEpicTask(otherNewEpicTask);

        EpicTask testedEpicTask = taskManager.getEpicTaskByID(10);

        //Проверяем, что сохранился эпик класса tasks.EpicTask.
        assertInstanceOf(EpicTask.class, testedEpicTask, "Объект должен быть класса tasks.EpicTask");

        //Проверяем, что при добавлении эпика корректно сохраняются заголовок, описание, статус и ID,
        //а также, что имеется пустой лист для сохранения ID будущих сабтасков.
        assertEquals("Это@$^тестовый@@$%??эпик_;lwekpqf<>?", testedEpicTask.title,
                "Заголовки не совпадают!");
        assertEquals("&%(#)Тестовый~!~dfge><><sgkwe^^*#)_>>эпик-2_qr31qf", testedEpicTask.description,
                "Описания не совпадают!");
        assertEquals(TaskStatus.DONE, testedEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals(10, testedEpicTask.taskID, "ID не совпадают!");
        assertTrue(testedEpicTask.getEpicSubTaskIDList().isEmpty(), "Список ID сабтасков не пустой!");
    }

    //Проверяем метод для добавления новой сабтаски.
    @Test
    void addSubTaskTesting() {
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 15L);
        otherSubTask3.setTaskID(9);
        SubTask newSubTask = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 25L);
        taskManager.addSubTask(newSubTask);

        //Проверяем, что добавленный таск существует.
        assertNotNull(taskManager.getSubTaskByID(9), "Сабтаск не найден!");
        //Проверяем, что таску присвоен ожидаемый ID, и что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherSubTask3, taskManager.getSubTaskByID(9), "Сабтаски не равны!");

        SubTask subTaskWithEpicNumber = new SubTask("Тест-1", "Тестовый сабтаск-12",
                TaskStatus.NEW, 10, 19L);
        taskManager.addSubTask(subTaskWithEpicNumber);

        //Проверяем, что сабтаску присвоен ожидаемый ID
        assertEquals(10, subTaskWithEpicNumber.getTaskID(), "Присвоен неверный ID");

        //Проверяем, что сабтаск с определенным ID не будет внесен в спискок сабтасков менеджера,
        //при условии, что ID этого же сабтаска прописан как ID эпика, к которому относится сабтаск.
        try {
            taskManager.getSubTaskByID(subTaskWithEpicNumber.getTaskID());
        } catch (NotFoundException e) {
            assertEquals("Не существует подзадачи с указанным ID: "
                    + subTaskWithEpicNumber.getTaskID(), e.getMessage());
        }

        EpicTask otherNewEpicTask = new EpicTask("Test", "Epic test", TaskStatus.NEW);
        taskManager.addEpicTask(otherNewEpicTask);
        SubTask otherNewSubTask = new SubTask("Это)_*@#$%@@тестовый1^$#1fgh@vsсабтаск_;dfg432p23%^&qf<>>,>?",
                ")#&*%@?>Тестовый~!~dfge><<sgk$&#w^*#)_>>subtask-3%@%_qr1qf", TaskStatus.DONE, 10, 10L);
        taskManager.addSubTask(otherNewSubTask);

        EpicTask testedEpicTask = taskManager.getEpicTaskByID(10);
        SubTask testedSubTask = taskManager.getSubTaskByID(11);

        //Проверяем, что сохранился эпик класса tasks.EpicTask.
        assertInstanceOf(SubTask.class, testedSubTask, "Объект должен быть класса tasks.SubTask");

        //Проверяем, что при добавлении сабтаска корректно сохраняются заголовок, описание, статус и ID,
        //а также, что в этом же сабтаске верно сохранен номер ID эпика, к которому относится сабтаск.
        assertEquals("Это)_*@#$%@@тестовый1^$#1fgh@vsсабтаск_;dfg432p23%^&qf<>>,>?", testedSubTask.title,
                "Заголовки не совпадают!");
        assertEquals(")#&*%@?>Тестовый~!~dfge><<sgk$&#w^*#)_>>subtask-3%@%_qr1qf", testedSubTask.description,
                "Описания не совпадают!");
        assertEquals(TaskStatus.DONE, testedSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals(11, testedSubTask.taskID, "ID не совпадают!");
        assertEquals(testedEpicTask.getTaskID(), testedSubTask.getEpicTaskID(), "ID эпика не совпадают!");

        //Проверяем, что в список ID сабтасков нужного эпика добавился верный ID сабтаска.
        int subTaskID = testedSubTask.getTaskID();
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(subTaskID));
    }

    //Проверяем метод для обновления таски.
    @Test
    void updateTaskTesting() {
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW, 20L);
        otherTask2.setTaskID(2);
        Task updatedTask = new Task("$#^gfdjgSDGдалпа#@^#$@в", "$*&sgjks2#@(*lkжывдп2#%апопро@",
                TaskStatus.IN_PROGRESS, 20L);
        updatedTask.setTaskID(2);
        taskManager.updateTask(updatedTask);

        //Проверяем, что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherTask2, taskManager.getTaskByID(2), "Таски не равны!");

        Task testedTask = taskManager.getTaskByID(2);

        //Проверяем, что при обновлении таска корректно сохраняются заголовок, описание, статус и ID.
        assertEquals("$#^gfdjgSDGдалпа#@^#$@в", testedTask.title,
                "Заголовки не совпадают!");
        assertEquals("$*&sgjks2#@(*lkжывдп2#%апопро@", testedTask.description,
                "Описания не совпадают!");
        assertEquals(TaskStatus.IN_PROGRESS, testedTask.taskStatus, "Статусы не совпадают!");
        assertEquals(2, testedTask.taskID, "ID не совпадают!");
    }

    //Проверяем метод для обновления эпика.
    @Test
    void updateEpicTaskTesting() {
        EpicTask otherEpicTask2 = new EpicTask("Тест", "Тестовый эпик-2", TaskStatus.NEW);
        otherEpicTask2.setTaskID(4);
        otherEpicTask2.addSubTaskID(7);
        otherEpicTask2.addSubTaskID(8);

        EpicTask updatedEpicTask = new EpicTask("@#%@%jkdfns23", "@#%kmgl#()%водл#52", TaskStatus.NEW);
        updatedEpicTask.setTaskID(4);
        updatedEpicTask.addSubTaskID(7);
        updatedEpicTask.addSubTaskID(8);
        taskManager.updateEpicTask(updatedEpicTask);

        //Проверяем, что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherEpicTask2, taskManager.getEpicTaskByID(4), "Эпики не равны!");

        EpicTask testedEpicTask = taskManager.getEpicTaskByID(4);

        //Проверяем, что у старого и обновленного эпика совпадает список сабтасков.
        assertEquals(otherEpicTask2.getEpicSubTaskIDList(), testedEpicTask.getEpicSubTaskIDList(),
                "Списки с ID сабтасков не совпадают!");

        //Проверяем, что при обновлении эпика корректно сохраняются заголовок, описание, статус и ID.
        assertEquals("@#%@%jkdfns23", testedEpicTask.title, "Заголовки не совпадают!");
        assertEquals("@#%kmgl#()%водл#52", testedEpicTask.description, "Описания не совпадают!");
        assertEquals(TaskStatus.NEW, testedEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals(4, testedEpicTask.taskID, "ID не совпадают!");
    }

    //Проверяем метод для обновления сабтаски.
    @Test
    void updateSubTaskTesting() {
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4, 15L);
        otherSubTask3.setTaskID(8);
        SubTask updatedSubTask = new SubTask("@()sldnыжве34#$@_)", "_$*^gk;g@#2;l_ываорш$#",
                TaskStatus.DONE, 4, 15L);
        updatedSubTask.setTaskID(8);
        taskManager.updateSubTask(updatedSubTask);

        //Проверяем, что задачи равны по ID.
        assertEquals(otherSubTask3, taskManager.getSubTaskByID(8), "Сабтаски не равны!");

        SubTask otherSubTask = taskManager.getSubTaskByID(8);

        //Проверяем, что номер эпика, к которому относится сабтаск, не изменился.
        assertEquals(otherSubTask3.getEpicTaskID(), otherSubTask.getEpicTaskID(),
                "Сабтаски принадлежат разным эпикам!");

        SubTask otherUpdatedSubTask = new SubTask("Тест-Q", "Тестовый сабтаск-18",
                TaskStatus.NEW, 8,15L);
        otherUpdatedSubTask.setTaskID(8);
        taskManager.updateSubTask(otherUpdatedSubTask);

        SubTask someSubTask = taskManager.getSubTaskByID(8);

        //Проверяем, что ID сабтаска не сохраняется в поле с номером эпика, к которому относится сабтаск.
        assertEquals(4, someSubTask.getEpicTaskID(), "Сабтаск сохранил себя в номер своего эпика!");
        //Проверяем, что получили сабтаск именно с ID 8.
        assertEquals(8, someSubTask.getTaskID(), "ID не совпадают!");


        SubTask newUpdatedSubTask = new SubTask("Тест-DJ", "Тестовый сабтаск-34",
                TaskStatus.NEW, 15, 15L);
        newUpdatedSubTask.setTaskID(8);
        taskManager.updateSubTask(newUpdatedSubTask);

        SubTask someSubTask2 = taskManager.getSubTaskByID(8);

        //Проверяем, что в списке эпиков нет эпика с ID 15.
        try {
            taskManager.getEpicTaskByID(15);
        } catch (NotFoundException e) {
            assertEquals("Не существует Epic-задачи с указанным ID: " + 15, e.getMessage());
        }

        //Проверяем, что сабтаск не сохраняется в эпике, которого нет в списке.
        assertEquals(4, someSubTask2.getEpicTaskID(), "Сабтаск сохранил себя в несуществующий эпик!");

        EpicTask testedEpicTask = taskManager.getEpicTaskByID(4);
        SubTask testedSubTask = taskManager.getSubTaskByID(8);

        //Проверяем, что при обновлении сабтаска корректно сохраняются заголовок, описание, статус и ID,
        //а также, что в этом же сабтаске верно сохранен номер ID эпика, к которому относится сабтаск.
        assertEquals("@()sldnыжве34#$@_)", testedSubTask.title, "Заголовки не совпадают!");
        assertEquals("_$*^gk;g@#2;l_ываорш$#", testedSubTask.description, "Описания не совпадают!");
        assertEquals(TaskStatus.DONE, testedSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals(8, testedSubTask.taskID, "ID не совпадают!");
        assertEquals(testedEpicTask.getTaskID(), testedSubTask.getEpicTaskID(), "ID эпика не совпадают!");

        //Проверяем, что в списке ID сабтасков нужного эпика сохранился верный ID сабтаска.
        int subTaskID = testedSubTask.getTaskID();
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(subTaskID));
    }

    //Проверяем метод для удаления таски по ID.
    @Test
    void removeTaskByIDTesting() {
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW, 10L);
        otherTask2.setTaskID(2);

        taskManager.removeTaskByID(2);

        assertFalse(taskManager.getTasksList().contains(otherTask2), "Таск не удален!");
    }

    //Проверяем метод для удаления эпика по ID, включая его сабтаски.
    @Test
    void removeEpicTaskByIDTesting() {
        EpicTask otherEpicTask1 = new EpicTask("Тест", "Тестовый эпик-1", TaskStatus.NEW);
        otherEpicTask1.setTaskID(3);
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 10L);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 15L);
        otherSubTask2.setTaskID(6);
        otherEpicTask1.addSubTaskID(5);
        otherEpicTask1.addSubTaskID(6);

        taskManager.removeEpicTaskByID(3);

        assertFalse(taskManager.getEpicTasksList().contains(otherEpicTask1), "Эпик не удален!");
        assertFalse(taskManager.getSubTasksList().contains(otherSubTask1), "Сабтаск не удален!");
        assertFalse(taskManager.getSubTasksList().contains(otherSubTask2), "Сабтаск не удален!");
    }

    //Проверяем метод для удаления сабтаски по ID, в том числе удаления ID сабтаска из списка ID сабтасков его эпика.
    @Test
    void removeSubTaskByIDTesting() {
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3, 10L);
        otherSubTask2.setTaskID(6);
        SubTask subTask = taskManager.getSubTaskByID(6);

        taskManager.removeSubTaskByID(6);
        EpicTask epicTask = taskManager.getEpicTaskByID(subTask.getEpicTaskID());

        assertFalse(taskManager.getSubTasksList().contains(otherSubTask2), "Сабтаск не удален!");
        assertFalse(epicTask.getEpicSubTaskIDList().contains(otherSubTask2.getTaskID()), "Сабтаск не удален!");
    }

    //Проверяем метод для определения статуса эпика на основе статуса его сабтасков.
    @Test
    void chooseEpicTaskStatusTesting() {
        EpicTask epicTaskTest1 = taskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус всех сабтасков NEW, имеет статус NEW.
        assertEquals(TaskStatus.NEW, epicTaskTest1.getTaskStatus(), "Статус не совпадает!");

        SubTask subTaskTest1 = new SubTask("СабтаскNewТест", "Тестовый сабтаскNew",
                TaskStatus.IN_PROGRESS, 4, 10L);
        taskManager.addSubTask(subTaskTest1);
        EpicTask epicTaskTest2 = taskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус хотя бы одной сабтаски IN_PROGRESS, имеет статус IN_PROGRESS.
        assertEquals(TaskStatus.IN_PROGRESS, epicTaskTest2.getTaskStatus(), "Статус не совпадает!");

        subTaskTest1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTaskTest1);
        epicTaskTest2 = taskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус сабтасков NEW или DONE, имеет статус IN_PROGRESS.
        assertEquals(TaskStatus.IN_PROGRESS, epicTaskTest2.getTaskStatus(), "Статус не совпадает!");

        SubTask subTaskTest2 = taskManager.getSubTaskByID(7);
        subTaskTest2.setTaskStatus(TaskStatus.DONE);
        SubTask subTaskTest3 = taskManager.getSubTaskByID(8);
        subTaskTest3.setTaskStatus(TaskStatus.DONE);
        SubTask subTaskTest4 = taskManager.getSubTaskByID(9);
        subTaskTest4.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTaskTest2);
        taskManager.updateSubTask(subTaskTest3);
        taskManager.updateSubTask(subTaskTest4);
        EpicTask epicTaskTest3 = taskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус всех сабтасков DONE, имеет статус DONE.
        assertEquals(TaskStatus.DONE, epicTaskTest3.getTaskStatus(), "Статус не совпадает!");

        EpicTask epicTaskTest4 = taskManager.getEpicTaskByID(4);
        epicTaskTest4.clearEpicSubTaskIDList();
        taskManager.chooseEpicTaskStatus(epicTaskTest4);

        //Проверяем, что эпик, у которого список ID сабтасков пуст, имеет статус NEW.
        assertEquals(TaskStatus.NEW, epicTaskTest4.getTaskStatus(), "Статус не совпадает!");
    }

    //Проверяем метод для расчета длительности, времени начала и окончания эпика на основе его сабтасков.
    @Test
    void calculateEpicTaskDurationTesting() {
        EpicTask epicDurationTest1 = new EpicTask("Epic1", "Epic1_description", TaskStatus.NEW);
        taskManager.addEpicTask(epicDurationTest1);

        //Проверяем пустую Epic-задачу
        EpicTask testEpic = taskManager.getEpicTaskByID(epicDurationTest1.getTaskID());
        taskManager.calculateEpicTaskDuration(testEpic);

        assertEquals(Duration.ZERO, testEpic.getDuration());
        assertNull(testEpic.getStartTime());
        assertNull(testEpic.getEndTime());

        //Проверяем Epic-задачу с одной подзадачей
        SubTask subTask1 = new SubTask("Sub1", "Sub1_des", TaskStatus.NEW, epicDurationTest1.getTaskID(), 15L);
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 15));
        taskManager.addSubTask(subTask1);

        EpicTask testEpic11 = taskManager.getEpicTaskByID(epicDurationTest1.getTaskID());
        taskManager.calculateEpicTaskDuration(testEpic11);

        assertEquals(Duration.ofMinutes(15), testEpic11.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 10, 15), testEpic11.getStartTime(), "Время начала не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 10, 30), testEpic11.getEndTime(), "Время окончания не совпадает!");

        //Проверяем Epic-задачу с несколькими подзадачами
        SubTask subTask2 = new SubTask("Sub2", "Sub2_des", TaskStatus.NEW, epicDurationTest1.getTaskID(), 35L);
        subTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 50));
        taskManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Sub3", "Sub3_des", TaskStatus.NEW, epicDurationTest1.getTaskID(), 60L);
        subTask3.setStartTime(LocalDateTime.of(2025, 10, 31, 11, 40));
        taskManager.addSubTask(subTask3);

        SubTask subTask4 = new SubTask("Sub4", "Sub4_des", TaskStatus.NEW, epicDurationTest1.getTaskID(), 20L);
        subTask4.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 40));
        taskManager.addSubTask(subTask4);

        EpicTask testEpic22 = taskManager.getEpicTaskByID(epicDurationTest1.getTaskID());
        taskManager.calculateEpicTaskDuration(testEpic22);

        assertEquals(Duration.ofMinutes(130), testEpic22.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 40), testEpic22.getStartTime(), "Время начала не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 12, 40), testEpic22.getEndTime(), "Время окончания не совпадает!");
    }

    //Проверяем метод для проверки пересечения по времени новой задачи с уже имеющимися задачами.
    @Test
    void isOverlapWithCurrentTasks() {
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 15));
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 0));
        subTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 30));
        taskManager.updateTask(task1);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);

        task1 = taskManager.getTaskByID(task1.getTaskID());
        epicTask1 = taskManager.getEpicTaskByID(epicTask1.getTaskID());
        subTask1 = taskManager.getSubTaskByID(subTask1.getTaskID());
        subTask2 = taskManager.getSubTaskByID(subTask2.getTaskID());

        //Проверяем, что метод корректно отрабатывает задачи без перекрытия и добавляет в список.
        Task otherTask1 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 20L);
        otherTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 35));
        assertFalse(taskManager.isOverlapWithCurrentTasks(otherTask1), "Задачи не должны пересекаться!");
        taskManager.addTask(otherTask1);
        assertNotNull(taskManager.getTaskByID(otherTask1.getTaskID()), "Задача не добавлена в общий список!");
        assertTrue(taskManager.getPrioritizedTasks().contains(otherTask1),
                "Задача не добавлена в список сортировки по времени!");

        //Проверяем частичное перекрытие начала новой задачи с существующей задачей.
        Task otherTask2 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 30L);
        otherTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 40));
        assertTrue(taskManager.isOverlapWithCurrentTasks(otherTask2), "Задачи должны пересекаться!");
        taskManager.addTask(otherTask2);

        try {
            taskManager.getTaskByID(otherTask2.getTaskID());
        } catch (NotFoundException e) {
            assertEquals("Не существует задачи с указанным ID: " + otherTask2.getTaskID(), e.getMessage());
        }

        assertFalse(taskManager.getPrioritizedTasks().contains(otherTask2),
                "Задача не должна быть добавлена в список сортировки по времени!");

        //Проверяем частичное перекрытие конца новой задачи с существующей задачей.
        Task otherTask3 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 35L);
        otherTask3.setStartTime(LocalDateTime.of(2025, 10, 31, 8, 50));
        assertTrue(taskManager.isOverlapWithCurrentTasks(otherTask3), "Задачи должны пересекаться!");
        taskManager.addTask(otherTask3);

        try {
            taskManager.getTaskByID(otherTask3.getTaskID());
        } catch (NotFoundException e) {
            assertEquals("Не существует задачи с указанным ID: " + otherTask3.getTaskID(), e.getMessage());
        }

        assertFalse(taskManager.getPrioritizedTasks().contains(otherTask3),
                "Задача не должна быть добавлена в список сортировки по времени!");

        //Проверяем случай когда новая задача полностью перекрывает существующую.
        Task otherTask4 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 35L);
        otherTask4.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 50));
        assertTrue(taskManager.isOverlapWithCurrentTasks(otherTask4), "Задачи должны пересекаться!");
        taskManager.addTask(otherTask4);

        try {
            taskManager.getTaskByID(otherTask4.getTaskID());
        } catch (NotFoundException e) {
            assertEquals("Не существует задачи с указанным ID: " + otherTask4.getTaskID(), e.getMessage());
        }

        assertFalse(taskManager.getPrioritizedTasks().contains(otherTask4),
                "Задача не должна быть добавлена в список сортировки по времени!");

        //Проверяем случай когда новая задача по времени полностью внутри существующей.
        Task otherTask5 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 10L);
        otherTask5.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 35));
        assertTrue(taskManager.isOverlapWithCurrentTasks(otherTask5), "Задачи должны пересекаться!");
        taskManager.addTask(otherTask5);

        try {
            taskManager.getTaskByID(otherTask5.getTaskID());
        } catch (NotFoundException e) {
            assertEquals("Не существует задачи с указанным ID: " + otherTask5.getTaskID(), e.getMessage());
        }

        assertFalse(taskManager.getPrioritizedTasks().contains(otherTask5),
                "Задача не должна быть добавлена в список сортировки по времени!");

        //Проверяем случай совпадения времени начала новой задачи со временем окончания имеющейся.
        Task otherTask6 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 5L);
        otherTask6.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 30));
        assertFalse(taskManager.isOverlapWithCurrentTasks(otherTask6), "Задачи не должны пересекаться!");
        taskManager.addTask(otherTask6);
        assertNotNull(taskManager.getTaskByID(otherTask6.getTaskID()), "Задача не добавлена в общий список!");
        assertTrue(taskManager.getPrioritizedTasks().contains(otherTask6),
                "Задача не добавлена в список сортировки по времени!");

        //Проверяем случай совпадения времени окончания новой задачи со временем начала имеющейся.
        Task otherTask7 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW, 15L);
        otherTask7.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 10));
        assertFalse(taskManager.isOverlapWithCurrentTasks(otherTask7), "Задачи не должны пересекаться!");
        taskManager.addTask(otherTask7);
        assertNotNull(taskManager.getTaskByID(otherTask7.getTaskID()), "Задача не добавлена в общий список!");
        assertTrue(taskManager.getPrioritizedTasks().contains(otherTask7),
                "Задача не добавлена в список сортировки по времени!");
    }

    //Проверяем метод для генерации ID всех тасков.
    @Test
    void generateTaskIDTesting() {
        Task task1 = new Task("Тест", "Тестовый таск-11", TaskStatus.NEW, 20L);
        Task task2 = new Task("Тест", "Тестовый таск-12", TaskStatus.NEW, 20L);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        //Проверяем, что получили ожидаемый ID.
        assertEquals(10, task2.getTaskID(), "Генерация ID задач не работает!");

        EpicTask epicTask15 = new EpicTask("Тест", "Тестовый эпик-15", TaskStatus.NEW);
        epicTask15.setTaskID(15);
        taskManager.addEpicTask(epicTask15);

        //Проверим, что метод генерации переназначил верный ID.
        assertEquals(11, epicTask15.getTaskID(), "Генерация ID задач не работает!");

        taskManager.removeEpicTaskByID(11);
        taskManager.removeTaskByID(9);
        taskManager.removeTaskByID(10);
        taskManager.removeSubTaskByID(5);
        taskManager.removeSubTaskByID(6);

        SubTask subTask5 = new SubTask("Тест", "Тестовый сабтаск-156",TaskStatus.NEW, 4, 25L);
        taskManager.addSubTask(subTask5);

        //Проверяем, что метод генерации верно выдал новый ID.
        assertEquals(9, subTask5.getTaskID(), "Генерация ID задач не работает!");
    }

    //Проверяем корректность создания копии задачи для сохранения в истории просмотра.
    @Test
    void createTaskCopyTesting() {
        Task testedTask = taskManager.createTaskCopy(task2);
        task2.title = "ChangedТаск Тест 2";
        task2.description = "ChangedТестовый таск-2";
        task2.setTaskStatus(TaskStatus.DONE);

        //Проверяем, что копия таски сохранила значение всех полей.
        assertEquals("Таск Тест 2", testedTask.title, "Несовпадение полей!");
        assertEquals("Тестовый таск-2", testedTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedTask.taskStatus, "Несовпадение полей!");
        assertEquals(2, testedTask.taskID, "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(20), testedTask.getDuration(), "Длительность не совпадает!");
    }

    //Проверяем корректность создания копии эпик-задачи.
    @Test
    void createEpicTaskCopyTesting() {
        EpicTask testedEpicTask = taskManager.createEpicTaskCopy(epicTask1);
        epicTask1.title = "ChangedЭпик Тест 1";
        epicTask1.description = "ChangedТестовый эпик-1";
        subTask1.setTaskStatus(TaskStatus.DONE);
        subTask2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        SubTask otherSubTask = new SubTask("test", "subForTest", TaskStatus.DONE, 3, 15L);
        taskManager.addSubTask(otherSubTask);

        //Проверяем, что в epicTask1 в список сабстасков был добавлен ID 9 нового сабтаска,
        //и что статус epicTask1 изменился на DONE.
        assertTrue(taskManager.getEpicTaskByID(3).getEpicSubTaskIDList().contains(9));
        assertEquals(TaskStatus.DONE, taskManager.getEpicTaskByID(3).getTaskStatus(), "Статусы не совпадают!");

        //Проверяем, что копия эпика сохранила значение всех полей.
        assertEquals("Эпик Тест 1", testedEpicTask.title, "Несовпадение полей!");
        assertEquals("Тестовый эпик-1", testedEpicTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedEpicTask.taskStatus, "Несовпадение полей!");
        assertEquals(3, testedEpicTask.taskID, "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(30), testedEpicTask.getDuration(), "Длительность не совпадает!");

        //Проверяем, что копия эпика сохранила неизменным список ID сабтасков.
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(5));
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(6));
        assertFalse(testedEpicTask.getEpicSubTaskIDList().contains(9));
    }

    //Проверяем корректность создания копии подзадачи.
    @Test
    void createSubTaskCopyTesting() {
        SubTask testedSubTask = taskManager.createSubTaskCopy(subTask3);
        subTask3.title = "ChangedСабтаск Тест 3";
        subTask3.description = "ChangedТестовый сабтаск-3";
        subTask3.setTaskStatus(TaskStatus.DONE);

        //Проверяем, что копия таски сохранила значение всех полей.
        assertEquals("Сабтаск Тест 3", testedSubTask.title, "Несовпадение полей!");
        assertEquals("Тестовый сабтаск-3", testedSubTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedSubTask.taskStatus, "Несовпадение полей!");
        assertEquals(7, testedSubTask.taskID, "Несовпадение полей!");
        assertEquals(4, testedSubTask.getEpicTaskID(), "Несовпадение полей!");
        assertEquals(Duration.ofMinutes(15), testedSubTask.getDuration(), "Длительность не совпадает!");
    }

    //Метод для получения менеджера истории.
    @Test
    void getHistoryManagerTesting() {
        historyManager = taskManager.getHistoryManager();

        //Проверяем, что менеджер истории существует
        assertNotNull(historyManager);

        //Проверяем, что возвращаемый объект нужного типа.
        assertInstanceOf(HistoryManager.class, historyManager, "Тип не совпадает!");

        Task testedTask = taskManager.getTaskByID(2);
        EpicTask testedEpicTask = taskManager.getEpicTaskByID(4);
        SubTask testedSubTask = taskManager.getSubTaskByID(8);

        HistoryManager newHistoryManager = taskManager.getHistoryManager();

        //Проверим, что возвращается один и тот же объект после проведения операций с менеджером истории.
        assertSame(historyManager, newHistoryManager, "Должен вернуться один и тот же экземпляр!");
    }
}