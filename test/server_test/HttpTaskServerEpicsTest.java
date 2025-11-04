package server_test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.Managers;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.TaskStatus;
import tasks.TaskType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerEpicsTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = httpTaskServer.getGson();
    EpicTask epicTask;
    SubTask subTask1;
    HttpClient client;

    HttpTaskServerEpicsTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        epicTask = new EpicTask("Эпик Тест 1", "Тестовый эпик-1", TaskStatus.NEW);
        subTask1 = new SubTask("Сабтаск Тест 1", "Тестовый сабтаск-1", TaskStatus.NEW, 1, 15L);
        taskManager.clearTasksLists();
        taskManager.clearSubTasksLists();
        taskManager.clearEpicTasksLists();
        client = HttpClient.newHttpClient();
        httpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    @Test
    void getAllEpicTasksTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую Epic-задачу
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение списка Epic-задач и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список Epic-задач существует и не пуст
        List<EpicTask> epicTasksFromServer = gson.fromJson(response.body(),
                new TypeToken<List<EpicTask>>() {}.getType());
        assertNotNull(epicTasksFromServer, "Эпики не возвращаются.");
        assertEquals(1, epicTasksFromServer.size(), "Некорректное количество эпиков.");

        //Проверяем, что в списке эпик с корректными данными
        assertEquals("Эпик Тест 1", epicTasksFromServer.get(0).title, "Некорректное имя эпика.");
        assertEquals("Тестовый эпик-1", epicTasksFromServer.get(0).description,
                "Некорректное описание эпика.");
        assertEquals(TaskStatus.NEW, epicTasksFromServer.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(2, epicTasksFromServer.get(0).getEpicSubTaskIDList().getFirst(),
                "Некорректный номер подзадачи");
        assertEquals(TaskType.EPICTASK, epicTasksFromServer.get(0).taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), epicTasksFromServer.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                epicTasksFromServer.get(0).getStartTime(), "Некорректное время начала.");
    }

    @Test
    void getEpicTaskByIDTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую Epic-задачу
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение эпика и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что получили нужный эпик с корректными данными
        EpicTask epicTaskFromServer = gson.fromJson(response.body(), EpicTask.class);

        assertEquals("Эпик Тест 1", epicTaskFromServer.title, "Некорректное имя задачи.");
        assertEquals("Тестовый эпик-1", epicTaskFromServer.description,
                "Некорректное описание эпика.");
        assertEquals(TaskStatus.NEW, epicTaskFromServer.getTaskStatus(), "Некорректный статус.");
        assertEquals(2, epicTaskFromServer.getEpicSubTaskIDList().getFirst(),
                "Некорректный номер подзадачи");
        assertEquals(TaskType.EPICTASK, epicTaskFromServer.taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), epicTaskFromServer.getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                epicTaskFromServer.getStartTime(), "Некорректное время начала.");

        //Проверим, что вернется верный ответ в случае, если попытаться получить эпик по несуществующему ID
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/3"))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(404, response2.statusCode());
        assertEquals("An incorrect ID was provided, or a task with that ID does not exist," +
                " or an incorrect URL was provided.", response2.headers().firstValue("Error-Description").orElse(null));
    }

    @Test
    void getEpicSubTaskIDListTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую Epic-задачу и пару подзадач
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        SubTask subTask2 = new SubTask("Сабтаск Тест 2", "Тестовый сабтаск-2", TaskStatus.NEW, 1, 15L);
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение эпика и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список подзадач эпика существует и не пуст
        List<Integer> epicSubTaskIDListFromServer = gson.fromJson(response.body(),
                new TypeToken<List<Integer>>() {}.getType());
        assertNotNull(epicSubTaskIDListFromServer, "Список не возвращается.");
        assertEquals(2, epicSubTaskIDListFromServer.size(), "Некорректное количество подзадач.");
        assertEquals(2, epicSubTaskIDListFromServer.get(0), "Некорректный номер подзадачи");
        assertEquals(3, epicSubTaskIDListFromServer.get(1), "Некорректный номер подзадачи");

        //Проверим, что вернется верный ответ в случае, если попытаться получить подзадачи эпика по несуществующему ID
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/4/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(404, response2.statusCode());
        assertEquals("An incorrect ID was provided, or a task with that ID does not exist," +
                " or an incorrect URL was provided.", response2.headers().firstValue("Error-Description").orElse(null));
    }

    @Test
    void addEpicTaskTesting() throws IOException, InterruptedException {
        //Конвертируем эпик в Json
        String jsonEpicTask = gson.toJson(epicTask);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpicTask))
                .build();

        //Посылаем запрос на создание нового эпика и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(201, response.statusCode());

        //Проверяем, что создался один эпик с корректными данными
        List<EpicTask> epicTasksFromManager = taskManager.getEpicTasksList();

        assertNotNull(epicTasksFromManager, "Эпики не возвращаются.");
        assertEquals(1, epicTasksFromManager.size(), "Некорректное количество эпиков.");
        assertEquals("Эпик Тест 1", epicTasksFromManager.get(0).title, "Некорректное имя эпика.");
        assertEquals("Тестовый эпик-1", epicTasksFromManager.get(0).description,
                "Некорректное описание эпика.");
        assertEquals(TaskStatus.NEW, epicTasksFromManager.get(0).getTaskStatus(), "Некорректный статус.");
        assertTrue(epicTasksFromManager.get(0).getEpicSubTaskIDList().isEmpty(), "Список ID подзадач эпика не пуст.");
        assertEquals(TaskType.EPICTASK, epicTasksFromManager.get(0).taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(0L), epicTasksFromManager.get(0).getDuration(), "Некорректная длительность.");
        assertNull(epicTasksFromManager.get(0).getStartTime(), "Некорректное время начала.");
    }

    @Test
    void deleteEpicTaskByIDTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новый эпик
        taskManager.addEpicTask(epicTask);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        //Посылаем запрос на удаление подзадачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список эпиков пуст
        List<EpicTask> epicTasksFromManager = taskManager.getEpicTasksList();
        assertNotNull(epicTasksFromManager, "Эпики не возвращаются.");
        assertTrue(epicTasksFromManager.isEmpty(), "Список не пуст.");

        //Проверим, что вернется верный ответ в случае, если попытаться удалить подзадачу по несуществующему ID
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(404, response2.statusCode());
        assertEquals("An incorrect ID was provided, or a task with that ID does not exist," +
                " or an incorrect URL was provided.", response2.headers().firstValue("Error-Description").orElse(null));
    }

}