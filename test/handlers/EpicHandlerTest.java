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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicHandlerTest {
    private static final String EPICS_URL = "http://localhost:8080/epics";
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
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("ЭПИК 1", "ОПИСАНИЕ ЭПИКА", TaskStatus.NEW);
        String jsonEpic = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EPICS_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Epic addEpic = taskManager.getEpic(1);
        assertNotNull(addEpic);
        assertEquals("ЭПИК 1", addEpic.getName());
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("ЭПИК 1", "ОПИСАНИЕ ЭПИКА", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EPICS_URL + "?id=1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic returnedEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals("ЭПИК 1", returnedEpic.getName());
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "ОПИСАНИЕ ЭПИКА 1", TaskStatus.NEW);
        Epic epic2 = new Epic("Эпик 2", "ОПИСАНИЕ ЭПИКА 2", TaskStatus.NEW);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EPICS_URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(2, epics.length);
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Эпик 1", "ОПИСАНИЕ ЭПИКА", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EPICS_URL + "?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(taskManager.getEpic(1));
    }
}
