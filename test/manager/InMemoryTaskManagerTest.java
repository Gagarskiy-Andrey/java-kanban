package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void init() {
        taskManager = Manager.getDefault();
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
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        Task expectedTask = new Task(1, "task1", "description1", TaskStatus.NEW);
        // do
        taskManager.addNewTask(task);
        // check
        assertNotNull(taskManager.getTask(1));
        assertEquals(expectedTask, taskManager.getTask(1));
    }

    @Test
    void addNewTask() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        Task expectedTask = new Task(1, "task1", "description1", TaskStatus.NEW);
        // do
        taskManager.addNewTask(task);
        // check
        assertFalse(taskManager.getTasks().isEmpty());
        assertEquals(expectedTask, taskManager.getTask(1));
    }

    @Test
    void updateTask_() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        Task updatedTask = new Task(taskManager.addNewTask(task).getId(), "task2", "description2", TaskStatus.NEW);
        // do
        taskManager.updateTask(updatedTask);
        // check
        assertEquals(updatedTask.getName(), taskManager.getTask(1).getName());
        assertEquals(updatedTask.getDescription(), taskManager.getTask(1).getDescription());
    }

    @Test
    void deleteTask() {
        // prepare
        Task task = new Task("task1", "description1", TaskStatus.NEW);
        int taskId = taskManager.addNewTask(task).getId();
        // do
        taskManager.deleteTask(taskId);
        // check
        assertTrue(taskManager.getTasks().isEmpty());

    }

    @Test
    void getEpic() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Epic expectedEpic = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        // do
        taskManager.addNewEpic(epic);
        // check
        assertNotNull(taskManager.getEpic(1));
        assertEquals(expectedEpic, taskManager.getEpic(1));
    }

    @Test
    void addNewEpic() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Epic expectedEpic = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        // do
        taskManager.addNewEpic(epic);
        // check
        assertFalse(taskManager.getEpics().isEmpty());
        assertEquals(expectedEpic, taskManager.getEpic(1));
    }

    @Test
    void updateEpic() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        Epic updatedEpic = new Epic(taskManager.addNewEpic(epic).getId(), "epic1", "description1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("epic1", "description1", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("epic2", "description2", TaskStatus.NEW, 1);
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
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("epic1", "description1", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("epic2", "description2", TaskStatus.NEW, 1);
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
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        List<Integer> epSub = new ArrayList<>();
        Subtask subtask1 = new Subtask("epic1", "description1", TaskStatus.NEW, 1);
        Subtask expectedSubtask1 = new Subtask(2, "epic1", "description1", TaskStatus.NEW);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("epic2", "description2", TaskStatus.NEW, 1);
        Subtask expectedSubtask2 = new Subtask(3, "epic1", "description1", TaskStatus.NEW);
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
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, 1);
        Subtask expectedSubtask = new Subtask(2, "sub1", "description1", TaskStatus.NEW);
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
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, 1);
        Subtask expectedSubtask = new Subtask(2, "sub1", "description1", TaskStatus.NEW);
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
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, 1);
        Subtask expectedSubtask = new Subtask(2, "sub1", "description1", TaskStatus.NEW);
        // do
        taskManager.addNewSubtask(subtask);
        // check
        assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteSubtask() {
        // prepare
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, 1);
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
        Epic epic = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        Subtask subtask = new Subtask("sub1", "description1", TaskStatus.NEW, 1);
        Subtask updatedSubtask = new Subtask(2, "sub2", "description2", TaskStatus.NEW);
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
        Task task1 = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task1);
        Task task2 = new Task("task2", "description2", TaskStatus.NEW);
        taskManager.addNewTask(task2);
        Task task3 = new Task("task3", "description3", TaskStatus.NEW);
        taskManager.addNewTask(task3);
        // do
        taskManager.deleteAllTasks();
        // check
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteAllEpics() {
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW);
        taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "description2", TaskStatus.NEW);
        taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("epic3", "description3", TaskStatus.NEW);
        taskManager.addNewEpic(epic3);
        // do
        taskManager.deleteAllEpics();
        // check
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubtasks().isEmpty());

    }

    @Test
    void deleteSubtasks() {
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW);
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "description1", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "description1", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "description1", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask3);
        // do
        taskManager.deleteSubtasks();
        // check
        assertTrue(taskManager.getSubtasks().isEmpty());
        assertTrue(taskManager.getEpic(1).getSubtaskId().isEmpty());
    }

    @Test
    void getTasks() {
        Task task1 = new Task("task1", "description1", TaskStatus.NEW);
        taskManager.addNewTask(task1);
        Task task2 = new Task("task2", "description2", TaskStatus.NEW);
        taskManager.addNewTask(task2);
        Task task3 = new Task("task3", "description3", TaskStatus.NEW);
        taskManager.addNewTask(task3);
        Task expectedTask1 = new Task(1, "task1", "description1", TaskStatus.NEW);
        Task expectedTask2 = new Task(2, "task2", "description2", TaskStatus.NEW);
        Task expectedTask3 = new Task(3, "task3", "description3", TaskStatus.NEW);
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
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW);
        taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("epic2", "description2", TaskStatus.NEW);
        taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("epic3", "description3", TaskStatus.NEW);
        taskManager.addNewEpic(epic3);
        Epic expectedEpic1 = new Epic(1, "epic1", "description1", TaskStatus.NEW);
        Epic expectedEpic2 = new Epic(2, "epic2", "description2", TaskStatus.NEW);
        Epic expectedEpic3 = new Epic(3, "epic3", "description3", TaskStatus.NEW);
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
        Epic epic1 = new Epic("epic1", "description1", TaskStatus.NEW);
        taskManager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1", "description1", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "description2", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "description3", TaskStatus.NEW, 1);
        taskManager.addNewSubtask(subtask3);
        Subtask expectedSubtask1 = new Subtask(2, "subtask1", "description1", TaskStatus.NEW);
        Subtask expectedSubtask2 = new Subtask(3, "subtask2", "description2", TaskStatus.NEW);
        Subtask expectedSubtask3 = new Subtask(4, "subtask3", "description3", TaskStatus.NEW);
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