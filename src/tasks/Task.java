package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected final TaskType type = TaskType.TASK;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(Integer id, String name, String description, TaskStatus status) { //для проверки обновления задачи по id
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(Integer id, String name, String description, TaskStatus status, LocalDateTime startTime, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public TaskType getPrototype() {
        return type;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public TaskStatus getStatus() {

        return status;
    }

    public void setStatus(TaskStatus status) {

        this.status = status;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, type, name, status, description, startTime, duration.toMinutes());
    }
}


