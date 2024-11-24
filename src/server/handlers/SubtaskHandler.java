package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import managers.TaskManager;
import server.typeAdapters.DurationAdapter;
import server.typeAdapters.LocalDateTimeAdapter;
import tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();
    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            switch (method) {
                case "GET" -> {
                    if (Pattern.matches("/subtasks", path)) {
                        if (query != null && query.startsWith("id=")) {
                            handleGetById(exchange);
                        } else {
                            String response = gson.toJson(taskManager.getSubtasks());
                            sendText(exchange, response, 200);
                        }
                    }
                }
                case "POST" -> handelPost(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handleGetById(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getRawQuery();
            if (query != null && query.startsWith("id=")) {
                String idStr = query.split("=")[1];
                int id = Integer.parseInt(idStr);
                Subtask subTask = taskManager.getSubtask(id);
                String response = gson.toJson(subTask);
                System.out.println(response);
                sendText(exchange, response, 200);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Internal Server Error", 500);
        }
    }

    private void handelPost(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String path = exchange.getRequestURI().getPath();
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subTask = gson.fromJson(body, Subtask.class);
        if (Pattern.matches("/subtasks", path)) {
            if (query != null && query.startsWith("id=")) {
                try {
                    String idStr = query.split("=")[1];
                    int id = Integer.parseInt(idStr);
                    subTask.setId(id);
                    taskManager.updateSubtask(subTask);
                    String response = gson.toJson(subTask);
                    System.out.println(response);
                    sendText(exchange, response, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendText(exchange, "Server Error", 500);
                }
            } else {
                try {
                    taskManager.addNewSubtask(subTask);
                    String response = gson.toJson(subTask);
                    sendText(exchange, response, 201);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendText(exchange, "Server Error", 500);
                }
            }
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        try {
            String query = exchange.getRequestURI().getRawQuery();
            if (query != null && query.startsWith("id=")) {
                String idStr = query.split("=")[1];
                int id = Integer.parseInt(idStr);
                if (id != -1) {
                    taskManager.deleteSubtask(id);
                    sendText(exchange, "Задача с id: " + id + " удалена", 200);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendText(exchange, "Internal Server Error", 500);
        }
    }
}
