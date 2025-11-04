package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected HistoryManager historyManager;
    protected Gson gson;

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        historyManager = taskManager.getHistoryManager();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, response.length);
        httpExchange.getResponseBody().write(response);
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Error-Description",
                "An incorrect ID was provided, or a task with that ID does not exist," +
                        " or an incorrect URL was provided.");
        httpExchange.sendResponseHeaders(404, 0);
        httpExchange.close();
    }

    protected void sendHasOverlaps(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders()
                .add("Error-Description", "The added task overlaps in time with an existing one.");
        httpExchange.sendResponseHeaders(406, 0);
        httpExchange.close();
    }

    protected int parseID(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}