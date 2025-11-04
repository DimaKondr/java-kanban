package server;

import com.sun.net.httpserver.HttpExchange;
import managers.interfaces.TaskManager;
import tasks.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] splitPath = path.split("/");

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getTasksList());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            Task task = taskManager.getTaskByID(id);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Предоставлен некорректный ID: " + id);
                            sendNotFound(httpExchange);
                        }
                    } else {
                        System.out.println("Предоставлен неверный URL или некорректный ID: " + splitPath[2]);
                        sendNotFound(httpExchange);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String request = new String(httpExchange.getRequestBody().readAllBytes(),
                                StandardCharsets.UTF_8);
                        Task task = gson.fromJson(request, Task.class);
                        int taskID = task.getTaskID();
                        if (task.getStartTime() != null && taskManager.isOverlapWithCurrentTasks(task)) {
                            System.out.println("Добавляемая задача пересекается по времени с существующей.");
                            sendHasOverlaps(httpExchange);
                            break;
                        }
                        if (taskID == 0) {
                            taskManager.addTask(task);
                            System.out.println("Добавлена новая задача с ID: " + task.getTaskID());
                        } else {
                            taskManager.updateTask(task);
                            System.out.println("Была обновлена задача с ID: " + task.getTaskID());
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                    } else {
                        System.out.println("Предоставлен некорректный URL");
                        sendNotFound(httpExchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            taskManager.removeTaskByID(id);
                            System.out.println("Удалена задача с ID: " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Предоставлен некорректный ID: " + id);
                            sendNotFound(httpExchange);
                        }
                    } else {
                        System.out.println("Предоставлен неверный URL или некорректный ID: " + splitPath[2]);
                        sendNotFound(httpExchange);
                    }
                    break;
                }
            }
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange);
        } catch (Exception exception) {
            httpExchange.sendResponseHeaders(500, 0);
        } finally {
            httpExchange.close();
        }
    }
}