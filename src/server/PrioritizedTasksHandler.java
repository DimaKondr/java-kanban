package server;

import com.sun.net.httpserver.HttpExchange;
import managers.interfaces.TaskManager;
import tasks.NotFoundException;

import java.io.IOException;
import java.util.regex.Pattern;

public class PrioritizedTasksHandler extends BaseHttpHandler {

    public PrioritizedTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();

            if (Pattern.matches("^/prioritized$", path) && "GET".equals(method)) {
                String response = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(httpExchange, response);
            } else {
                System.out.println("Предоставлен неверный URL");
                sendNotFound(httpExchange);
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
