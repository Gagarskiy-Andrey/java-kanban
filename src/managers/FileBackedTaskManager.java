package managers;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TaskType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

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

    private void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            if (!getTasks().isEmpty() || !getSubtasks().isEmpty() || !getEpics().isEmpty()) {
                fileWriter.write("ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC\n");
                for (Task task : getTasks()) {
                    fileWriter.write(task.toString() + "\n");
                }
                for (Task epic : getEpics()) {
                    fileWriter.write(epic.toString() + "\n");
                }
                for (Task subtask : getSubtasks()) {
                    fileWriter.write(subtask.toString() + "\n");
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
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


        if (taskType.equals(TaskType.TASK)) {
            return new Task(id, name, description, status);
        } else if (taskType.equals(TaskType.EPIC)) {
            return new Epic(id, name, description, status);
        } else if (taskType.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(newTask[5]);
            return new Subtask(id, name, description, status, epicId);
        } else {
            return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadException {

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

                    if (task.getType().equals(TaskType.TASK)) {
                        tasks.put(task.getId(), task);
                    } else if (task.getType().equals(TaskType.EPIC)) {
                        epics.put(task.getId(), (Epic) task);
                    } else if (task.getType().equals(TaskType.SUBTASK)) {
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
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = super.deleteTask(id);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
        return task;
    }

    @Override
    public Epic addNewEpic(Epic newEpic) {
        Epic epic = super.addNewEpic(newEpic);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = super.deleteEpic(id);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
        return epic;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        Integer id = super.addNewSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
        return id;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        try {
            save();
        } catch (ManagerSaveException exception) {
            exception.printStackTrace();
        }
    }
}
