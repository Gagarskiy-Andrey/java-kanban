package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    private int id = 1;

    public InMemoryTaskManager() {
        this.historyManager = Manager.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTask(int id) {
        final Task task = tasks.get(id);
        historyManager.add(task);
        return task;
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
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.put(id, task);
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        historyManager.remove(id);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
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
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        for (Integer subId : savedEpic.getSubtaskId()) {
            epic.setSubtaskId(subId);
        }
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }

    @Override
    public Epic deleteEpic(Integer id) {
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            epics.remove(id);
            historyManager.remove(id);

            if (epic.getSubtaskId() != null) {
                for (Integer subtaskId : epic.getSubtaskId()) {
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                }
            }
            return epic;
        }
        return null;
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
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
        historyManager.remove(id);
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
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        deleteSubtasks();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    @Override
    public List<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {

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
