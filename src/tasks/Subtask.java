package tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {

    protected int epicId;
    protected final TaskType type = TaskType.SUBTASK;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public Subtask(Integer id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, TaskStatus status, LocalDateTime startTime, int duration) {
        super(id, name, description, status, startTime, duration);
    }

    public Subtask(Integer id, String name, String description, TaskStatus status, LocalDateTime startTime, int duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return type;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s", id, type, name, status, description, startTime, duration.toMinutes(), epicId);
    }
}
