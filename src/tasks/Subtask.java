package tasks;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public int getEpicId() {

        return epicId;
    }

    public void setEpicId(int epicId) {

        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ",epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}
