package server;

import com.sun.net.httpserver.HttpExchange;
import managers.interfaces.TaskManager;
import tasks.EpicTask;
import tasks.NotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class EpicTasksHandler extends BaseHttpHandler {

    public EpicTasksHandler(TaskManager taskManager) {
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
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getEpicTasksList());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            EpicTask epicTask = taskManager.getEpicTaskByID(id);
                            String response = gson.toJson(epicTask);
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Предоставлен некорректный ID: " + id);
                            sendNotFound(httpExchange);
                        }
                    } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            EpicTask epicTask = taskManager.getEpicTaskByID(id);
                            String response = gson.toJson(epicTask.getEpicSubTaskIDList());
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
                    if (Pattern.matches("^/epics$", path)) {
                        String request = new String(httpExchange.getRequestBody().readAllBytes(),
                                StandardCharsets.UTF_8);
                        EpicTask epicTask = gson.fromJson(request, EpicTask.class);
                        taskManager.addEpicTask(epicTask);
                        System.out.println("Добавлена новая Epic-задача с ID: " + epicTask.getTaskID());
                        httpExchange.sendResponseHeaders(201, 0);
                    } else {
                        System.out.println("Предоставлен некорректный URL");
                        sendNotFound(httpExchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String stringID = splitPath[2];
                        int id = parseID(stringID);
                        if (id > 0) {
                            taskManager.removeEpicTaskByID(id);
                            System.out.println("Удалена Epic-задача с ID: " + id);
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