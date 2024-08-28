package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;
    private int id = 1;

    public InMemoryTaskManager() {
        this.historyManager = Manager.getDefaultHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task addNewTask(Task newTask) {
        int newId = generateNewId();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public void updateTask(Task task) {
        Task savedTask = tasks.get(task.getId());
        if (savedTask == null) {
            return;
        }
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Epic addNewEpic(Epic newEpic) {
        int newId = generateNewId();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        for (Integer sId : epic.getSubtaskId()) {
            savedEpic.setSubtaskId(sId);
        }
    }

    @Override
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

    @Override
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

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
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

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());
    }

    @Override
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

    @Override
    public void deleteAllTasks() {

        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        deleteSubtasks();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    @Override
    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }

    @Override
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
