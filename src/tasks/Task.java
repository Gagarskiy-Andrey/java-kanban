package tasks;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;

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
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}


