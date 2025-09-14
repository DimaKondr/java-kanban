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

    //Получаем ID задачи.
    public int getTaskID() {
        return taskID;
    }

    //Задаем ID задачи.
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    //Получаем статус задачи.
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    //Задаем статус задачи.
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Task otherTask = (Task) o;
        return Objects.equals(taskID, otherTask.taskID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskID);
    }

    @Override
    public String toString() {
        return "Task{title='" + title + "', description='" + description + "', taskStatus='"
                + taskStatus + "', taskID='" + taskID + "'}";
    }
}