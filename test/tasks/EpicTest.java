package tasks;

import managers.Manager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;

class EpicTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Manager.getDefault();
    }

    @Test
    void getSubtaskId() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 11, 10, 10), 10, 1);
        Subtask subtask2 = new Subtask("sub2", "des2", TaskStatus.NEW, LocalDateTime.of(2024, 11, 11, 10, 20), 10, 1);
        Subtask subtask3 = new Subtask("sub3", "des3", TaskStatus.NEW, LocalDateTime.of(2024, 11, 11, 10, 30), 10, 1);
        ArrayList<Integer> subId = new ArrayList<>();
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);
        subId.add(taskManager.getSubtask(2).getId());
        subId.add(taskManager.getSubtask(3).getId());
        subId.add(taskManager.getSubtask(4).getId());
        // check
        assertEquals(subId, taskManager.getEpic(1).getSubtaskId());
    }

    @Test
    void setSubtaskId() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        // do
        taskManager.addNewEpic(epic);
        taskManager.getEpic(1).setSubtaskId(7);
        // check
        assertEquals(7, taskManager.getEpic(1).getSubtaskId().get(0));
    }

    @Test
    void removeSubtask() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 11, 10, 10), 10, 1);
        ArrayList<Integer> zero = new ArrayList<>();
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.getEpic(1).removeSubtask(2);
        // check
        assertEquals(zero, taskManager.getEpic(1).getSubtaskId());
    }

    @Test
    void cleanSubtaskIds() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("sub1", "des1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 11, 10, 10), 10, 1);
        ArrayList<Integer> zero = new ArrayList<>();
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.getEpic(1).cleanSubtaskIds();
        // check
        assertEquals(zero, taskManager.getEpic(1).getSubtaskId());
    }

    @Test
    void checkEqualEpics() {
        // prepare
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW);
        Epic epic2 = new Epic("epic2", "description2", TaskStatus.NEW);
        // do
        // check
        assertEquals(epic1, epic2);
    }
}