package Tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> idToTask = new HashMap<>();
    HashMap<Integer, Epic> idToEpic = new HashMap<>();
    HashMap<Integer, Subtask> idToSubtask = new HashMap<>();
    static int id = 1;

    private  int generateNewId() {
        return id++;
    }

    public Task getTaskById(Integer id) {
        if (idToTask.containsKey(id)) {
            return idToTask.get(id);
        } else {
            System.out.println("Искомая задача отсутствует");
            return null;
        }
    }

    public Task addNewTask(Task newTask) {
        int newId = generateNewId();
        newTask.setId(newId);
        idToTask.put(newTask.getId(), newTask);
        return newTask;
    }

    public Task updateTask(Task updatedTask) {

        Integer taskId = updatedTask.getId();
        if (idToTask.containsKey(taskId)) {
        idToTask.put(taskId, updatedTask);
            return updatedTask;
        } else {
            System.out.println("Задачи с таким id не существует!");
            return null;
        }

}

public Task deleteTask(Integer id) {
      Task task = idToTask.get(id);
      idToTask.remove(id);
      return task;
}

    public Epic getEpicById(Integer id) {
        if (idToEpic.containsKey(id)) {
            return idToEpic.get(id);
        } else {
            System.out.println("Искомая эпическая задача отсутствует");
            return null;
        }
    }
    public Epic addNewEpic(Epic newEpic) {
        int newId = generateNewId();
        newEpic.setId(newId);
        idToEpic.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public Epic updateEpic(Epic updatedEpic) {

        Integer epicId = updatedEpic.getId();
        if (idToEpic.containsKey(epicId)) {

            Epic oldEpic = idToEpic.get(epicId);
            ArrayList<Integer> oldEpicSubtasks = oldEpic.getSubtaskId();
            for (Integer id : oldEpicSubtasks) {
                idToSubtask.remove(id);
            }

            idToEpic.put(epicId, updatedEpic);
            return updatedEpic;
        } else {
            System.out.println("Эпической задачи с таким id не существует!");
            return null;
        }

    }

    public Epic deleteEpic(Integer id) {
        Epic epic = idToEpic.get(id);
        idToEpic.remove(id);

        if (epic.getSubtaskId() != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                idToSubtask.remove(subtaskId);
            }
        }
        return epic;
    }

    public ArrayList<Subtask> getAllSubtasksByEpic (Integer epicId) {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();

        if (idToEpic.containsKey(epicId)) {
        Epic epic = idToEpic.get(epicId);
        if (epic.getSubtaskId() != null) {
            for (Integer id : epic.getSubtaskId()) {
                Subtask subtask = idToSubtask.get(id);
                allSubtasks.add(subtask);
                }
            return allSubtasks;
            } else {
            System.out.println("Подзадач нет");
            return null;
        }
        } else {
            System.out.println("Не существующая эпическая задача");
            return null;
        }
    }
    public Subtask getSubtaskById(Integer id) {
        if (idToSubtask.containsKey(id)) {
            return idToSubtask.get(id);
        } else {
            System.out.println("Искомая подзадача отсутствует");
            return null;
        }
    }
    public Subtask addNewSubtask(Subtask newSubtask, Integer newEpicId) {
        Integer newId = generateNewId();
        newSubtask.setId(newId);
        newSubtask.setEpicId(newEpicId);
        Epic epic = idToEpic.get(newEpicId);
        ArrayList<Integer> epicSubtasks = epic.getSubtaskId();
        epicSubtasks.add(newId);
        epic.setSubtaskId(epicSubtasks);
        idToSubtask.put(newSubtask.getId(), newSubtask);

        if (epic.getSubtaskId() != null) {
            for (Integer id : epic.getSubtaskId()) {
                Subtask subtask = idToSubtask.get(id);
                if (subtask.getStatus() == newSubtask.getStatus()) {
                    epic.setStatus(newSubtask.getStatus());
                } else {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    break;
                }
            }
        }

        idToEpic.put(newEpicId, epic);
        return newSubtask;
    }

    public Subtask deleteSubtask(Integer id) {
        Subtask subtask = idToSubtask.get(id);
        idToSubtask.remove(id);
        Epic epic = idToEpic.get(subtask.getEpicId());

        if (epic.getSubtaskId() != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                Subtask safeSubtask = idToSubtask.get(subtaskId);
                if (safeSubtask.getStatus() != epic.getStatus()) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    break;
                }
            }
        }
        return subtask;
    }

    public Subtask updateSubtask(Subtask updatedSubtask) {

        Integer epicId = updatedSubtask.getId();
        if (idToSubtask.containsKey(epicId)) {

            Subtask oldSubtask = idToSubtask.get(epicId);
            int oldSubtaskEpicId = oldSubtask.getEpicId();
            updatedSubtask.setEpicId(oldSubtaskEpicId);
            idToSubtask.put(epicId, updatedSubtask);

            Epic epic = idToEpic.get(oldSubtaskEpicId);
            if (epic.getSubtaskId() != null) {
                for (Integer id : epic.getSubtaskId()) {
                    Subtask subtask = idToSubtask.get(id);
                    if (subtask.getStatus() == updatedSubtask.getStatus()) {
                        epic.setStatus(updatedSubtask.getStatus());
                    } else {
                        epic.setStatus(TaskStatus.IN_PROGRESS);
                        break;
                    }
                }
            }
            idToEpic.put(oldSubtaskEpicId, epic);
            return updatedSubtask;
        } else {
            System.out.println("Подзадачи с таким id не существует!");
            return null;
        }
    }

    public void deleteAllTasks () {
        idToTask.clear();
    }

    public void deleteAllEpics () {
        idToEpic.clear();
        deleteAllSubtasks();
    }
    public void deleteAllSubtasks () {
        idToSubtask.clear();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!idToTask.isEmpty()) {
            for (Task task : idToTask.values()) {
                tasks.add(task);
            }
            return tasks;
        } else {
            System.out.println("Список пуст");
            return null;
        }
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epics = new ArrayList<>();
        if (!idToEpic.isEmpty()) {
            for (Epic epic : idToEpic.values()) {
                epics.add(epic);
            }
            return epics;
        } else {
            System.out.println("Список пуст");
            return null;
        }
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (!idToSubtask.isEmpty()) {
            for (Subtask subtask : idToSubtask.values()) {
                subtasks.add(subtask);
            }
            return subtasks;
        } else {
            System.out.println("Список пуст");
            return null;
        }
    }


}
