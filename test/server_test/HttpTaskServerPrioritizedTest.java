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
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerPrioritizedTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = httpTaskServer.getGson();
    Task task1;
    Task task2;
    EpicTask epicTask1;
    SubTask subTask1;
    SubTask subTask2;
    HttpClient client;

    HttpTaskServerPrioritizedTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        task1 = new Task("Таск Тест 1", "Тестовый таск-1", TaskStatus.NEW, 15L);
        task2 = new Task("Таск Тест 2", "Тестовый таск-2", TaskStatus.NEW, 20L);
        epicTask1 = new EpicTask("Эпик Тест 1", "Тестовый эпик-1", TaskStatus.NEW);
        subTask1 = new SubTask("Сабтаск Тест 1", "Тестовый сабтаск-1", TaskStatus.NEW, 3, 15L);
        subTask2 = new SubTask("Сабтаск Тест 2", "Тестовый сабтаск-2", TaskStatus.NEW, 3, 30L);
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
    void getPrioritizedTesting() throws IOException, InterruptedException {
        //Установим время начала и добавим в менеджер новые задачи
        task1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 0));
        task2.setStartTime(LocalDateTime.of(2025, 10, 31, 11, 30));
        subTask1.setStartTime(LocalDateTime.of(2025, 10, 31, 9, 30));
        subTask2.setStartTime(LocalDateTime.of(2025, 10, 31, 10, 30));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpicTask(epicTask1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение списка отсортированных по времени задач и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список существует и не пуст
        List<Task> prioritizedFromServer = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(prioritizedFromServer, "Список не возвращается.");
        assertEquals(4, prioritizedFromServer.size(), "Некорректное количество задач.");

        //Проверяем, что задачи в списке в нужно порядке
        assertEquals(1, prioritizedFromServer.get(0).getTaskID(), "Порядок в списках не совпадает.");
        assertEquals(4, prioritizedFromServer.get(1).getTaskID(), "Порядок в списках не совпадает.");
        assertEquals(5, prioritizedFromServer.get(2).getTaskID(), "Порядок в списках не совпадает.");
        assertEquals(2, prioritizedFromServer.get(3).getTaskID(), "Порядок в списках не совпадает.");
    }
}