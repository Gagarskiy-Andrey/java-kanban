package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.TaskStatus;
import tasks.Subtask;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerTest {
    private static final String SUBTASKS_URL = "http://localhost:8080/subtasks";
    private HttpTaskServer server;
    private TaskManager taskManager;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    public void startServer() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        client = HttpClient.newHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new server.typeAdapters.DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new server.typeAdapters.LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        server.start();
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        Subtask subTask = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW,
                 LocalDateTime.of(2024, 10, 22, 18, 18), 30, 1);
        String jsonSubTask = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUBTASKS_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Subtask addedSubTask = taskManager.getSubtask(2);
        assertNotNull(addedSubTask);
        assertEquals("Подзадача 1", addedSubTask.getName());
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        Subtask subTask = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 22, 18, 18), 30, 1);
        taskManager.addNewSubtask(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUBTASKS_URL + "?id=2"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask returnedSubTask = gson.fromJson(response.body(), Subtask.class);
        assertEquals("Подзадача 1", returnedSubTask.getName());
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 22, 18, 18), 30, 1));
        taskManager.addNewSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.NEW,
               LocalDateTime.of(2025, 10, 23, 18, 18), 30, 1));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUBTASKS_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(2, subtasks.length);
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        Subtask subTask = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 22, 18, 18), 30, 1);
        taskManager.addNewSubtask(subTask);
        subTask.setName("Обновленная подзадача");
        String jsonSubTask = gson.toJson(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUBTASKS_URL + "?id=2"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask updatedSubTask = taskManager.getSubtask(2);
        assertEquals("Обновленная подзадача", updatedSubTask.getName());
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        Subtask subTask = new Subtask( "Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW,
                LocalDateTime.of(2024, 10, 22, 18, 18), 30, 1);
        taskManager.addNewSubtask(subTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUBTASKS_URL + "?id=2"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(taskManager.getSubtask(1));
    }
}
