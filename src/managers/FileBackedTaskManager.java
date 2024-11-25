package managers;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TaskType;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String HEADER = "ID,TYPE,NAME,STATUS,DESCRIPTION,START_TIME,DURATION,EPIC";
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public FileBackedTaskManager(File file, Map<Integer, Task> tasks,
                                 Map<Integer, Epic> epics,
                                 Map<Integer, Subtask> subtasks,
                                 int id) {
        this.file = file;
        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
        this.id = id;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!getTasks().isEmpty() || !getSubtasks().isEmpty() || !getEpics().isEmpty()) {
                writer.write(HEADER);
                writer.newLine();
                for (Task task : getTasks()) {
                    writer.write(toString(task));
                    writer.newLine();
                }
                for (Task epic : getEpics()) {
                    writer.write(toString(epic));
                    writer.newLine();
                }
                for (Task subtask : getSubtasks()) {
                    writer.write(toString(subtask));
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), exception);
        }
    }

    private static Task fromString(String lane) {
        String[] newTask = lane.split(",");
        int id = Integer.parseInt(newTask[0]);
        String tType = newTask[1];
        TaskType taskType = TaskType.valueOf(tType);
        String name = newTask[2];
        String tStatus = newTask[3];
        TaskStatus status = TaskStatus.valueOf(tStatus);
        String description = newTask[4];
        LocalDateTime startTime;
        Integer duration;
        if (newTask[5].equals("null")) {
            startTime = null;
            duration = null;
        } else {
            startTime = LocalDateTime.parse(newTask[5]);
            duration = Integer.parseInt(newTask[6]);
        }
        if (taskType.equals(TaskType.TASK)) {
            return new Task(id, name, description, status, startTime, duration);
        } else if (taskType.equals(TaskType.EPIC)) {
            if (Objects.isNull(startTime)) {
                return new Epic(id, name, description, status, null, null);
            } else {
                return new Epic(id, name, description, status, startTime, Duration.ofMinutes(duration));
            }
        } else if (taskType.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(newTask[7]);
            return new Subtask(id, name, description, status, startTime, duration, epicId);
        } else {
            return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        Map<Integer, Task> tasks = new HashMap<>();
        Map<Integer, Subtask> subtasks = new HashMap<>();
        Map<Integer, Epic> epics = new HashMap<>();
        int id = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Task task = null;
            while (br.ready()) {
                String taskLine = br.readLine();
                if (!taskLine.startsWith("ID")) {
                    task = fromString(taskLine);
                }
                if (task != null) {
                    id = Math.max(id, task.getId());
                    if (task.getPrototype().equals(TaskType.TASK)) {
                        tasks.put(task.getId(), task);
                    } else if (task.getPrototype().equals(TaskType.EPIC)) {
                        epics.put(task.getId(), (Epic) task);
                    } else if (task.getPrototype().equals(TaskType.SUBTASK)) {
                        subtasks.put(task.getId(), (Subtask) task);
                    }
                }
            }
            for (Subtask sub : subtasks.values()) {
                Epic epic = epics.get(sub.getEpicId());
                epic.setSubtaskId(sub.getId());
            }
        } catch (IOException exception) {
            throw new ManagerLoadException(exception.getMessage());
        }
        return new FileBackedTaskManager(file, tasks, epics, subtasks, id);
    }

    @Override
    public Task addNewTask(Task newTask) {
        Task task = super.addNewTask(newTask);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = super.deleteTask(id);
        save();
        return task;
    }

    @Override
    public Epic addNewEpic(Epic newEpic) {
        Epic epic = super.addNewEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = super.deleteEpic(id);
        save();
        return epic;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Integer id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    public static String toString(Task task) {
        if (Objects.isNull(task.getStartTime())) {
            return task.getId() + "," + task.getPrototype() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + "null" + "," + "null";
        }
        return task.getId() + "," + task.getPrototype() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration().toMinutes() + "," + (task.getPrototype().equals(TaskType.SUBTASK) ? ((Subtask) task).getEpicId() : "");
    }
}
