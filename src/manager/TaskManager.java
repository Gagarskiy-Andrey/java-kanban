package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    HistoryManager getHistoryManager();
    Task getTask(int id);

    Task addNewTask(Task newTask);

    void updateTask(Task task);

    Task deleteTask(Integer id);

    Epic getEpic(int id);

    Epic addNewEpic(Epic newEpic);

    void updateEpic(Epic epic);

    Epic deleteEpic(Integer id);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    Subtask getSubtask(int id);

    Integer addNewSubtask(Subtask subtask);

    void deleteSubtask(int id);

    void updateSubtask(Subtask subtask);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteSubtasks();

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();
}
