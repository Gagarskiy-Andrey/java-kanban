import exceptions.ManagerLoadException;
import managers.FileBackedTaskManager;
import managers.Manager;
import managers.TaskManager;
import tasks.*;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws ManagerLoadException {
        System.out.println("Поехали!");

        TaskManager taskManager = Manager.getDefault();
        File file = new File("/Users/andrei/clone/java-kanban/data.csv");
        TaskManager tManager = Manager.getFileBackedTaskManager(file);

        Task newTask1 = new Task("Заголовок1", "Описание1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 10), 10); // Для проверки создали задачу с параметрами
        taskManager.addNewTask(newTask1);
        Task newTask2 = new Task("Заголовок2", "Описание2", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 20), 10);
        taskManager.addNewTask(newTask2);
        Epic newEpic1 = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic1);
        Epic newEpic2 = new Epic("Эпик2", "Описание2", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 50), 10);
        taskManager.addNewEpic(newEpic2);
        Subtask newSubtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 30), 10, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask1);
        Subtask newSubtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 40), 10, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask2);
        Subtask newSubtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 16, 50), 10, newEpic2.getId());
        taskManager.addNewSubtask(newSubtask3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("_____________________________________________________________");

        Task updateTask1 = new Task(1, "Заголовок1", "Описание1", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 11, 4, 15, 10), 10);
        taskManager.updateTask(updateTask1);
        Subtask updateSubtask3 = new Subtask(7, "Подзадача3", "Описание3", TaskStatus.DONE, LocalDateTime.of(2024, 11, 4, 16, 50), 20);
        taskManager.updateSubtask(updateSubtask3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("_____________________________________________________________");

        taskManager.deleteTask(2);
        taskManager.deleteEpic(4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("_____________________________________________________________");

        System.out.println(taskManager.getEpicSubtasks(3));
        System.out.println("_____________________________________________________________");

        taskManager.deleteAllEpics();

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("_____________________________________________________________");

        Task newTask11 = new Task("Заголовок1", "Описание1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 18, 0), 10);
        tManager.addNewTask(newTask11);
        Task newTask22 = new Task("Заголовок2", "Описание2", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 18, 10), 10);
        tManager.addNewTask(newTask22);
        Epic newEpic11 = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        tManager.addNewEpic(newEpic11);
        Epic newEpic22 = new Epic("Эпик2", "Описание2", TaskStatus.NEW);
        tManager.addNewEpic(newEpic22);
        Subtask newSubtask11 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 18, 20), 10, newEpic1.getId());
        tManager.addNewSubtask(newSubtask11);
        Subtask newSubtask22 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 18, 30), 10, newEpic1.getId());
        tManager.addNewSubtask(newSubtask22);
        Subtask newSubtask33 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, LocalDateTime.of(2024, 11, 4, 18, 40), 10, newEpic2.getId());
        tManager.addNewSubtask(newSubtask33);

        FileBackedTaskManager tManager2 = Manager.getFileBackedTaskManager(file);
        FileBackedTaskManager tManager1 = tManager2.loadFromFile(file);
        tManager1.getTask(1);
        tManager1.getTask(2);
        tManager1.getEpic(3);
        tManager1.getEpic(4);
        tManager1.getSubtask(5);
        tManager1.getSubtask(6);
        tManager1.getSubtask(7);

        System.out.println(tManager1.getHistory());
        System.out.println("_____________________________________________________________");

        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(tManager.getPrioritizedTasks());
    }
}
