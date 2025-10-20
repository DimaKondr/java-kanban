import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    public int epicTaskID; //Здесь храним ID Epic-задачи, в которую входит подзадача.

    //Конструктор для создания новых объектов.
    public SubTask(String title, String description, TaskStatus taskStatus, int epicTaskID, Long duration) {
        super(title, description, taskStatus,duration);
        this.epicTaskID = epicTaskID;
    }

    //Конструктор для создания копии объекта.
    public SubTask(SubTask subTaskForCopy) {
        super(subTaskForCopy);
        this.epicTaskID = subTaskForCopy.epicTaskID;
    }

    //Получаем ID Epic-задачи в которую входит подзадача.
    public int getEpicTaskID() {
        return epicTaskID;
    }

    //Переводим подзадачу в строку для сохранения в файл.
    public String toStringForFile(SubTask subTaskToString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");
        String formatedStartTime
                = subTaskToString.getStartTime() == null ? "-" : formatter.format(subTaskToString.getStartTime());
        return subTaskToString.taskID + "," + TaskType.SUBTASK + "," + subTaskToString.title + ","
                + subTaskToString.taskStatus + "," + subTaskToString.description + "," + subTaskToString.epicTaskID
                + "," + subTaskToString.getDuration().toMinutes() + "," + formatedStartTime + "," + "-";
    }

    //Создаем подзадачу из полученной строки.
    public static SubTask createSubTaskFromFile(String dataFromFile) {
        String[] splitSubTask = dataFromFile.split(",");
        int taskID = Integer.parseInt(splitSubTask[0]);
        TaskType taskType = TaskType.valueOf(splitSubTask[1]);
        String title = splitSubTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(splitSubTask[3]);
        String description = splitSubTask[4];
        int epicTaskID = Integer.parseInt(splitSubTask[5]);
        Long duration = Long.parseLong(splitSubTask[6]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");
        LocalDateTime startTime = splitSubTask[7].equals("-") ? null : LocalDateTime.parse(splitSubTask[7], formatter);

        SubTask subTask = new SubTask(title, description, taskStatus, epicTaskID, duration);
        subTask.setTaskType(taskType);
        subTask.setTaskID(taskID);
        subTask.setStartTime(startTime);

        return subTask;
    }

    @Override
    public String toString() {
        return "SubTask{epicTaskID='" + epicTaskID + "', title='" + title
                + "', description='" + description + "', taskStatus='"
                + taskStatus + "', subTaskID='" + taskID + "', duration='"
                + duration + "', startTime='" + startTime + "', endTime='" + getEndTime() + "'}";
    }

}