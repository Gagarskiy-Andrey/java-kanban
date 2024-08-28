package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Task;
import tasks.TaskStatus;

class InMemoryHistoryManagerTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void init() {
        taskManager = Manager.getDefault();
        historyManager = Manager.getDefaultHistory();
    }

    @Test
    void add() {
        // prepare
        Epic epic = new Epic("epic1", "description1", TaskStatus.NEW);
        // do
        historyManager.add(epic);
        // check
        assertNotNull(historyManager.getHistory());
    }

    @Test
    void add_savedSizeIfAddMoreThan10Tasks() {
        // prepare
        Task task1 = new Task("task1", "description1", TaskStatus.NEW);
        Task task2 = new Task("task2", "description2", TaskStatus.NEW);
        Task task3 = new Task("task3", "description3", TaskStatus.NEW);
        Task task4 = new Task("task4", "description4", TaskStatus.NEW);
        Task task5 = new Task("task5", "description5", TaskStatus.NEW);
        Task task6 = new Task("task6", "description6", TaskStatus.NEW);
        Task task7 = new Task("task7", "description7", TaskStatus.NEW);
        Task task8 = new Task("task8", "description8", TaskStatus.NEW);
        Task task9 = new Task("task9", "description9", TaskStatus.NEW);
        Task task10 = new Task("task10", "description10", TaskStatus.NEW);
        Task task11 = new Task("task11", "description11", TaskStatus.NEW);
        // do
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);
        historyManager.add(task9);
        historyManager.add(task10);
        historyManager.add(task11);
        // check
        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void add_removeFistTaskIfAddMoreThan10Tasks() {
        // prepare
        Task task1 = new Task(1, "task1", "description1", TaskStatus.NEW);
        Task task2 = new Task(2, "task2", "description2", TaskStatus.NEW);
        Task task3 = new Task(3, "task3", "description3", TaskStatus.NEW);
        Task task4 = new Task(4, "task4", "description4", TaskStatus.NEW);
        Task task5 = new Task(5, "task5", "description5", TaskStatus.NEW);
        Task task6 = new Task(6, "task6", "description6", TaskStatus.NEW);
        Task task7 = new Task(7, "task7", "description7", TaskStatus.NEW);
        Task task8 = new Task(8, "task8", "description8", TaskStatus.NEW);
        Task task9 = new Task(9, "task9", "description9", TaskStatus.NEW);
        Task task10 = new Task(10, "task10", "description10", TaskStatus.NEW);
        Task task11 = new Task(11, "task11", "description11", TaskStatus.NEW);
        // do
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(task7);
        historyManager.add(task8);
        historyManager.add(task9);
        historyManager.add(task10);
        historyManager.add(task11);
        // check
        assertFalse(historyManager.getHistory().contains(task1));
    }

    @Test
    void getHistory_getHistoryWhithVersionOfTaskBeforeChange() {
        // prepare
        Task task1 = new Task("task1", "description1", TaskStatus.NEW);
        Task task2 = new Task(1, "task2", "description2", TaskStatus.DONE);
        // do
        taskManager.addNewTask(task1);
        taskManager.getTask(1);
        taskManager.updateTask(task2);
        // check
        assertEquals(task1.getName(), taskManager.getHistory().get(0).getName());
        assertNotEquals(task2.getName(), taskManager.getHistory().get(0).getName());
    }
}