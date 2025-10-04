import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    Path projectFolder = Paths.get(".");
    File tasksStorageTempFile;

    //Создаем временный файл.
    @BeforeEach
    void createTempFile() {
        try {
            tasksStorageTempFile = File.createTempFile("TasksStorageTempFile", ".csv",
                    projectFolder.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Проверяем сохранение и загрузку пустого файла.
    @Test
    void createAndLoadFromEmptyFileTesting() {
        //Создаем менеджер и делаем сохранение при полном отсутствии задач.
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tasksStorageTempFile);
        fileBackedTaskManager.saveTasksToFile();

        //Создаем нового менеджера с загрузкой из файла
        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadTasksFromFile(tasksStorageTempFile);

        //Проверяем, что все списки нового менеджера пустые
        assertTrue(newFileBackedTaskManager.getTasksList().isEmpty());
        assertTrue(newFileBackedTaskManager.getEpicTasksList().isEmpty());
        assertTrue(newFileBackedTaskManager.getSubTasksList().isEmpty());
    }

    //Проверяем метод сохранения задач в файл.
    @Test
    void saveTasksToFileTesting() {
        //Создаем менеджер и добавляем задачи.
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tasksStorageTempFile);
        Task task1 = new Task("task1", "taskDescription", TaskStatus.NEW);
        Task task2 = new Task("task2", "taskDescription_11", TaskStatus.IN_PROGRESS);
        EpicTask epicTask1 = new EpicTask("epic1", "epicDescription", TaskStatus.NEW);
        EpicTask epicTask2 = new EpicTask("epic2", "epicDescription_11", TaskStatus.NEW);
        SubTask subTask1 = new SubTask("subTask1", "subDescription", TaskStatus.DONE, 3);
        SubTask subTask2 = new SubTask("subTask2", "subDescription_11", TaskStatus.NEW, 4);

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpicTask(epicTask1);
        fileBackedTaskManager.addEpicTask(epicTask2);
        fileBackedTaskManager.addSubTask(subTask1);
        fileBackedTaskManager.addSubTask(subTask2);

        //Сохраняем задачи в файл и создаем нового менеджера через загрузку из файла.
        fileBackedTaskManager.saveTasksToFile();
        FileBackedTaskManager newManager = FileBackedTaskManager.loadTasksFromFile(tasksStorageTempFile);

        //Проверим количество задач в списках нового менеджера.
        assertEquals(2, newManager.getTasksList().size());
        assertEquals(2, newManager.getEpicTasksList().size());
        assertEquals(2, newManager.getSubTasksList().size());

        //Получим по одному типу каждой задачи и проверим, что объекты существуют.
        Task newTask = newManager.getTaskByID(2);
        EpicTask newEpicTask = newManager.getEpicTaskByID(3);
        SubTask newSubTask = newManager.getSubTaskByID(5);
        assertNotNull(newTask, "Таск не найден!");
        assertNotNull(newEpicTask, "Эпик не найден!");
        assertNotNull(newSubTask, "Сабтаск не найден!");

        //Проверяем, что задачи в процессе сериализации сохранили верные данные.
        assertEquals(2, newTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.TASK, newTask.taskType, "Типы не совпадают!");
        assertEquals("task2", newTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.IN_PROGRESS, newTask.taskStatus, "Статусы не совпадают!");
        assertEquals("taskDescription_11", newTask.description, "Описания не совпадают!");

        assertEquals(3, newEpicTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.EPICTASK, newEpicTask.taskType, "Типы не совпадают!");
        assertEquals("epic1", newEpicTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals("epicDescription", newEpicTask.description, "Описания не совпадают!");

        assertEquals(5, newSubTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.SUBTASK, newSubTask.taskType, "Типы не совпадают!");
        assertEquals("subTask1", newSubTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals("subDescription", newSubTask.description, "Описания не совпадают!");
        assertEquals(3, newSubTask.getEpicTaskID(), "Описания не совпадают!");
    }

    //Проверяем метод загрузки задач из файла.
    @Test
    void loadTasksFromFileTesting() {
        //Создаем тестовые данные.
        String testData = "id,type,title,status,description,epic\n" +
                "2,TASK,task2,IN_PROGRESS,taskDescription_11,\n" +
                "4,EPICTASK,epic2,DONE,epicDescription_11,\n" +
                "6,SUBTASK,subTask2,DONE,subDescription_11,4";

        try {
            Files.writeString(tasksStorageTempFile.toPath(), testData);
        } catch (IOException e) {
                fail("Ошибка записи тестовых данных", e);
        }

        //Создаем нового менеджера через загрузку из файла.
        FileBackedTaskManager newManager = FileBackedTaskManager.loadTasksFromFile(tasksStorageTempFile);

        //Получим задачи из нового менеджера и проверим, что объекты существуют.
        Task newTask = newManager.getTaskByID(2);
        EpicTask newEpicTask = newManager.getEpicTaskByID(4);
        SubTask newSubTask = newManager.getSubTaskByID(6);
        assertNotNull(newTask, "Таск не найден!");
        assertNotNull(newEpicTask, "Эпик не найден!");
        assertNotNull(newSubTask, "Сабтаск не найден!");

        //Проверяем, что задачи в процессе загрузки сохранили верные данные.
        assertEquals(2, newTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.TASK, newTask.taskType, "Типы не совпадают!");
        assertEquals("task2", newTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.IN_PROGRESS, newTask.taskStatus, "Статусы не совпадают!");
        assertEquals("taskDescription_11", newTask.description, "Описания не совпадают!");

        assertEquals(4, newEpicTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.EPICTASK, newEpicTask.taskType, "Типы не совпадают!");
        assertEquals("epic2", newEpicTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals("epicDescription_11", newEpicTask.description, "Описания не совпадают!");

        assertEquals(6, newSubTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.SUBTASK, newSubTask.taskType, "Типы не совпадают!");
        assertEquals("subTask2", newSubTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals("subDescription_11", newSubTask.description, "Описания не совпадают!");
        assertEquals(4, newSubTask.getEpicTaskID(), "Описания не совпадают!");
    }

    //Удаляем временный файл.
    @AfterEach
    void deleteTempFile() {
        tasksStorageTempFile.deleteOnExit();
    }

}