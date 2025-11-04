package server;

import com.sun.net.httpserver.HttpExchange;
import managers.interfaces.TaskManager;
import tasks.NotFoundException;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SubTasksHandler extends BaseHttpHandler {

    public SubTasksHandler(TaskManager taskManager) {
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
                    if (Pattern.matches("^/subtasks$", path)) {
                        String response = gson.toJson(taskManager.getSubTasksList());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            SubTask subTask = taskManager.getSubTaskByID(id);
                            String response = gson.toJson(subTask);
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
                    if (Pattern.matches("^/subtasks$", path)) {
                        String request = new String(httpExchange.getRequestBody().readAllBytes(),
                                StandardCharsets.UTF_8);
                        SubTask subTask = gson.fromJson(request, SubTask.class);
                        int taskID = subTask.getTaskID();
                        if (subTask.getStartTime() != null && taskManager.isOverlapWithCurrentTasks(subTask)) {
                            System.out.println("Добавляемая подзадача пересекается по времени с существующей.");
                            sendHasOverlaps(httpExchange);
                            break;
                        }
                        if (taskID == 0) {
                            taskManager.addSubTask(subTask);
                            System.out.println("Добавлена новая подзадача с ID: " + subTask.getTaskID());
                        } else {
                            taskManager.updateSubTask(subTask);
                            System.out.println("Была обновлена подзадача с ID: " + subTask.getTaskID());
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                    } else {
                        System.out.println("Предоставлен некорректный URL");
                        sendNotFound(httpExchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            taskManager.removeSubTaskByID(id);
                            System.out.println("Удалена подзадача с ID: " + id);
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