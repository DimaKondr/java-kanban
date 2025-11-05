package managers_test;

import managers.ManagerSaveException;
import managers.file_backed.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    Path projectFolder = Paths.get(".");
    File tasksStorageTempFile;
    FileBackedTaskManager fileBackedTaskManager;

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

    //Проверяем загрузку из пустого файла.
    @Test
    void createAndLoadFromEmptyFileTesting() {
        //Создаем нового менеджера с загрузкой из пустого файла
        //и проверяем, что создание менеджера не вызывает исключений.
        Assertions.assertDoesNotThrow(() -> {
            fileBackedTaskManager = FileBackedTaskManager.loadTasksFromFile(tasksStorageTempFile);
        }, "Создание менеджера должно пройти успешно");

        //Проверяем, что все списки нового менеджера пустые
        assertTrue(fileBackedTaskManager.getTasksList().isEmpty());
        assertTrue(fileBackedTaskManager.getEpicTasksList().isEmpty());
        assertTrue(fileBackedTaskManager.getSubTasksList().isEmpty());

        //Проверяем, что при несуществующем пути выбрасывается исключение
        Assertions.assertThrows(ManagerSaveException.class, () -> fileBackedTaskManager
                        = FileBackedTaskManager.loadTasksFromFile(Path.of(projectFolder.toString(),
                        "non_existent_file.txt").toFile()), "Должен быть выброшен managers.ManagerSaveException");

        //Проверяем обработку некорректного пути
        Assertions.assertThrows(IllegalArgumentException.class,() -> fileBackedTaskManager
                        = new FileBackedTaskManager(null),
                "Должен быть выброшен IllegalArgumentException при null пути"
        );
    }

    //Проверяем метод сохранения задач в файл.
    @Test
    void saveTasksToFileTesting() {
        //Создаем менеджер и добавляем задачи.
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(tasksStorageTempFile);
        Task task1 = new Task("task1", "taskDescription", TaskStatus.NEW, 15L);
        Task task2 = new Task("task2", "taskDescription_11", TaskStatus.IN_PROGRESS, 20L);
        task2.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 0));
        EpicTask epicTask1 = new EpicTask("epic1", "epicDescription", TaskStatus.NEW);
        EpicTask epicTask2 = new EpicTask("epic2", "epicDescription_11", TaskStatus.NEW);
        SubTask subTask1 = new SubTask("subTask1", "subDescription", TaskStatus.DONE, 3, 25L);
        SubTask subTask2 = new SubTask("subTask2", "subDescription_11", TaskStatus.NEW, 4, 30L);
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 14, 15));
        EpicTask epicTask3 = new EpicTask("epic3", "epicDescription_22", TaskStatus.NEW);

        //При добавлении задачи проверим, что не возникает ошибок при записи в файл.
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addTask(task1),
                "Сохранение в файл должно пройти успешно");
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addTask(task2),
                "Сохранение в файл должно пройти успешно");
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addEpicTask(epicTask1),
                "Сохранение в файл должно пройти успешно");
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addEpicTask(epicTask2),
                "Сохранение в файл должно пройти успешно");
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addSubTask(subTask1),
                "Сохранение в файл должно пройти успешно");
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addSubTask(subTask2),
                "Сохранение в файл должно пройти успешно");
        Assertions.assertDoesNotThrow(() -> fileBackedTaskManager.addEpicTask(epicTask3),
                "Сохранение в файл должно пройти успешно");

        //Создаем нового менеджера через загрузку из файла.
        FileBackedTaskManager newManager = FileBackedTaskManager.loadTasksFromFile(tasksStorageTempFile);

        //Проверим количество задач в списках нового менеджера.
        assertEquals(2, newManager.getTasksList().size());
        assertEquals(3, newManager.getEpicTasksList().size());
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
        assertEquals(Duration.ofMinutes(20), newTask.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 10, 0), newTask.getStartTime(), "Время старта не совпадает!");

        assertEquals(3, newEpicTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.EPICTASK, newEpicTask.taskType, "Типы не совпадают!");
        assertEquals("epic1", newEpicTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals("epicDescription", newEpicTask.description, "Описания не совпадают!");
        assertEquals(Duration.ofMinutes(25), newEpicTask.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 14, 15), newEpicTask.getStartTime(),
                "Время старта не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 14, 40), newEpicTask.getEndTime(),
                "Время завершения не совпадает!");

        assertEquals(5, newSubTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.SUBTASK, newSubTask.taskType, "Типы не совпадают!");
        assertEquals("subTask1", newSubTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals("subDescription", newSubTask.description, "Описания не совпадают!");
        assertEquals(3, newSubTask.getEpicTaskID(), "ID эпика не совпадают!");
        assertEquals(Duration.ofMinutes(25), newSubTask.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 14, 15), newSubTask.getStartTime(), "Время старта не совпадает!");

        //Проверим, что верно обработаны задачи, у которых не задано время начала выполнения.
        Task taskWithoutStartTime = newManager.getTaskByID(1);
        assertNull(taskWithoutStartTime.getStartTime(), "Время старта не должно быть задано!");

        Task epicTaskWithoutStartTime = newManager.getEpicTaskByID(4);
        assertEquals(Duration.ofMinutes(30), epicTaskWithoutStartTime.getDuration(), "Длительность не совпадает!");
        assertNull(epicTaskWithoutStartTime.getStartTime(), "Время старта не должно быть задано!");

        Task subTaskWithoutStartTime = newManager.getSubTaskByID(6);
        assertNull(subTaskWithoutStartTime.getStartTime(), "Время старта не должно быть задано!");

        //Проверим, что верно обработана Epic-задача, у которой нет подзадач.
        EpicTask emptyEpicTask = newManager.getEpicTaskByID(7);
        assertEquals(Duration.ofMinutes(0), emptyEpicTask.getDuration(), "Длительность не совпадает!");
    }

    //Проверяем метод загрузки задач из файла.
    @Test
    void loadTasksFromFileTesting() {
        //Создаем тестовые данные.
        String testData = "id,type,title,status,description,epic,duration(minutes),startTime,endTimeOfEpic\n"
                + "2,TASK,task2,IN_PROGRESS,taskDescription_11,-,25,31.10.25|14:00,-\n"
                + "4,EPICTASK,epic2,DONE,epicDescription_11,-,47,05.11.25|09:45,05.11.25|10:32\n"
                + "6,SUBTASK,subTask2,DONE,subDescription_11,4,47,05.11.25|09:45,-\n"
                + "7,TASK,task7,NEW,taskDescription_77,-,30,-,-\n"
                + "8,EPICTASK,epic8,NEW,epicDescription_88,-,0,-,-";

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
        assertEquals(Duration.ofMinutes(25), newTask.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 10, 31, 14, 0), newTask.getStartTime(), "Время старта не совпадает!");

        assertEquals(4, newEpicTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.EPICTASK, newEpicTask.taskType, "Типы не совпадают!");
        assertEquals("epic2", newEpicTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newEpicTask.taskStatus, "Статусы не совпадают!");
        assertEquals("epicDescription_11", newEpicTask.description, "Описания не совпадают!");
        assertEquals(Duration.ofMinutes(47), newEpicTask.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 11, 5, 9, 45), newEpicTask.getStartTime(), "Время старта не совпадает!");
        assertEquals(LocalDateTime.of(2025, 11, 5, 10, 32), newEpicTask.getEndTime(), "Время завершения не совпадает!");

        assertEquals(6, newSubTask.getTaskID(), "ID не совпадают!");
        assertEquals(TaskType.SUBTASK, newSubTask.taskType, "Типы не совпадают!");
        assertEquals("subTask2", newSubTask.title, "Заголовки не совпадают!");
        assertEquals(TaskStatus.DONE, newSubTask.taskStatus, "Статусы не совпадают!");
        assertEquals("subDescription_11", newSubTask.description, "Описания не совпадают!");
        assertEquals(4, newSubTask.getEpicTaskID(), "Описания не совпадают!");
        assertEquals(Duration.ofMinutes(47), newSubTask.getDuration(), "Длительность не совпадает!");
        assertEquals(LocalDateTime.of(2025, 11, 5, 9, 45), newSubTask.getStartTime(), "Время старта не совпадает!");

        //Проверим, что верно обработана задача, у которой не задано время начала выполнения.
        Task taskWithoutStartTime = newManager.getTaskByID(7);
        assertNull(taskWithoutStartTime.getStartTime(), "Время старта не должно быть задано!");

        //Проверим, что верно обработана Epic-задача, у которой нет подзадач.
        EpicTask emptyEpicTask = newManager.getEpicTaskByID(8);
        assertEquals(Duration.ofMinutes(0), emptyEpicTask.getDuration(), "Длительность не совпадает!");
    }

    //Удаляем временный файл.
    @AfterEach
    void deleteTempFile() {
        tasksStorageTempFile.deleteOnExit();
    }

}