import java.util.Objects;

public class Task {
    public String title; //Здесь храним название задачи.
    public String description; //Здесь храним описание задачи.
    public TaskStatus taskStatus; //Здесь храним статус задачи.
    public TaskType taskType; //Здесь храним тип задачи.
    public int taskID; //Здесь храним ID задачи.

    //Конструктор для создания новых объектов.
    public Task(String title, String description, TaskStatus taskStatus) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.taskType = TaskType.TASK;
    }

    //Конструктор для создания копии объекта.
    public Task(Task taskForCopy) {
        this.title = taskForCopy.title;
        this.description = taskForCopy.description;
        this.taskStatus = taskForCopy.taskStatus;
        this.taskID = taskForCopy.taskID;
        this.taskType = taskForCopy.taskType;
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

    //Переводим задачу в строку для сохранения в файл.
    public String toStringForFile(Task taskToString) {
        return taskToString.taskID + "," + taskToString.taskType + "," + taskToString.title + ","
                + taskToString.taskStatus + "," + taskToString.description;
    }

    //Создаем задачу из полученной строки.
    public static Task createTaskFromFile(String dataFromFile) {
        String[] splitTask = dataFromFile.split(",");
        int taskID = Integer.parseInt(splitTask[0]);
        TaskType taskType = TaskType.valueOf(splitTask[1]);
        String title = splitTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(splitTask[3]);
        String description = splitTask[4];

        Task task = new Task(title, description, taskStatus);
        task.setTaskType(taskType);
        task.setTaskID(taskID);

        return task;
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