package server;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import managers.Managers;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpTasksServer;
    private Gson gson;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpTasksServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpTasksServer.createContext("/tasks", new TasksHandler(taskManager));
        httpTasksServer.createContext("/subtasks", new SubTasksHandler(taskManager));
        httpTasksServer.createContext("/epics", new EpicTasksHandler(taskManager));
        httpTasksServer.createContext("/history", new HistoryHandler(taskManager));
        httpTasksServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager serverTaskManager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(serverTaskManager);
        taskServer.start();
        taskServer.stop();
    }

    public Gson getGson() {
        return gson;
    }

    public void start() {
        httpTasksServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpTasksServer.stop(0);
        System.out.println("HTTP-сервер на порту " + PORT + " остановлен!");
    }

}