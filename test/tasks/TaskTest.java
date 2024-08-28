package tasks;

import manager.Manager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class TaskTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Manager.getDefault();
    }

    @Test
    void getId() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        Integer id = taskManager.getTask(1).getId();
        // check
        assertEquals(1, id);
    }

    @Test
    void setId() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        taskManager.getTask(1).setId(7);
        // check
        assertEquals(7, taskManager.getTask(1).getId());
    }

    @Test
    void getStatus() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        TaskStatus status = taskManager.getTask(1).getStatus();
        // check
        assertEquals(TaskStatus.NEW, status);
    }

    @Test
    void setStatus() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        taskManager.getTask(1).setStatus(TaskStatus.DONE);
        // check
        assertEquals(TaskStatus.DONE, taskManager.getTask(1).getStatus());
    }

    @Test
    void getName() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        String name = taskManager.getTask(1).getName();
        // check
        assertEquals("task1", name);
    }

    @Test
    void setName() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        taskManager.getTask(1).setName("task2");
        // check
        assertEquals("task2", taskManager.getTask(1).getName());
    }

    @Test
    void getDescription() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        String description = taskManager.getTask(1).getDescription();
        // check
        assertEquals("description1", description);
    }

    @Test
    void setDescription() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task);
        // do
        taskManager.getTask(1).setDescription("description2");
        // check
        assertEquals("description2", taskManager.getTask(1).getDescription());
    }

    @Test
    void checkEqualTasks() {
        // prepare
        Task task1 = new Task(1, "task1", "description1", TaskStatus.NEW);
        Task task2 = new Task(1, "task2", "description2", TaskStatus.NEW);
        // do
        // check
        assertEquals(task1, task2);
    }
}