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

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends AbstractTaskManagerTest<TaskManager> {

    private FileBackedTaskManager fileBackedTaskManager;

    private File file;

    @BeforeEach
    void initFile() {
        file = null;
        try {
            file = java.io.File.createTempFile("data", "csv");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        fileBackedTaskManager = Manager.getFileBackedTaskManager(file);
    }

    @Test
    void save_shouldSaveFewTasksInFile() {
        // prepare
        Task task = new Task("Задача1", "Описание1", TaskStatus.NEW, time1, duration);
        Epic epic = new Epic("Эпик1", "Описание1", TaskStatus.NEW, time2, duration);
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, time3, duration, 2);
        //do
        Task newTask = fileBackedTaskManager.addNewTask(task);
        Epic newEpic = fileBackedTaskManager.addNewEpic(epic);
        Subtask newSubtask = fileBackedTaskManager.getSubtask(fileBackedTaskManager.addNewSubtask(subtask));
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
        Task task = new Task("Задача1", "Описание1", TaskStatus.NEW, time1, duration);
        Epic epic = new Epic("Эпик1", "Описание1", TaskStatus.NEW, time2, duration);
        Subtask subtask = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, time3, duration, 2);
        //do
        Task newTask = fileBackedTaskManager.addNewTask(task);
        Epic newEpic = fileBackedTaskManager.addNewEpic(epic);
        Subtask newSubtask = fileBackedTaskManager.getSubtask(fileBackedTaskManager.addNewSubtask(subtask));
        // check
        FileBackedTaskManager manager = null;
        try {
            manager = fileBackedTaskManager.loadFromFile(file);
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

    @Test
    void save_shouldThrowsExceptionLoadWrongFile() {
        // prepare
        // do
        // check
        assertThrows(ManagerLoadException.class, () -> FileBackedTaskManager.loadFromFile(new File("file")));
    }
}
