package server_test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.Managers;
import managers.interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerSubTasksTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = httpTaskServer.getGson();
    EpicTask epicTask;
    SubTask subTask1;
    HttpClient client;

    HttpTaskServerSubTasksTest() throws IOException {
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
    void getAllSubTasksTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую подзадачу
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение списка подзадач и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список подзадач существует и не пуст
        List<SubTask> subTasksFromServer = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
        assertNotNull(subTasksFromServer, "Подзадачи не возвращаются.");
        assertEquals(1, subTasksFromServer.size(), "Некорректное количество подзадач.");

        //Проверяем, что в списке задача с корректными данными
        assertEquals("Сабтаск Тест 1", subTasksFromServer.get(0).title, "Некорректное имя задачи.");
        assertEquals("Тестовый сабтаск-1", subTasksFromServer.get(0).description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, subTasksFromServer.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(1, subTasksFromServer.get(0).getEpicTaskID(), "Некорректный номер эпика");
        assertEquals(TaskType.SUBTASK, subTasksFromServer.get(0).taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), subTasksFromServer.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                subTasksFromServer.get(0).getStartTime(), "Некорректное время начала.");
    }

    @Test
    void getSubTaskByIDTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую подзадачу
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addEpicTask(epicTask);
        taskManager.addSubTask(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение подзадачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что получили нужную подзадачу с корректными данными
        SubTask subTaskFromServer = gson.fromJson(response.body(), SubTask.class);

        assertEquals("Сабтаск Тест 1", subTaskFromServer.title, "Некорректное имя задачи.");
        assertEquals("Тестовый сабтаск-1", subTaskFromServer.description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, subTaskFromServer.getTaskStatus(), "Некорректный статус.");
        assertEquals(1, subTaskFromServer.getEpicTaskID(), "Некорректный номер эпика");
        assertEquals(TaskType.SUBTASK, subTaskFromServer.taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), subTaskFromServer.getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                subTaskFromServer.getStartTime(), "Некорректное время начала.");

        //Проверим, что вернется верный ответ в случае, если попытаться получить подзадачу по несуществующему ID
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/3"))
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //Проверяем код ответа и ожидаемый заголовок
        assertEquals(404, response2.statusCode());
        assertEquals("An incorrect ID was provided, or a task with that ID does not exist," +
                " or an incorrect URL was provided.", response2.headers().firstValue("Error-Description").orElse(null));
    }

    @Test
    void addSubTaskTesting() throws IOException, InterruptedException {
        //Устанавливаем время начала новой подзадачи и конвертируем ее в Json
        taskManager.addEpicTask(epicTask);
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        String jsonSubTask = gson.toJson(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .build();

        //Посылаем запрос на создание новой подзадачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(201, response.statusCode());

        //Проверяем, что создалась одна задача с корректными данными
        List<SubTask> subTasksFromManager = taskManager.getSubTasksList();

        assertNotNull(subTasksFromManager, "Подзадачи не возвращаются.");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество подзадач.");
        assertEquals("Сабтаск Тест 1", subTasksFromManager.get(0).title, "Некорректное имя подзадачи.");
        assertEquals("Тестовый сабтаск-1", subTasksFromManager.get(0).description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, subTasksFromManager.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(1, subTasksFromManager.get(0).getEpicTaskID(), "Некорректный номер эпика");
        assertEquals(TaskType.SUBTASK, subTasksFromManager.get(0).taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), subTasksFromManager.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 0),
                subTasksFromManager.get(0).getStartTime(), "Некорректное время начала.");

        //Проверим, что вернется верный ответ в случае попытки добавления новой подзадачи,
        // пересекающейся по времени с имеющейся
        SubTask subTask2 = new SubTask("Сабтаск Тест 2", "Тестовый сабтаск-2", TaskStatus.NEW, 1, 20L);
        subTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 10));
        String jsonTask2 = gson.toJson(subTask2);
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
    void updateSubTaskTesting() throws IOException, InterruptedException {
        //Добавим в менедежер новую подзадачу
        taskManager.addEpicTask(epicTask);
        SubTask subTask0 = new SubTask("Сабтаск Тест 0", "Тестовый сабтаск-0", TaskStatus.NEW, 1, 15L);
        subTask0.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addSubTask(subTask0);

        //Устанавливаем время начала обновленной задачи и ее ID, конвертируем ее в Json
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 15));
        subTask1.setTaskID(2);
        String jsonTask = gson.toJson(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        //Посылаем запрос на обновление подзадачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(201, response.statusCode());

        //Проверяем, что в списке подзадач по-прежнему одна подзадача, но с новыми данными
        List<SubTask> subTasksFromManager = taskManager.getSubTasksList();

        assertNotNull(subTasksFromManager, "Задачи не возвращаются.");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество задач.");
        assertEquals("Сабтаск Тест 1", subTasksFromManager.get(0).title, "Некорректное имя задачи.");
        assertEquals("Тестовый сабтаск-1", subTasksFromManager.get(0).description,
                "Некорректное описание задачи.");
        assertEquals(TaskStatus.NEW, subTasksFromManager.get(0).getTaskStatus(), "Некорректный статус.");
        assertEquals(1, subTasksFromManager.get(0).getEpicTaskID(), "Некорректный номер эпика");
        assertEquals(TaskType.SUBTASK, subTasksFromManager.get(0).taskType, "Некорректный тип.");
        assertEquals(Duration.ofMinutes(15L), subTasksFromManager.get(0).getDuration(),
                "Некорректная длительность.");
        assertEquals(LocalDateTime.of(2025, 10, 31, 9, 15),
                subTasksFromManager.get(0).getStartTime(), "Некорректное время начала.");

        //Добавим в менеджер еще одну новую подзадачу
        SubTask subTask2 = new SubTask("Сабтск Тест 2", "Тестовый сабтаск-2", TaskStatus.NEW, 1, 15L);
        subTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 30));
        taskManager.addSubTask(subTask2);

        //Проверим, что вернется верный ответ в случае, если обновленная подзадача пересекается по времени с имеющейся
        Task subTask3 = new SubTask("Сабтаск Тест 3", "Тестовый сабтаск-3", TaskStatus.NEW, 1, 20L);
        subTask3.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 40));
        String jsonTask3 = gson.toJson(subTask3);
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
        SubTask subTask4 = new SubTask("Сабтаск Тест 0", "Тестовый сабтаск-0", TaskStatus.NEW, 1, 15L);
        subTask4.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 30));
        subTask4.setTaskID(4);
        String jsonTask4 = gson.toJson(subTask4);
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
    void deleteSubTaskByIDTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новую подзадачу
        taskManager.addEpicTask(epicTask);
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        taskManager.addSubTask(subTask1);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        //Посылаем запрос на удаление подзадачи и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список подзадач пуст
        List<SubTask> subTasksFromManager = taskManager.getSubTasksList();
        assertNotNull(subTasksFromManager, "Подзадачи не возвращаются.");
        assertTrue(subTasksFromManager.isEmpty(), "Список не пуст.");

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