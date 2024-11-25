package server;

import com.sun.net.httpserver.HttpServer;
import managers.Manager;
import managers.TaskManager;
import server.handlers.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final TaskManager taskManager;
    private static final int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = taskManager;
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.setExecutor(null);
    }

    public void start() {
        httpServer.start();
        System.out.println("Server start");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server stop");
    }

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/andrei/clone/java-kanban/data.csv");
        TaskManager taskManager = Manager.getFileBackedTaskManager(file);
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();

        Task newTask1 = new Task("Заголовок1", "Описание1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 10), 10); // Для проверки создали задачу с параметрами
        taskManager.addNewTask(newTask1);
        Epic newEpic1 = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic1);
        Subtask newSubtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 30), 10, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask1);
        System.out.println("Загруженные задачи: " + taskManager.getTask(1));
        System.out.println("Загруженные эпики: " + taskManager.getEpic(2));
        System.out.println("Загруженные подзадачи: " + taskManager.getSubtask(3));
    }
}
