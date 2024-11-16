package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected HistoryManager historyManager;
    protected int id = 1;

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
        if (toBeOrNotToBeAdded(newTask)) {
            int newId = generateNewId();
            newTask.setId(newId);
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);
            return newTask;
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        final int id = task.getId();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        prioritizedTasks.remove(savedTask);
        if (toBeOrNotToBeAdded(task)) {
            tasks.put(id, task);
            prioritizedTasks.add(task);
        } else {
            prioritizedTasks.add(savedTask);
        }
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = tasks.get(id);
        tasks.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(task);
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
        epic.setStartTime(savedEpic.getStartTime());
        epic.setDuration(savedEpic.getDuration());
        epic.setEndTime(savedEpic.getEndTime());
        epics.put(epic.getId(), epic);
    }

    @Override
    public Epic deleteEpic(Integer id) {
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            epics.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(epic);

            if (epic.getSubtaskId() != null) {
                epic.getSubtaskId().stream().forEach(subtaskId -> {
                    subtasks.remove(subtaskId);
                    historyManager.remove(subtaskId);
                });
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
        epic.getSubtaskId().stream().forEach(id -> tasks.add(subtasks.get(id)));
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
        if (toBeOrNotToBeAdded(subtask)) {
            int id = generateNewId();
            subtask.setId(id);
            subtasks.put(id, subtask);
            epic.setSubtaskId(subtask.getId());
            updateEpicStatus(epicId);
            updateEpicTimeFrame(epicId);
            prioritizedTasks.add(subtask);
            return id;
        } else {
            return null;
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(subtask);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());
        updateEpicTimeFrame(epic.getId());
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
        prioritizedTasks.remove(savedSubtask);
        if (toBeOrNotToBeAdded(subtask)) {
            subtasks.put(id, subtask);
            updateEpicStatus(epicId);
            updateEpicTimeFrame(epicId);
            prioritizedTasks.add(subtask);
        } else {
            prioritizedTasks.add(savedSubtask);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().stream().forEach(id -> {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        });
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().stream().forEach(id -> historyManager.remove(id));
        epics.clear();
        deleteSubtasks();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.keySet().stream().forEach(id -> {
            historyManager.remove(id);
            prioritizedTasks.remove(subtasks.get(id));
        });
        epics.values().stream().forEach(epic -> {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
            prioritizedTasks.add(epic);
        });
        subtasks.clear();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
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
        Epic epic = epics.get(epicId);
        if (epic.getSubtaskId() != null) {
            List<Subtask> subtaskNew = epic.getSubtaskId().stream().map(id -> subtasks.get(id)).filter(subtask -> subtask.getStatus() == TaskStatus.NEW).collect(Collectors.toList());
            List<Subtask> subtaskDone = epic.getSubtaskId().stream().map(id -> subtasks.get(id)).filter(subtask -> subtask.getStatus() == TaskStatus.DONE).collect(Collectors.toList());
            if (subtaskNew.size() == epic.getSubtaskId().size()) {
                epics.get(epicId).setStatus(TaskStatus.NEW);
            } else if (subtaskDone.size() == epic.getSubtaskId().size()) {
                epics.get(epicId).setStatus(TaskStatus.DONE);
            } else {
                epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    private void updateEpicTimeFrame(int epicId) {
        LocalDateTime startTime = null;
        Duration duration = null;
        LocalDateTime endTime = null;
        Epic epic = epics.get(epicId);
        for (Integer id : epic.getSubtaskId()) {
            Subtask subtask = subtasks.get(id);
            if (startTime != null) {
                if (startTime.isAfter(subtask.getStartTime())) {
                    startTime = subtask.getStartTime();
                }
                duration = duration.plus(subtask.getDuration());
                if (endTime.isBefore(subtask.getEndTime())) {
                    endTime = subtask.getEndTime();
                }
            } else {
                startTime = subtask.getStartTime();
                duration = subtask.getDuration();
                endTime = subtask.getEndTime();
            }
        }
        epics.get(epicId).setStartTime(startTime);
        epics.get(epicId).setDuration(duration);
        epics.get(epicId).setEndTime(endTime);
    }

    protected boolean checkForIntersections(Task first, Task second) {
        LocalDateTime fStartTime = first.getStartTime();
        LocalDateTime sStartTime = second.getStartTime();
        LocalDateTime fEndTime = first.getEndTime();
        LocalDateTime sEndTime = second.getEndTime();
        return (fStartTime.isBefore(sStartTime) && fEndTime.isAfter(sStartTime))
                || (fStartTime.isBefore(sEndTime) && fEndTime.isAfter(sEndTime))
                || (fStartTime.isBefore(sStartTime) && fEndTime.isAfter(sEndTime))
                || (fStartTime.isAfter(sStartTime) && fEndTime.isBefore(sEndTime))
                || fStartTime.equals(sStartTime)
                || fEndTime.equals(sEndTime);
    }

    protected boolean toBeOrNotToBeAdded(Task second) {
        if (getPrioritizedTasks().stream().noneMatch(first -> checkForIntersections(first, second))) {
            return true;
        }
        return false;
    }
}
