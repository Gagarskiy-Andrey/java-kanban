package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected List<Integer> subtaskId = new ArrayList<>();
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime endTime;
    protected final TaskType type = TaskType.EPIC;

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

    public void setStartTime(LocalDateTime time) {
        this.startTime = time;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        if (Objects.isNull(startTime)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, type, name, status, description, null, null);
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s", id, type, name, status, description, startTime, duration.toMinutes());
        }
    }
}


