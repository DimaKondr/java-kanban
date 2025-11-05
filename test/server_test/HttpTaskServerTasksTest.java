package server_test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.Managers;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Task;
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

class HttpTaskServerTasksTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = httpTaskServer.getGson();
    Task task1;
    HttpClient client;

    HttpTaskServerTasksTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        task1 = new Task("Таск Тест 1", "Тестовый таск-1", TaskStatus.NEW, 15L);
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
    void getAllTasksTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую задачу
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addTask(task1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение списка задач и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список задач существует и не пуст
        List<Task> tasksFromServer = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(tasksFromServer, "Задачи не возвращаются.");
        assertEquals(1, tasksFromServer.size(), "Некорректное количество задач.");

        //Проверяем, что в списке задача с корректными данными
        assertEquals("Таск Тест 1", tasksFromServer.get(0).title, "Некорректное имя задачи.");
        assertEquals("Тестовый таск-1", tasksFromServer.get(0).description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, tasksFromServer.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(TaskType.TASK, tasksFromServer.get(0).taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), tasksFromServer.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                tasksFromServer.get(0).getStartTime(), "Некорректное время начала.");
    }

    @Test
    void getTaskByIDTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую задачу
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addTask(task1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение задачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что получили нужную задачу с корректными данными
        Task taskFromServer = gson.fromJson(response.body(), Task.class);

        assertEquals("Таск Тест 1", taskFromServer.title, "Некорректное имя задачи.");
        assertEquals("Тестовый таск-1", taskFromServer.description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, taskFromServer.getTaskStatus(), "Некорректный статус.");
        assertEquals(TaskType.TASK, taskFromServer.taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), taskFromServer.getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                taskFromServer.getStartTime(), "Некорректное время начала.");

        //Проверим, что вернется верный ответ в случае, если попытаться получить задачу по несуществующему ID
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/2"))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(404, response2.statusCode());
        assertEquals("An incorrect ID was provided, or a task with that ID does not exist," +
                " or an incorrect URL was provided.", response2.headers().firstValue("Error-Description").orElse(null));
    }

    @Test
    void addTaskTesting() throws IOException, InterruptedException {
        //Устанавливаем время начала новой задачи и конвертируем ее в Json
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        String jsonTask = gson.toJson(task1);

        //Создаём HTTP-клиент и запрос для сервера
        //HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        //Посылаем запрос на создание новой задачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(201, response.statusCode());

        //Проверяем, что создалась одна задача с корректными данными
        List<Task> tasksFromManager = taskManager.getTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются.");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач.");
        assertEquals("Таск Тест 1", tasksFromManager.get(0).title, "Некорректное имя задачи.");
        assertEquals("Тестовый таск-1", tasksFromManager.get(0).description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, tasksFromManager.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(TaskType.TASK, tasksFromManager.get(0).taskType, "Некорректный статус.");
        assertEquals(Duration.ofMinutes(15L), tasksFromManager.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                tasksFromManager.get(0).getStartTime(), "Некорректное время начала.");

        //Проверим, что вернется верный ответ в случае попытки добавления новой задачи,
        // пересекающейся по времени с имеющейся
        Task task2 = new Task("Таск Тест 2", "Тестовый таск-2", TaskStatus.NEW, 20L);
        task2.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 10));
        String jsonTask2 = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа
        assertEquals(406, response2.statusCode());
        assertEquals("The added task overlaps in time with an existing one.",
                response2.headers().firstValue("Error-Description").orElse(null));
    }

    @Test
    void updateTaskTesting() throws IOException, InterruptedException {
        //Добавим в менедежер новую задачу
        Task task0 = new Task("Таск Тест 0", "Тестовый таск-0", TaskStatus.NEW, 15L);
        task0.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addTask(task0);

        //Устанавливаем время начала обновленной задачи и ее ID, конвертируем ее в Json
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 15));
        task1.setTaskID(1);
        String jsonTask = gson.toJson(task1);

        //Создаём HTTP-клиент и запрос для сервера
        //HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        //Посылаем запрос на обновление задачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(201, response.statusCode());

        //Проверяем, что в списке задач по-прежнему одна задача, но с новыми данными
        List<Task> tasksFromManager = taskManager.getTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются.");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач.");
        assertEquals("Таск Тест 1", tasksFromManager.get(0).title, "Некорректное имя задачи.");
        assertEquals("Тестовый таск-1", tasksFromManager.get(0).description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, tasksFromManager.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(TaskType.TASK, tasksFromManager.get(0).taskType, "Некорректный статус.");
        assertEquals(Duration.ofMinutes(15L), tasksFromManager.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 15),
                tasksFromManager.get(0).getStartTime(), "Некорректное время начала.");

        //Добавим в менеджер еще одну новую задачу
        Task task2 = new Task("Таск Тест 2", "Тестовый таск-2", TaskStatus.NEW, 15L);
        task2.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 30));
        taskManager.addTask(task2);

        //Проверим, что вернется верный ответ в случае, если обновленная задача пересекается по времени с имеющейся
        Task task3 = new Task("Таск Тест 3", "Тестовый таск-3", TaskStatus.NEW, 20L);
        task3.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 40));
        String jsonTask3 = gson.toJson(task3);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask3))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(406, response2.statusCode());
        assertEquals("The added task overlaps in time with an existing one.",
                response2.headers().firstValue("Error-Description").orElse(null));

        //Проверим, что вернется верный ответ в случае, если попытаться обновить задачу по несуществующему ID
        Task task4 = new Task("Таск Тест 0", "Тестовый таск-0", TaskStatus.NEW, 15L);
        task4.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 30));
        task4.setTaskID(3);
        String jsonTask4 = gson.toJson(task4);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask4))
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(404, response3.statusCode());
        assertEquals("An incorrect ID was provided, or a task with that ID does not exist," +
                " or an incorrect URL was provided.", response3.headers().firstValue("Error-Description").orElse(null));
    }

    @Test
    void deleteTaskByIDTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую задачу
        taskManager.addTask(task1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        //Посылаем запрос на удаление задачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список задач пуст
        List<Task> tasksFromManager = taskManager.getTasksList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются.");
        assertTrue(tasksFromManager.isEmpty(), "Список не пуст.");

        //Проверим, что вернется верный ответ в случае, если попытаться удалить задачу по несуществующему ID
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