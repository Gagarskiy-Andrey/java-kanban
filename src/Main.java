import exceptions.ManagerLoadException;
import managers.FileBackedTaskManager;
import managers.Manager;
import managers.TaskManager;
import tasks.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ManagerLoadException {
        System.out.println("Поехали!");

        TaskManager taskManager = Manager.getDefault();
        File file = new File("/Users/andrei/clone/java-kanban/data.csv");
        //TaskManager tManager = Manager.getFileBackedTaskManager(file);

        Task newTask1 = new Task("Заголовок1", "Описание1", TaskStatus.NEW); // Для проверки создали задачу с параметрами
        taskManager.addNewTask(newTask1);
        Task newTask2 = new Task("Заголовок2", "Описание2", TaskStatus.NEW);
        taskManager.addNewTask(newTask2);
        Epic newEpic1 = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic1);
        Epic newEpic2 = new Epic("Эпик2", "Описание2", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic2);
        Subtask newSubtask1 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask1);
        Subtask newSubtask2 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask2);
        Subtask newSubtask3 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, newEpic2.getId());
        taskManager.addNewSubtask(newSubtask3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("_____________________________________________________________");

        Subtask updatedSubTask1 = new Subtask(7, "Подзадача4", "Описание4", TaskStatus.DONE);
        taskManager.updateSubtask(updatedSubTask1);
        Subtask updatedSubTask2 = new Subtask(5, "Подзадача5", "Описание5", TaskStatus.DONE);
        taskManager.updateSubtask(updatedSubTask2);

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

        Task newTask3 = new Task("Заголовок3", "Описание3", TaskStatus.NEW); // Для проверки создали задачу с параметрами
        taskManager.addNewTask(newTask3);
        Task newTask4 = new Task("Заголовок4", "Описание4", TaskStatus.NEW);
        taskManager.addNewTask(newTask4);
        Epic newEpic3 = new Epic("Эпик3", "Описание3", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic3);
        Epic newEpic4 = new Epic("Эпик4", "Описание4", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic4);
        Epic newEpic5 = new Epic("Эпик5", "Описание5", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic5);
        Subtask newSubtask6 = new Subtask("Подзадача6", "Описание6", TaskStatus.NEW, newEpic3.getId());
        taskManager.addNewSubtask(newSubtask6);
        Subtask newSubtask7 = new Subtask("Подзадача7", "Описание7", TaskStatus.NEW, newEpic3.getId());
        taskManager.addNewSubtask(newSubtask7);
        Subtask newSubtask8 = new Subtask("Подзадача8", "Описание8", TaskStatus.NEW, newEpic4.getId());
        taskManager.addNewSubtask(newSubtask8);
        Subtask newSubtask9 = new Subtask("Подзадача9", "Описание9", TaskStatus.NEW, newEpic5.getId());
        taskManager.addNewSubtask(newSubtask9);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println("_____________________________________________________________");

        taskManager.getTask(1);
        taskManager.getTask(8);
        taskManager.getTask(9);
        taskManager.getEpic(10);
        taskManager.getEpic(11);
        taskManager.getEpic(12);
        taskManager.getSubtask(13);
        taskManager.getSubtask(14);
        taskManager.getSubtask(15);
        taskManager.getSubtask(16);

        System.out.println(taskManager.getHistory());
        System.out.println("_____________________________________________________________");

        taskManager.getTask(1);

        System.out.println(taskManager.getHistory());
        System.out.println("_____________________________________________________________");

        //Task newTask11 = new Task("Заголовок1", "Описание1", TaskStatus.NEW);
        //tManager.addNewTask(newTask11);
        //Task newTask22 = new Task("Заголовок2", "Описание2", TaskStatus.NEW);
        //tManager.addNewTask(newTask22);
        //Epic newEpic11 = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        //tManager.addNewEpic(newEpic11);
        //Epic newEpic22 = new Epic("Эпик2", "Описание2", TaskStatus.NEW);
        //tManager.addNewEpic(newEpic22);
        //Subtask newSubtask11 = new Subtask("Подзадача1", "Описание1", TaskStatus.NEW, newEpic1.getId());
        //tManager.addNewSubtask(newSubtask11);
        //Subtask newSubtask22 = new Subtask("Подзадача2", "Описание2", TaskStatus.NEW, newEpic1.getId());
        //tManager.addNewSubtask(newSubtask22);
        //Subtask newSubtask33 = new Subtask("Подзадача3", "Описание3", TaskStatus.NEW, newEpic2.getId());
        //tManager.addNewSubtask(newSubtask33);

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

    }
}
