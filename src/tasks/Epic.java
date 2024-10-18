package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    protected List<Integer> subtaskId = new ArrayList<>();

    protected final TaskType type = TaskType.EPIC;

    public Epic(String name, String description, TaskStatus status) {

        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    @Override
    public TaskType getType() {
        return type;
    }

    public List<Integer> getSubtaskId() {

        return subtaskId;
    }

    public void setSubtaskId(Integer subId) {
        subtaskId.add(subId);
    }

    public void removeSubtask(Integer id) {
        subtaskId.remove(id);
    }

    public void cleanSubtaskIds() {
        subtaskId.clear();
    }


    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s", id, type, name, status, description);
    }

}
