import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;
    HistoryManager historyManager;
    Task task1;
    Task task2;
    EpicTask epicTask1;
    EpicTask epicTask2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    SubTask subTask4;

    @BeforeEach
    void createTaskManager() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("Таск Тест 1", "Тестовый таск-1", TaskStatus.NEW);
        task1.setTaskID(1);
        inMemoryTaskManager.generalTasksCount += 1;
        task2 = new Task("Таск Тест 2", "Тестовый таск-2", TaskStatus.NEW);
        task2.setTaskID(2);
        inMemoryTaskManager.generalTasksCount += 1;
        epicTask1 = new EpicTask("Эпик Тест 1", "Тестовый эпик-1", TaskStatus.NEW);
        epicTask1.setTaskID(3);
        inMemoryTaskManager.generalTasksCount += 1;
        epicTask2 = new EpicTask("Эпик Тест 2", "Тестовый эпик-2", TaskStatus.NEW);
        epicTask2.setTaskID(4);
        inMemoryTaskManager.generalTasksCount += 1;
        subTask1 = new SubTask("Сабтаск Тест 1", "Тестовый сабтаск-1",TaskStatus.NEW, 3);
        subTask1.setTaskID(5);
        inMemoryTaskManager.generalTasksCount += 1;
        subTask2 = new SubTask("Сабтаск Тест 2", "Тестовый сабтаск-2",TaskStatus.NEW, 3);
        subTask2.setTaskID(6);
        inMemoryTaskManager.generalTasksCount += 1;
        subTask3 = new SubTask("Сабтаск Тест 3", "Тестовый сабтаск-3",TaskStatus.NEW, 4);
        subTask3.setTaskID(7);
        inMemoryTaskManager.generalTasksCount += 1;
        subTask4 = new SubTask("Сабтаск Тест 4", "Тестовый сабтаск-4",TaskStatus.NEW, 4);
        subTask4.setTaskID(8);
        inMemoryTaskManager.generalTasksCount += 1;
        epicTask1.addSubTaskID(5);
        epicTask1.addSubTaskID(6);
        epicTask2.addSubTaskID(7);
        epicTask2.addSubTaskID(8);
        inMemoryTaskManager.tasksList.put(task1.getTaskID(), task1);
        inMemoryTaskManager.tasksList.put(task2.getTaskID(), task2);
        inMemoryTaskManager.epicTasksList.put(epicTask1.getTaskID(), epicTask1);
        inMemoryTaskManager.epicTasksList.put(epicTask2.getTaskID(), epicTask2);
        inMemoryTaskManager.subTasksList.put(subTask1.getTaskID(), subTask1);
        inMemoryTaskManager.subTasksList.put(subTask2.getTaskID(), subTask2);
        inMemoryTaskManager.subTasksList.put(subTask3.getTaskID(), subTask3);
        inMemoryTaskManager.subTasksList.put(subTask4.getTaskID(), subTask4);
    }

    //Проверяем метод, возвращающий список тасков.
    @Test
    void getTasksListTesting() {
        ArrayList<Task> tasksArrayList = new ArrayList<>();
        Task otherTask1 = new Task("Тест", "Тестовый таск-1", TaskStatus.NEW);
        otherTask1.setTaskID(1);
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW);
        otherTask2.setTaskID(2);
        tasksArrayList.add(otherTask1);
        tasksArrayList.add(otherTask2);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(inMemoryTaskManager.getTasksList(), "Список не существует!");
        assertEquals(tasksArrayList, inMemoryTaskManager.getTasksList(), "Списки не равны!");
        assertEquals(2, inMemoryTaskManager.getTasksList().size(), "Количество задач не совпадает!");
    }

    //Проверяем метод, возвращающий список эпиков.
    @Test
    void getEpicTasksListTesting() {
        ArrayList<EpicTask> epicTasksArrayList = new ArrayList<>();
        EpicTask otherEpicTask1 = new EpicTask("Тест", "Тестовый эпик-1", TaskStatus.NEW);
        otherEpicTask1.setTaskID(3);
        EpicTask otherEpicTask2 = new EpicTask("Тест", "Тестовый эпик-2", TaskStatus.NEW);
        otherEpicTask2.setTaskID(4);
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask2.setTaskID(6);
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        otherSubTask3.setTaskID(7);
        SubTask otherSubTask4 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        otherSubTask4.setTaskID(8);
        otherEpicTask1.addSubTaskID(5);
        otherEpicTask1.addSubTaskID(6);
        otherEpicTask2.addSubTaskID(7);
        otherEpicTask2.addSubTaskID(8);
        epicTasksArrayList.add(otherEpicTask1);
        epicTasksArrayList.add(otherEpicTask2);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(inMemoryTaskManager.getEpicTasksList(), "Список не существует!");
        assertEquals(epicTasksArrayList, inMemoryTaskManager.getEpicTasksList(), "Списки не равны!");
        assertEquals(2, inMemoryTaskManager.getEpicTasksList().size(),
                "Количество эпиков не совпадает!");
    }

    //Проверяем метод, возвращающий список сабтасков.
    @Test
    void getSubTasksListTesting() {
        ArrayList<SubTask> subTasksArrayList = new ArrayList<>();
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask2.setTaskID(6);
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        otherSubTask3.setTaskID(7);
        SubTask otherSubTask4 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        otherSubTask4.setTaskID(8);
        subTasksArrayList.add(otherSubTask1);
        subTasksArrayList.add(otherSubTask2);
        subTasksArrayList.add(otherSubTask3);
        subTasksArrayList.add(otherSubTask4);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(inMemoryTaskManager.getSubTasksList(), "Список не существует!");
        assertEquals(subTasksArrayList, inMemoryTaskManager.getSubTasksList(), "Списки не равны!");
        assertEquals(4, inMemoryTaskManager.getSubTasksList().size(),
                "Количество сабтасков не совпадает!");
    }

    //Проверяем метод, возвращающий список сабтасков определенного эпика.
    @Test
    void getSubTasksOfEpicTaskTesting() {
        ArrayList<SubTask> subTasksOfEpicTask = new ArrayList<>();
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask2.setTaskID(6);
        subTasksOfEpicTask.add(otherSubTask1);
        subTasksOfEpicTask.add(otherSubTask2);

        //Проверяем, что список существует, что списки совпадают, и что количество элементов в списке верное
        assertNotNull(inMemoryTaskManager.getSubTasksOfEpicTask(3), "Список не существует!");
        assertEquals(subTasksOfEpicTask,
                inMemoryTaskManager.getSubTasksOfEpicTask(3), "Списки не равны!");
        assertEquals(2, inMemoryTaskManager.getSubTasksOfEpicTask(3).size(),
                "Количество сабтасков в эпике не совпадает!");
    }

    //Проверяем метод, полностью очищающий список тасков.
    @Test
    void clearTasksListsTesting() {
        inMemoryTaskManager.clearTasksLists();
        assertTrue(inMemoryTaskManager.tasksList.isEmpty(), "Список не пуст!");
    }

    //Проверяем метод, полностью очищающий список эпиков, включая список сабтасков.
    @Test
    void clearEpicTasksListsTesting() {
        inMemoryTaskManager.clearEpicTasksLists();

        assertTrue(inMemoryTaskManager.epicTasksList.isEmpty(), "Список эпиков не пуст!");
        assertTrue(inMemoryTaskManager.subTasksList.isEmpty(), "Список сабтасков не пуст!");
    }

    //Проверяем метод, полностью очищающий список сабтасков, а также очищающий список ID сабтасков всех эпиков.
    @Test
    void clearSubTasksListsTesting() {
        inMemoryTaskManager.clearSubTasksLists();

        assertTrue(inMemoryTaskManager.subTasksList.isEmpty(), "Список не пуст!");

        for (Integer key : inMemoryTaskManager.epicTasksList.keySet()) {
            EpicTask epicTask = inMemoryTaskManager.epicTasksList.get(key);
            epicTask.clearEpicSubTaskIDList();
            assertTrue(epicTask.getEpicSubTaskIDList().isEmpty(), "Список сабтасков Эпика не пуст!");
        }
    }

    //Проверяем метод для получения таски по ID.
    @Test
    void getTaskByIDTesting() {
        Task testedTask = inMemoryTaskManager.getTaskByID(2);

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
        EpicTask testedEpicTask = inMemoryTaskManager.getEpicTaskByID(4);

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
        SubTask testedSubTask = inMemoryTaskManager.getSubTaskByID(7);

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
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW);
        otherTask2.setTaskID(9);
        Task newTask = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW);
        inMemoryTaskManager.addTask(newTask);

        //Проверяем, что добавленный таск существует.
        assertNotNull(inMemoryTaskManager.getTaskByID(9), "Таск не найден!");
        //Проверяем, что таску присвоен ожидаемый ID, и что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherTask2, inMemoryTaskManager.getTaskByID(9), "Таски не равны!");

        Task otherNewTask = new Task("Это!%тестовая@@$%??таска_sldkfjalewf",
                "&%(#)Тестовый><><sgkwe^^*#)_>>таск-2_sdfjsdfgn", TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.addTask(otherNewTask);

        Task testedTask = inMemoryTaskManager.getTaskByID(10);

        //Проверяем, что сохранился таск класса Task.
        assertInstanceOf(Task.class, testedTask, "Объект должен быть класса Task");

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
        inMemoryTaskManager.addEpicTask(newEpicTask);

        //Проверяем, что добавленный таск существует.
        assertNotNull(inMemoryTaskManager.getEpicTaskByID(9), "Эпик не найден!");
        //Проверяем, что таску присвоен ожидаемый ID, и что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherEpicTask2, inMemoryTaskManager.getEpicTaskByID(9), "Эпики не равны!");

        EpicTask otherNewEpicTask = new EpicTask("Это@$^тестовый@@$%??эпик_;lwekpqf<>?",
                "&%(#)Тестовый~!~dfge><><sgkwe^^*#)_>>эпик-2_qr31qf", TaskStatus.DONE);
        inMemoryTaskManager.addEpicTask(otherNewEpicTask);

        EpicTask testedEpicTask = inMemoryTaskManager.getEpicTaskByID(10);

        //Проверяем, что сохранился эпик класса EpicTask.
        assertInstanceOf(EpicTask.class, testedEpicTask, "Объект должен быть класса EpicTask");

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
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        otherSubTask3.setTaskID(9);
        SubTask newSubTask = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        inMemoryTaskManager.addSubTask(newSubTask);

        //Проверяем, что добавленный таск существует.
        assertNotNull(inMemoryTaskManager.getSubTaskByID(9), "Сабтаск не найден!");
        //Проверяем, что таску присвоен ожидаемый ID, и что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherSubTask3, inMemoryTaskManager.getSubTaskByID(9), "Сабтаски не равны!");

        SubTask subTaskWithEpicNumber = new SubTask("Тест-1", "Тестовый сабтаск-12",
                TaskStatus.NEW, 10);
        inMemoryTaskManager.addSubTask(subTaskWithEpicNumber);

        //Проверяем, что сабтаску присвоен ожидаемый ID
        assertEquals(10, subTaskWithEpicNumber.getTaskID(), "Присвоен неверный ID");
        //Проверяем, что сабтаск с определенным ID не будет внесен в спискок сабтасков менеджера,
        //при условии, что ID этого же сабтаска прописан как ID эпика, к которому относится сабтаск.
        assertFalse(inMemoryTaskManager.subTasksList.containsKey(10), "ID найден.");

        EpicTask otherNewEpicTask = new EpicTask("Test", "Epic test", TaskStatus.NEW);
        inMemoryTaskManager.addEpicTask(otherNewEpicTask);
        SubTask otherNewSubTask = new SubTask("Это)_*@#$%@@тестовый1^$#1fgh@vsсабтаск_;dfg432p23%^&qf<>>,>?",
                ")#&*%@?>Тестовый~!~dfge><<sgk$&#w^*#)_>>subtask-3%@%_qr1qf", TaskStatus.DONE, 10);
        inMemoryTaskManager.addSubTask(otherNewSubTask);

        EpicTask testedEpicTask = inMemoryTaskManager.getEpicTaskByID(10);
        SubTask testedSubTask = inMemoryTaskManager.getSubTaskByID(11);

        //Проверяем, что сохранился эпик класса EpicTask.
        assertInstanceOf(SubTask.class, testedSubTask, "Объект должен быть класса SubTask");

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
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW);
        otherTask2.setTaskID(2);
        Task updatedTask = new Task("$#^gfdjgSDGдалпа#@^#$@в", "$*&sgjks2#@(*lkжывдп2#%апопро@",
                TaskStatus.IN_PROGRESS);
        updatedTask.setTaskID(2);
        inMemoryTaskManager.updateTask(updatedTask);

        //Проверяем, что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherTask2, inMemoryTaskManager.getTaskByID(2), "Таски не равны!");

        Task testedTask = inMemoryTaskManager.getTaskByID(2);

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
        inMemoryTaskManager.updateEpicTask(updatedEpicTask);

        //Проверяем, что таски совпадают по ID, несмотря на разные поля.
        assertEquals(otherEpicTask2, inMemoryTaskManager.getEpicTaskByID(4), "Эпики не равны!");

        EpicTask testedEpicTask = inMemoryTaskManager.getEpicTaskByID(4);

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
        SubTask otherSubTask3 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 4);
        otherSubTask3.setTaskID(8);
        SubTask updatedSubTask = new SubTask("@()sldnыжве34#$@_)", "_$*^gk;g@#2;l_ываорш$#",
                TaskStatus.DONE, 4);
        updatedSubTask.setTaskID(8);
        inMemoryTaskManager.updateSubTask(updatedSubTask);

        //Проверяем, что задачи равны по ID.
        assertEquals(otherSubTask3, inMemoryTaskManager.getSubTaskByID(8), "Сабтаски не равны!");

        SubTask otherSubTask = inMemoryTaskManager.getSubTaskByID(8);

        //Проверяем, что номер эпика, к которому относится сабтаск, не изменился.
        assertEquals(otherSubTask3.getEpicTaskID(), otherSubTask.getEpicTaskID(),
                "Сабтаски принадлежат разным эпикам!");

        SubTask otherUpdatedSubTask = new SubTask("Тест-Q", "Тестовый сабтаск-18",
                TaskStatus.NEW, 8);
        otherUpdatedSubTask.setTaskID(8);
        inMemoryTaskManager.updateSubTask(otherUpdatedSubTask);

        SubTask someSubTask = inMemoryTaskManager.getSubTaskByID(8);

        //Проверяем, что ID сабтаска не сохраняется в поле с номером эпика, к которому относится сабтаск.
        assertEquals(4, someSubTask.getEpicTaskID(), "Сабтаск сохранил себя в номер своего эпика!");
        //Проверяем, что получили сабтаск именно с ID 8.
        assertEquals(8, someSubTask.getTaskID(), "ID не совпадают!");


        SubTask newUpdatedSubTask = new SubTask("Тест-DJ", "Тестовый сабтаск-34",
                TaskStatus.NEW, 15);
        newUpdatedSubTask.setTaskID(8);
        inMemoryTaskManager.updateSubTask(newUpdatedSubTask);

        SubTask someSubTask2 = inMemoryTaskManager.getSubTaskByID(8);

        //Проверяем, что в списке эпиков нет эпика с ID 15.
        assertFalse(inMemoryTaskManager.epicTasksList.containsKey(15), "ID найден.");
        //Проверяем, что сабтаск не сохраняется в эпике, которого нет в списке.
        assertEquals(4, someSubTask2.getEpicTaskID(), "Сабтаск сохранил себя в несуществующий эпик!");

        EpicTask testedEpicTask = inMemoryTaskManager.getEpicTaskByID(4);
        SubTask testedSubTask = inMemoryTaskManager.getSubTaskByID(8);

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
        Task otherTask2 = new Task("Тест", "Тестовый таск-2", TaskStatus.NEW);
        otherTask2.setTaskID(2);

        inMemoryTaskManager.removeTaskByID(2);

        assertFalse(inMemoryTaskManager.getTasksList().contains(otherTask2), "Таск не удален!");
    }

    //Проверяем метод для удаления эпика по ID, включая его сабтаски.
    @Test
    void removeEpicTaskByIDTesting() {
        EpicTask otherEpicTask1 = new EpicTask("Тест", "Тестовый эпик-1", TaskStatus.NEW);
        otherEpicTask1.setTaskID(3);
        SubTask otherSubTask1 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask1.setTaskID(5);
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask2.setTaskID(6);
        otherEpicTask1.addSubTaskID(5);
        otherEpicTask1.addSubTaskID(6);

        inMemoryTaskManager.removeEpicTaskByID(3);

        assertFalse(inMemoryTaskManager.getEpicTasksList().contains(otherEpicTask1), "Эпик не удален!");
        assertFalse(inMemoryTaskManager.getSubTasksList().contains(otherSubTask1), "Сабтаск не удален!");
        assertFalse(inMemoryTaskManager.getSubTasksList().contains(otherSubTask2), "Сабтаск не удален!");
    }

    //Проверяем метод для удаления сабтаски по ID, в том числе удаления ID сабтаска из списка ID сабтасков его эпика.
    @Test
    void removeSubTaskByIDTesting() {
        SubTask otherSubTask2 = new SubTask("Тест", "Тестовый сабтаск",TaskStatus.NEW, 3);
        otherSubTask2.setTaskID(6);
        SubTask subTask = inMemoryTaskManager.getSubTaskByID(6);

        inMemoryTaskManager.removeSubTaskByID(6);
        EpicTask epicTask = inMemoryTaskManager.getEpicTaskByID(subTask.getEpicTaskID());

        assertFalse(inMemoryTaskManager.getSubTasksList().contains(otherSubTask2), "Сабтаск не удален!");
        assertFalse(epicTask.getEpicSubTaskIDList().contains(otherSubTask2.getTaskID()), "Сабтаск не удален!");
    }

    //Проверяем метод для определения статуса эпика на основе статуса его сабтасков.
    @Test
    void chooseEpicTaskStatusTesting() {
        EpicTask epicTaskTest1 = inMemoryTaskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус всех сабтасков NEW, имеет статус NEW.
        assertEquals(TaskStatus.NEW, epicTaskTest1.getTaskStatus(), "Статус не совпадает!");

        SubTask subTaskTest1 = new SubTask("СабтаскNewТест", "Тестовый сабтаскNew",
                TaskStatus.IN_PROGRESS, 4);
        inMemoryTaskManager.addSubTask(subTaskTest1);
        EpicTask epicTaskTest2 = inMemoryTaskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус хотя бы одной сабтаски IN_PROGRESS, имеет статус IN_PROGRESS.
        assertEquals(TaskStatus.IN_PROGRESS, epicTaskTest2.getTaskStatus(), "Статус не совпадает!");

        SubTask subTaskTest2 = new SubTask("Сабтаск Тест 3", "Тестовый сабтаск-3",
                TaskStatus.DONE, 4);
        subTaskTest2.setTaskID(7);
        SubTask subTaskTest3 = new SubTask("Сабтаск Тест 4", "Тестовый сабтаск-4",
                TaskStatus.DONE, 4);
        subTaskTest3.setTaskID(8);
        SubTask subTaskTest4 = new SubTask("Сабтаск Тест 5", "Тестовый сабтаск-5",
                TaskStatus.DONE, 4);
        subTaskTest4.setTaskID(9);
        inMemoryTaskManager.updateSubTask(subTaskTest2);
        inMemoryTaskManager.updateSubTask(subTaskTest3);
        inMemoryTaskManager.updateSubTask(subTaskTest4);
        EpicTask epicTaskTest3 = inMemoryTaskManager.getEpicTaskByID(4);

        //Проверяем, что эпик, у которого статус всех сабтасков DONE, имеет статус DONE.
        assertEquals(TaskStatus.DONE, epicTaskTest3.getTaskStatus(), "Статус не совпадает!");

        EpicTask epicTaskTest4 = inMemoryTaskManager.getEpicTaskByID(4);
        epicTaskTest4.clearEpicSubTaskIDList();
        inMemoryTaskManager.chooseEpicTaskStatus(epicTaskTest4);

        //Проверяем, что эпик, у которого список ID сабтасков пуст, имеет статус NEW.
        assertEquals(TaskStatus.NEW, epicTaskTest4.getTaskStatus(), "Статус не совпадает!");
    }

    //Проверяем метод для генерации ID всех тасков.
    @Test
    void generateTaskIDTesting() {
        Task task1 = new Task("Тест", "Тестовый таск-11", TaskStatus.NEW);
        Task task2 = new Task("Тест", "Тестовый таск-12", TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        int task2ID = task2.getTaskID();

        //Проверяем, что получили ожидаемый ID.
        assertEquals(10, inMemoryTaskManager.generalTasksCount, "Генерация ID задач не работает!");
        assertEquals(10, task2ID, "Генерация ID задач не работает!");

        EpicTask epicTask15 = new EpicTask("Тест", "Тестовый эпик-15", TaskStatus.NEW);
        epicTask15.setTaskID(15);
        inMemoryTaskManager.epicTasksList.put(epicTask15.getTaskID(), epicTask15);

        //Промежуточная проверка, чтобы убедиться, что метод генерации не был применен и счетчик задач не изменился.
        assertEquals(10, inMemoryTaskManager.generalTasksCount, "Генерация ID задач не работает!");

        SubTask subTask16 = new SubTask("Тест", "Тестовый сабтаск-16",TaskStatus.NEW, 4);
        inMemoryTaskManager.addSubTask(subTask16);
        int subTask16ID = subTask16.getTaskID();

        //Проверяем, что генератор ID верно выдал новый ID 16, с учетом добавленного ID 15 вне метода генерации ID.
        assertEquals(16, inMemoryTaskManager.generalTasksCount, "Генерация ID задач не работает!");
        assertEquals(16, subTask16ID, "Генерация ID задач не работает!");
    }

    //Проверяем корректность создания копии задачи для сохранения в истории просмотра.
    @Test
    void createTaskCopyTesting() {
        Task testedTask = inMemoryTaskManager.createTaskCopy(task2);
        task2.title = "ChangedТаск Тест 2";
        task2.description = "ChangedТестовый таск-2";
        task2.setTaskStatus(TaskStatus.DONE);

        //Проверяем, что копия таски сохранила значение всех полей.
        assertEquals("Таск Тест 2", testedTask.title, "Несовпадение полей!");
        assertEquals("Тестовый таск-2", testedTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedTask.taskStatus, "Несовпадение полей!");
        assertEquals(2, testedTask.taskID, "Несовпадение полей!");
    }

    //Проверяем корректность создания копии эпик-задачи.
    @Test
    void createEpicTaskCopyTesting() {
        EpicTask testedEpicTask = inMemoryTaskManager.createEpicTaskCopy(epicTask1);
        epicTask1.title = "ChangedЭпик Тест 1";
        epicTask1.description = "ChangedТестовый эпик-1";
        subTask1.setTaskStatus(TaskStatus.DONE);
        subTask2.setTaskStatus(TaskStatus.DONE);
        SubTask otherSubTask = new SubTask("test", "subForTest", TaskStatus.DONE, 3);
        inMemoryTaskManager.addSubTask(otherSubTask);

        //Проверяем, что в epicTask1 в список сабстасков был добавлен ID 9 нового сабтаска,
        //и что статус epicTask1 изменился на DONE.
        assertTrue(epicTask1.getEpicSubTaskIDList().contains(9));
        assertEquals(TaskStatus.DONE, epicTask1.getTaskStatus(), "Статусы не совпадают!");

        //Проверяем, что копия эпика сохранила значение всех полей.
        assertEquals("Эпик Тест 1", testedEpicTask.title, "Несовпадение полей!");
        assertEquals("Тестовый эпик-1", testedEpicTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedEpicTask.taskStatus, "Несовпадение полей!");
        assertEquals(3, testedEpicTask.taskID, "Несовпадение полей!");

        //Проверяем, что копия эпика сохранила неизменным список ID сабтасков.
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(5));
        assertTrue(testedEpicTask.getEpicSubTaskIDList().contains(6));
        assertFalse(testedEpicTask.getEpicSubTaskIDList().contains(9));
    }

    //Проверяем корректность создания копии подзадачи.
    @Test
    void createSubTaskCopyTesting() {
        SubTask testedSubTask = inMemoryTaskManager.createSubTaskCopy(subTask3);
        subTask3.title = "ChangedСабтаск Тест 3";
        subTask3.description = "ChangedТестовый сабтаск-3";
        subTask3.setTaskStatus(TaskStatus.DONE);

        //Проверяем, что копия таски сохранила значение всех полей.
        assertEquals("Сабтаск Тест 3", testedSubTask.title, "Несовпадение полей!");
        assertEquals("Тестовый сабтаск-3", testedSubTask.description, "Несовпадение полей!");
        assertEquals(TaskStatus.NEW, testedSubTask.taskStatus, "Несовпадение полей!");
        assertEquals(7, testedSubTask.taskID, "Несовпадение полей!");
        assertEquals(4, testedSubTask.getEpicTaskID(), "Несовпадение полей!");
    }

    //Метод для получения менеджера истории.
    @Test
    void getHistoryManagerTesting() {
        historyManager = inMemoryTaskManager.getHistoryManager();

        //Проверяем, что менеджер истории существует
        assertNotNull(historyManager);

        //Проверяем, что возвращаемый объект нужного типа.
        assertInstanceOf(HistoryManager.class, historyManager, "Тип не совпадает!");

        Task testedTask = inMemoryTaskManager.getTaskByID(2);
        EpicTask testedEpicTask = inMemoryTaskManager.getEpicTaskByID(4);
        SubTask testedSubTask = inMemoryTaskManager.getSubTaskByID(8);

        HistoryManager newHistoryManager = inMemoryTaskManager.getHistoryManager();

        //Проверим, что возвращается один и тот же объект после проведения операций с менеджером истории.
        assertSame(historyManager, newHistoryManager, "Должен вернуться один и тот же экземпляр!");
    }

}