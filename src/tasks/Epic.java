package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected final List<Integer> subtaskId = new ArrayList<>();
    protected LocalDateTime endTime;
    protected final TaskType prototype = TaskType.EPIC;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        super.startTime = null;
        super.duration = null;
        this.endTime = null;
    }

    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        super.startTime = null;
        super.duration = null;
        this.endTime = null;
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime startTime, Integer duration) {
        super(name, description, status, startTime, duration);
    }

    public Epic(Integer id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status);
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public TaskType getPrototype() {
        return prototype;
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
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        if (Objects.isNull(startTime)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, prototype, name, status, description, null, null);
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, prototype, name, status, description, startTime, duration.toMinutes());
        }
    }
}


