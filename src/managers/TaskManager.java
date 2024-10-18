package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    Task getTask(int id);

    Task addNewTask(Task newTask);

    void updateTask(Task task);

    Task deleteTask(Integer id);

    Epic getEpic(int id);

    Epic addNewEpic(Epic newEpic);

    void updateEpic(Epic epic);

    Epic deleteEpic(Integer id);

    List<Subtask> getEpicSubtasks(int epicId);

    Subtask getSubtask(int id);

    Integer addNewSubtask(Subtask subtask);

    void deleteSubtask(int id);

    void updateSubtask(Subtask subtask);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteSubtasks();

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();
}
