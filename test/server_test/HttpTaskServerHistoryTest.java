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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerHistoryTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = httpTaskServer.getGson();
    Task task1;
    EpicTask epicTask1;
    SubTask subTask1;
    HttpClient client;

    HttpTaskServerHistoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        task1 = new Task("Таск Тест 1", "Тестовый таск-1", TaskStatus.NEW, 15L);
        epicTask1 = new EpicTask("Эпик Тест 1", "Тестовый эпик-1", TaskStatus.NEW);
        subTask1 = new SubTask("Сабтаск Тест 1", "Тестовый сабтаск-1", TaskStatus.NEW, 2, 15L);
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
    void getHistoryTesting() throws IOException, InterruptedException {
        //Добавим в менеджер новые задачи
        taskManager.addTask(task1);
        taskManager.addEpicTask(epicTask1);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = taskManager.getSubTaskByID(subTask1.getTaskID());
        Task task2 = taskManager.getTaskByID(task1.getTaskID());
        EpicTask epicTask2 = taskManager.getEpicTaskByID(epicTask1.getTaskID());

        //Создаём запрос для сервера
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        //Посылаем запрос на получение истории просмотренных задач и получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверяем код ответа
        assertEquals(200, response.statusCode());

        //Проверяем, что список существует и не пуст
        List<Task> historyFromServer = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        assertNotNull(historyFromServer, "Список не возвращается.");
        assertEquals(3, historyFromServer.size(), "Некорректное количество задач.");

        //Проверяем, что задачи в списке в нужно порядке
        assertEquals(3, historyFromServer.get(0).getTaskID(), "Порядок в списках не совпадает.");
        assertEquals(1, historyFromServer.get(1).getTaskID(), "Порядок в списках не совпадает.");
        assertEquals(2, historyFromServer.get(2).getTaskID(), "Порядок в списках не совпадает.");
    }
}