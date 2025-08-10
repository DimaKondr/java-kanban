import java.util.Objects;

public class Task {
    public String title; //Здесь храним название задачи.
    public String description; //Здесь храним описание задачи.
    public TaskStatus taskStatus; //Здесь храним статус задачи.
    public int taskID; //Здесь храним ID задачи.

    public Task(String title, String description, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Task otherTask = (Task) o;
        return Objects.equals(title, otherTask.title) && Objects.equals(description, otherTask.description)
                && taskStatus == otherTask.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{title='" + title + "', description='" + description + "', taskStatus='"
                + taskStatus + "', taskID='" + taskID + "'}";
    }
}