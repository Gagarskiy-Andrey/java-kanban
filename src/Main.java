import manager.TaskManager;
import tasks.*;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task newTask1 = new Task( "Заголовок1", "Описание1", TaskStatus.NEW); // Для проверки создали задачу с параметрами
        taskManager.addNewTask(newTask1);
        Task newTask2 = new Task( "Заголовок2", "Описание2", TaskStatus.NEW);
        taskManager.addNewTask(newTask2);
        Epic newEpic1 = new Epic("Эпик1", "Описание1", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic1);
        Epic newEpic2 = new Epic("Эпик2", "Описание2", TaskStatus.NEW);
        taskManager.addNewEpic(newEpic2);
        Subtask newSubtask1 = new Subtask( "Подзадача1", "Описание1", TaskStatus.NEW, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask1);
        Subtask newSubtask2 = new Subtask( "Подзадача2", "Описание2", TaskStatus.NEW, newEpic1.getId());
        taskManager.addNewSubtask(newSubtask2);
        Subtask newSubtask3 = new Subtask( "Подзадача3", "Описание3", TaskStatus.NEW, newEpic2.getId());
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


    }
}
