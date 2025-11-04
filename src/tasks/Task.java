package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    public String title; //Здесь храним название задачи.
    public String description; //Здесь храним описание задачи.
    public TaskStatus taskStatus; //Здесь храним статус задачи.
    public TaskType taskType; //Здесь храним тип задачи.
    public int taskID; //Здесь храним ID задачи.
    public Duration duration; //Здесь храним длительность выполнения задачи.
    public LocalDateTime startTime;//Здесь храним время начала выполнения задачи.

    //Конструктор для создания новых объектов.
    public Task(String title, String description, TaskStatus taskStatus, Long duration) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.taskType = TaskType.TASK;
        this.duration = Duration.ofMinutes(duration);
    }

    //Конструктор для создания копии объекта.
    public Task(Task taskForCopy) {
        this.title = taskForCopy.title;
        this.description = taskForCopy.description;
        this.taskStatus = taskForCopy.taskStatus;
        this.taskID = taskForCopy.taskID;
        this.taskType = taskForCopy.taskType;
        this.duration = taskForCopy.duration;
        this.startTime = taskForCopy.startTime;
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

    //Задаем тип задачи.
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    //Получаем время начала выполнения задачи
    public LocalDateTime getStartTime() {
        return startTime;
    }

    //Задаем время начала выполнения задачи
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    //Получаем длительность выполнения задачи
    public Duration getDuration() {
        return duration;
    }

    //Задаем длительность выполнения задачи
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    //Переводим задачу в строку для сохранения в файл.
    public String toStringForFile(Task taskToString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");
        String formatedStartTime = taskToString.getStartTime()
                == null ? "-" : formatter.format(taskToString.getStartTime());
        return taskToString.taskID + "," + taskToString.taskType + "," + taskToString.title + ","
                + taskToString.taskStatus + "," + taskToString.description + "," + "-" + ","
                + taskToString.getDuration().toMinutes() + "," + formatedStartTime + "," + "-";
    }

    //Создаем задачу из полученной строки.
    public static Task createTaskFromFile(String dataFromFile) {
        String[] splitTask = dataFromFile.split(",");
        int taskID = Integer.parseInt(splitTask[0]);
        TaskType taskType = TaskType.valueOf(splitTask[1]);
        String title = splitTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(splitTask[3]);
        String description = splitTask[4];
        Long duration = Long.parseLong(splitTask[6]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");
        LocalDateTime startTime = splitTask[7].equals("-") ? null : LocalDateTime.parse(splitTask[7], formatter);

        Task task = new Task(title, description, taskStatus, duration);
        task.setTaskType(taskType);
        task.setTaskID(taskID);
        task.setStartTime(startTime);

        return task;
    }

    //Получаем время завершения выполнения задачи
    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
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
        return "tasks.Task{title='" + title + "', description='" + description + "', taskStatus='"
                + taskStatus + "', taskID='" + taskID + "', duration='"
                + duration + "', startTime='" + startTime + "', endTime='" + getEndTime() + "'}";
    }

}