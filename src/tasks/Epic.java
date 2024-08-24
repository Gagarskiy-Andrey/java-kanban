package tasks;

import java.util.ArrayList;
public class Epic extends Task {

    protected ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {

        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubtaskId() {

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
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}
