package Tasks;

public class Subtask extends Task{

    protected int epicId;
    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}
