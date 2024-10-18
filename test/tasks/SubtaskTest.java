package tasks;

import managers.Manager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class SubtaskTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Manager.getDefault();
    }

    @Test
    void getEpicId() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, 1);
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        // check
        assertEquals(taskManager.getEpic(1).getId(), taskManager.getSubtask(2).getEpicId());

    }

    @Test
    void setEpicId() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, 1);
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.getSubtask(2).setEpicId(2);
        // check
        assertNotEquals(taskManager.getEpic(1).getId(), taskManager.getSubtask(2).getEpicId());
    }

    @Test
    void checkEqualSubtasks() {
        // prepare
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, 1);
        // do
        // check
        assertEquals(subtask1, subtask2);
    }
}