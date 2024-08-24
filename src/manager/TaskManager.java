package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Task addNewTask(Task newTask) {
        int newId = generateNewId();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    public void updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
    }

    public Task deleteTask(Integer id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        return task;
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Epic addNewEpic(Epic newEpic) {
        int newId = generateNewId();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public Epic deleteEpic(Integer id) {
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            epics.remove(id);

            if (epic.getSubtaskId() != null) {
                for (Integer subtaskId : epic.getSubtaskId()) {
                    subtasks.remove(subtaskId);
                }
            }
            return epic;
        }
        return null;
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtaskId()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Integer addNewSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = generateNewId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.setSubtaskId(subtask.getId());
        updateEpicStatus(epicId);
        return id;
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtasks.get(id).getEpicId();
        subtask.setEpicId(epicId);
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicId);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        deleteSubtasks();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    private int generateNewId() {
        return id++;
    }

    private void updateEpicStatus(int epicId) {
        int countNew = 0;
        int countDone = 0;
        Epic epic = epics.get(epicId);
        if (epic.getSubtaskId() != null) {
            for (Integer id : epic.getSubtaskId()) {
                Subtask subtask = subtasks.get(id);
                if (subtask.getStatus() == TaskStatus.NEW) {
                    countNew++;
                } else if (subtask.getStatus() == TaskStatus.DONE) {
                    countDone++;
                }
            }
            if (countNew == epic.getSubtaskId().size()) {
                epics.get(epicId).setStatus(TaskStatus.NEW);
            } else if (countDone == epic.getSubtaskId().size()) {
                epics.get(epicId).setStatus(TaskStatus.DONE);
            } else {
                epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

}
