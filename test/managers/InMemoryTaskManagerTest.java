package managers;

import exceptions.TaskValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<TaskManager> {

    @BeforeEach
    void initManager() {
        TaskManager taskManager = Manager.getDefault();
    }

    @Test
    void updateEpicStatus_doubleNewSubtasks() {
        // prepare
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);
        Epic expectedEpic = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("sub1", "desc1", TaskStatus.NEW, time1, duration, epic.getId());
        Subtask subtask2 = new Subtask("sub2", "desc2", TaskStatus.NEW, time2, duration, epic.getId());
        // do
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        // check
        assertEquals(epic.getStatus(), TaskStatus.NEW);
    }

    @Test
    void updateEpicStatus_doubleDoneSubtasks() {
        // prepare
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);
        Epic expectedEpic = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("sub1", "desc1", TaskStatus.DONE, time1, duration, epic.getId());
        Subtask subtask2 = new Subtask("sub2", "desc2", TaskStatus.DONE, time2, duration, epic.getId());
        // do
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        // check
        assertEquals(epic.getStatus(), TaskStatus.DONE);
    }

    @Test
    void updateEpicStatus_doubleInProgressSubtasks() {
        // prepare
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);
        Epic expectedEpic = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("sub1", "desc1", TaskStatus.IN_PROGRESS, time1, duration, epic.getId());
        Subtask subtask2 = new Subtask("sub2", "desc2", TaskStatus.IN_PROGRESS, time2, duration, epic.getId());
        // do
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        // check
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void updateEpicStatus_newAndDoneSubtasks() {
        // prepare
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);
        Epic expectedEpic = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("sub1", "desc1", TaskStatus.NEW, time1, duration, epic.getId());
        Subtask subtask2 = new Subtask("sub2", "desc2", TaskStatus.DONE, time2, duration, epic.getId());
        // do
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        // check
        assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void checkForIntersections_shouldNotSaveTasksWithIntersection() {
        // prepare
        Task task1 = new Task("task1", "desc1", TaskStatus.NEW, time1, 20);
        Task task2 = new Task("task2", "desc2", TaskStatus.NEW, time1, 20);
        //do
        taskManager.addNewTask(task1);
        //check
        assertThrows(TaskValidationException.class, () -> taskManager.addNewTask(task2));
        /*assertEquals(taskManager.getTasks().size(), 1);
        assertTrue(taskManager.getTasks().contains(task1));*/
    }

    @Test
    void updateEpicTimeFrame() {
        // prepare
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);
        Epic expectedEpic = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("sub1", "desc1", TaskStatus.NEW, time1, duration, epic.getId());
        Subtask subtask2 = new Subtask("sub2", "desc2", TaskStatus.DONE, LocalDateTime.of(2024, 11, 11, 10, 20), duration, epic.getId());
        // do
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        // check
        assertEquals(expectedEpic.getStartTime(), subtask1.getStartTime());
        assertEquals(expectedEpic.getDuration(), Duration.between(subtask1.getStartTime(), subtask2.getEndTime()));
    }

    @Test
    void updateEpicTimeFrame_whenDeleteSubtask() {
        // prepare
        Epic epic = new Epic("epic", "description", TaskStatus.NEW);
        Epic expectedEpic = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("sub1", "desc1", TaskStatus.NEW, time1, duration, epic.getId());
        Subtask subtask2 = new Subtask("sub2", "desc2", TaskStatus.DONE, LocalDateTime.of(2024, 11, 11, 10, 20), duration, epic.getId());
        // do
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        LocalDateTime beforeDelete = taskManager.getEpic(1).getStartTime();
        Duration oldDuration = taskManager.getEpic(1).getDuration();
        taskManager.deleteSubtask(2);
        // check
        assertNotEquals(expectedEpic.getStartTime(), beforeDelete);
        assertEquals(taskManager.getEpic(1).getStartTime(), subtask2.getStartTime());
        assertNotEquals(expectedEpic.getDuration(), oldDuration);
        assertEquals(taskManager.getEpic(1).getDuration(), subtask2.getDuration());
    }
}