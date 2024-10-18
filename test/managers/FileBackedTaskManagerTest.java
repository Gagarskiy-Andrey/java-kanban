package managers;

import exceptions.ManagerLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager taskManager;

    private File file;

    @BeforeEach
    void init() {
        file = null;
        try {
            file = java.io.File.createTempFile("data", "csv");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        taskManager = Manager.getFileBackedTaskManager(file);
    }

    @Test
    void save_shouldSaveFewTasksInFile() {
        // prepare
        Task task = new Task("Задача1", "Описание1", TaskStatus.NEW);
        Epic epic = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 2);
        //do
        Task newTask = taskManager.addNewTask(task);
        Epic newEpic = taskManager.addNewEpic(epic);
        Subtask newSubtask = taskManager.getSubtask(taskManager.addNewSubtask(subtask));
        // check
        boolean taskInFile = false;
        boolean epicInFile = false;
        boolean subInFile = false;
        try {
            for (String s : Files.readAllLines(file.toPath())) {
                if (s.contains(newTask.getName())) {
                    taskInFile = true;
                } else if (s.contains(newEpic.getName())) {
                    epicInFile = true;
                } else if (s.contains(newSubtask.getName())) {
                    subInFile = true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assertTrue(taskInFile && epicInFile && subInFile);
    }

    @Test
    void save_shouldLoadTasks() {
        // prepare
        Task task = new Task("Задача1", "Описание1", TaskStatus.NEW);
        Epic epic = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, 2);
        //do
        Task newTask = taskManager.addNewTask(task);
        Epic newEpic = taskManager.addNewEpic(epic);
        Subtask newSubtask = taskManager.getSubtask(taskManager.addNewSubtask(subtask));
        // check
        FileBackedTaskManager manager = null;
        try {
            manager = taskManager.loadFromFile(file);
        } catch (ManagerLoadException | NullPointerException exception) {
            exception.printStackTrace();
        }
        Task loadTask = manager.getTask(1);
        Epic loadEpic = manager.getEpic(2);
        Subtask loadSub = manager.getSubtask(3);
        assertEquals(newTask, loadTask);
        assertEquals(newEpic, loadEpic);
        assertEquals(newSubtask, loadSub);
    }
}
