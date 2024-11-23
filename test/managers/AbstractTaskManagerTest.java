package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractTaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected int duration;
    protected LocalDateTime time1;
    protected LocalDateTime time2;
    protected LocalDateTime time3;
    protected LocalDateTime time4;

    @BeforeEach
    void init() {
        taskManager = (T) Manager.getDefault();
        duration = 10;
        time1 = LocalDateTime.of(2024, 11, 11, 10, 10);
        time2 = LocalDateTime.of(2024, 11, 11, 11, 10);
        time3 = LocalDateTime.of(2024, 11, 11, 12, 10);
        time4 = LocalDateTime.of(2024, 11, 11, 13, 10);
    }

    @Test
    void getHistoryManager() {
        // prepare
        HistoryManager historyManager;
        // do
        historyManager = Manager.getDefaultHistory();
        // check
        assertNotNull(historyManager);
    }

    @Test
    void getTask() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        Task expectedTask = new Task(1, "task1", "description1", TaskStatus.NEW, time1, duration);
        // do
        taskManager.addNewTask(task);
        // check
        assertNotNull(taskManager.getTask(1));
        assertEquals(expectedTask, taskManager.getTask(1));
    }

    @Test
    void addNewTask() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        Task expectedTask = new Task(1, "task1", "description1", TaskStatus.NEW, time1, duration);
        // do
        taskManager.addNewTask(task);
        // check
        assertFalse(taskManager.getTasks().isEmpty());
        assertEquals(expectedTask, taskManager.getTask(1));
    }

    @Test
    void updateTask_() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        Task updatedTask = new Task(taskManager.addNewTask(task).getId(), "task2", "description2", TaskStatus.NEW, time1, duration);
        // do
        taskManager.updateTask(updatedTask);
        // check
        assertEquals(updatedTask.getName(), taskManager.getTask(1).getName());
        assertEquals(updatedTask.getDescription(), taskManager.getTask(1).getDescription());
    }

    @Test
    void deleteSecondTask_andDeleteTaskInHistory() {
        // prepare
        HistoryManager historyManager;
        Task first = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        Task second = new Task("task2", "description2", TaskStatus.NEW, time2, duration);
        int taskId = taskManager.addNewTask(second).getId();
        Task last = new Task("task3", "description3", TaskStatus.NEW, time3, duration);
        taskManager.addNewTask(first);
        taskManager.addNewTask(last);
        // do
        historyManager = Manager.getDefaultHistory();
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        // check
        taskManager.deleteTask(taskId);
        assertEquals(taskManager.getTasks().size(), 2);
        assertEquals(taskManager.getHistory().size(), 2);
        assertFalse(taskManager.getHistory().contains(second));
    }

    @Test
    void deleteFirstTask_andDeleteTaskInHistory() {
        // prepare
        HistoryManager historyManager;
        Task first = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        int taskId = taskManager.addNewTask(first).getId();
        Task second = new Task("task2", "description2", TaskStatus.NEW, time2, duration);
        Task last = new Task("task3", "description3", TaskStatus.NEW, time3, duration);
        taskManager.addNewTask(second);
        taskManager.addNewTask(last);
        // do
        historyManager = Manager.getDefaultHistory();
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        // check
        taskManager.deleteTask(taskId);
        assertEquals(taskManager.getTasks().size(), 2);
        assertEquals(taskManager.getHistory().size(), 2);
        assertFalse(taskManager.getHistory().contains(first));
    }

    @Test
    void deleteLastTask_andDeleteTaskInHistory() {
        // prepare
        HistoryManager historyManager;
        Task first = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        Task second = new Task("task2", "description2", TaskStatus.NEW, time2, duration);
        Task last = new Task("task3", "description3", TaskStatus.NEW, time3, duration);
        int taskId = taskManager.addNewTask(last).getId();
        taskManager.addNewTask(second);
        taskManager.addNewTask(first);
        // do
        historyManager = Manager.getDefaultHistory();
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        // check
        taskManager.deleteTask(taskId);
        assertEquals(taskManager.getTasks().size(), 2);
        assertEquals(taskManager.getHistory().size(), 2);
        assertFalse(taskManager.getHistory().contains(last));
    }

    @Test
    void getEpic() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        Epic expectedEpic = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        // do
        taskManager.addNewEpic(epic);
        // check
        assertNotNull(taskManager.getEpic(1));
        assertEquals(expectedEpic, taskManager.getEpic(1));
    }

    @Test
    void addNewEpic() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        Epic expectedEpic = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        // do
        taskManager.addNewEpic(epic);
        // check
        assertFalse(taskManager.getEpics().isEmpty());
        assertEquals(expectedEpic, taskManager.getEpic(1));
    }

    @Test
    void updateEpic() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        Epic updatedEpic = new Epic(taskManager.addNewEpic(epic).getId(), "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        Subtask subtask1 = new Subtask("epic1", "description1", TaskStatus.NEW, time2, duration, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("epic2", "description2", TaskStatus.NEW, time3, duration, 1);
        taskManager.addNewSubtask(subtask2);
        // do
        taskManager.updateEpic(updatedEpic);
        // check
        assertEquals(updatedEpic.getName(), taskManager.getEpic(1).getName());
        assertEquals(updatedEpic.getDescription(), taskManager.getEpic(1).getDescription());
        assertEquals(2, taskManager.getEpic(1).getSubtaskId().get(0));
        assertEquals(3, taskManager.getEpic(1).getSubtaskId().get(1));
    }

    @Test
    void deleteEpic_andEpicSubtasks() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("epic1", "description1", TaskStatus.NEW, time2, duration, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("epic2", "description2", TaskStatus.NEW, time3, duration, 1);
        taskManager.addNewSubtask(subtask2);
        // do
        taskManager.deleteEpic(1);
        // check
        assertNull(taskManager.getEpic(1));
        assertTrue(taskManager.getSubtasks().isEmpty());

    }

    @Test
    void getEpicSubtasks() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic);
        List<Integer> epSub = new ArrayList<>();
        Subtask subtask1 = new Subtask("epic1", "description1", TaskStatus.NEW, time2, duration, 1);
        Subtask expectedSubtask1 = new Subtask(2, "epic1", "description1", TaskStatus.NEW, time2, duration);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("epic2", "description2", TaskStatus.NEW, time1, duration, 1);
        Subtask expectedSubtask2 = new Subtask(3, "epic1", "description1", TaskStatus.NEW, time3, duration);
        taskManager.addNewSubtask(subtask2);
        // do
        epSub = taskManager.getEpic(1).getSubtaskId();
        // check
        assertNotNull(epSub);
        assertEquals(expectedSubtask1.getId(), epSub.get(0));
        assertEquals(expectedSubtask2.getId(), epSub.get(1));
    }

    @Test
    void getSubtask() {
        // prepare
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, time1, duration, 1);
        Subtask expectedSubtask = new Subtask(2, "sub1", "description1", TaskStatus.NEW, time1, duration);
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        // check
        assertNotNull(taskManager.getSubtask(2));
        assertEquals(expectedSubtask, taskManager.getSubtask(2));
    }

    @Test
    void addNewSubtask() {
        // prepare
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, time1, duration, 1);
        Subtask expectedSubtask = new Subtask(2, "sub1", "description1", TaskStatus.NEW, time1, duration);
        // do
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        // check
        assertFalse(taskManager.getSubtasks().isEmpty());
        assertEquals(expectedSubtask, taskManager.getSubtask(2));
        assertEquals(expectedSubtask.getId(), taskManager.getEpic(1).getSubtaskId().get(0));
        assertEquals(taskManager.getSubtask(2).getEpicId(), epic.getId());
    }

    @Test
    void addNewSubtask_addSubtaskWithoutEpic() {
        // prepare
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, time1, duration, 1);
        // do
        taskManager.addNewSubtask(subtask);
        // check
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteSubtask() {
        // prepare
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, time1, duration, 1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        // do
        taskManager.deleteSubtask(2);
        // check
        assertTrue(taskManager.getSubtasks().isEmpty());
        assertTrue(taskManager.getEpic(1).getSubtaskId().isEmpty());
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, time1, duration, 1);
        Subtask updatedSubtask = new Subtask(2, "sub2", "description2", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        // do
        taskManager.updateSubtask(updatedSubtask);
        // check
        assertEquals(updatedSubtask.getName(), taskManager.getSubtask(2).getName());
        assertEquals(updatedSubtask.getDescription(), taskManager.getSubtask(2).getDescription());
        assertEquals(1, taskManager.getSubtask(2).getEpicId());
    }

    @Test
    void deleteAllTasks() {
        Task task1 = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewTask(task1);
        Task task2 = new Task("task2", "description2", TaskStatus.NEW, time2, duration);
        taskManager.addNewTask(task2);
        Task task3 = new Task("task3", "description3", TaskStatus.NEW, time3, duration);
        taskManager.addNewTask(task3);
        // do
        taskManager.deleteAllTasks();
        // check
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteAllEpics() {
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "description2", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("epic3", "description3", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic3);
        // do
        taskManager.deleteAllEpics();
        // check
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteSubtasks() {
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "description1", TaskStatus.NEW, time1, duration, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "description1", TaskStatus.NEW, time2, duration, 1);
        taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "description1", TaskStatus.NEW, time3, duration, 1);
        taskManager.addNewSubtask(subtask3);
        // do
        taskManager.deleteSubtasks();
        // check
        assertTrue(taskManager.getSubtasks().isEmpty());
        assertTrue(taskManager.getEpic(1).getSubtaskId().isEmpty());
    }

    @Test
    void getTasks() {
        Task task1 = new Task("task1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewTask(task1);
        Task task2 = new Task("task2", "description2", TaskStatus.NEW, time2, duration);
        taskManager.addNewTask(task2);
        Task task3 = new Task("task3", "description3", TaskStatus.NEW, time3, duration);
        taskManager.addNewTask(task3);
        Task expectedTask1 = new Task(1, "task1", "description1", TaskStatus.NEW, time1, duration);
        Task expectedTask2 = new Task(2, "task2", "description2", TaskStatus.NEW, time2, duration);
        Task expectedTask3 = new Task(3, "task3", "description3", TaskStatus.NEW, time3, duration);
        // do
        taskManager.getTasks();
        // check
        assertNotNull(taskManager.getTasks());
        assertEquals(3, taskManager.getTasks().size());
        assertEquals(expectedTask1, taskManager.getTasks().get(0));
        assertEquals(expectedTask2, taskManager.getTasks().get(1));
        assertEquals(expectedTask3, taskManager.getTasks().get(2));
    }

    @Test
    void getEpics() {
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "description2", TaskStatus.NEW, time2, duration);
        taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("epic3", "description3", TaskStatus.NEW, time3, duration);
        taskManager.addNewEpic(epic3);
        Epic expectedEpic1 = new Epic(1, "epic1", "description1", TaskStatus.NEW, time1, Duration.ofMinutes(duration));
        Epic expectedEpic2 = new Epic(2, "epic2", "description2", TaskStatus.NEW, time2, Duration.ofMinutes(duration));
        Epic expectedEpic3 = new Epic(3, "epic3", "description3", TaskStatus.NEW, time3, Duration.ofMinutes(duration));
        // do
        taskManager.getEpics();
        // check
        assertNotNull(taskManager.getEpics());
        assertEquals(3, taskManager.getEpics().size());
        assertEquals(expectedEpic1, taskManager.getEpics().get(0));
        assertEquals(expectedEpic2, taskManager.getEpics().get(1));
        assertEquals(expectedEpic3, taskManager.getEpics().get(2));
    }

    @Test
    void getSubtasks() {
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW, time1, duration);
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "description1", TaskStatus.NEW, time1, duration, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "description2", TaskStatus.NEW, time2, duration, 1);
        taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "description3", TaskStatus.NEW, time3, duration, 1);
        taskManager.addNewSubtask(subtask3);
        Subtask expectedSubtask1 = new Subtask(2, "subtask1", "description1", TaskStatus.NEW, time1, duration);
        Subtask expectedSubtask2 = new Subtask(3, "subtask2", "description2", TaskStatus.NEW, time2, duration);
        Subtask expectedSubtask3 = new Subtask(4, "subtask3", "description3", TaskStatus.NEW, time3, duration);
        // do
        taskManager.getSubtasks();
        // check
        assertNotNull(taskManager.getSubtasks());
        assertEquals(3, taskManager.getSubtasks().size());
        assertEquals(expectedSubtask1, taskManager.getSubtasks().get(0));
        assertEquals(expectedSubtask2, taskManager.getSubtasks().get(1));
        assertEquals(expectedSubtask3, taskManager.getSubtasks().get(2));
    }
}
