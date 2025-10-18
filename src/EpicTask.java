import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    //В списке храним ID всех подзадач, которые входят в Epic-задачу.
    public List<Integer> epicSubTaskIDList;
    LocalDateTime endTime;//Здесь храним время окончания Epic-задачи, рассчитанное на основе данных его подзадач.

    //Конструктор для создания новых объектов.
    public EpicTask(String title, String description, TaskStatus taskStatus) {
        super(title, description, taskStatus, 0L);
        epicSubTaskIDList = new ArrayList<>();
    }

    //Конструктор для создания копии объекта.
    public EpicTask(EpicTask epicTaskForCopy) {
        super(epicTaskForCopy);
        this.epicSubTaskIDList = new ArrayList<>(epicTaskForCopy.epicSubTaskIDList);
        this.endTime = epicTaskForCopy.endTime;
    }

    //Получаем списка ID подзадач.
    public List<Integer> getEpicSubTaskIDList() {
        return epicSubTaskIDList;
    }

    //Задаем в качестве списка ID подзадач полученный список.
    public void setEpicSubTaskIDList(List<Integer> epicSubTaskIDList) {
        if (!epicSubTaskIDList.contains(this.getTaskID())) {
            this.epicSubTaskIDList = epicSubTaskIDList;
        }
    }

    //Добавление ID подзадачи в список.
    public void addSubTaskID(Integer subTaskID) {
        if (!subTaskID.equals(this.getTaskID())) {
            epicSubTaskIDList.add(subTaskID);
        }
    }

    //Очищаем полностью список ID подзадач.
    public void clearEpicSubTaskIDList() {
        epicSubTaskIDList.clear();
    }

    //Удаляем из списка ID определенной подзадачи.
    public void removeEpicSubTaskByID(Integer subTaskID) {
        epicSubTaskIDList.remove(subTaskID);
    }

    //Переводим Epic-задачу в строку для сохранения в файл.
    public String toStringForFile(EpicTask epicTaskToString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");
        String formatedStartTime
                = epicTaskToString.getStartTime() == null ? "-" : formatter.format(epicTaskToString.getStartTime());
        String formatedEndTime
                = epicTaskToString.getEndTime() == null ? "-" : formatter.format(epicTaskToString.getEndTime());
        return epicTaskToString.taskID + "," + TaskType.EPICTASK + "," + epicTaskToString.title + ","
                + epicTaskToString.taskStatus + "," + epicTaskToString.description + "," + "-" + ","
                + epicTaskToString.getDuration().toMinutes() + "," + formatedStartTime + "," + formatedEndTime;
    }

    //Создаем Epic-задачу из полученной строки.
    public static EpicTask createEpicTaskFromFile(String dataFromFile) {
        String[] splitEpicTask = dataFromFile.split(",");
        int taskID = Integer.parseInt(splitEpicTask[0]);
        TaskType taskType = TaskType.valueOf(splitEpicTask[1]);
        String title = splitEpicTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(splitEpicTask[3]);
        String description = splitEpicTask[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(splitEpicTask[6]));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");
        LocalDateTime startTime = splitEpicTask[7].equals("-") ? null : LocalDateTime.parse(splitEpicTask[7], formatter);
        LocalDateTime endTime = splitEpicTask[8].equals("-") ? null : LocalDateTime.parse(splitEpicTask[8], formatter);

        EpicTask epicTask = new EpicTask(title, description, taskStatus);
        epicTask.setTaskType(taskType);
        epicTask.setTaskID(taskID);
        epicTask.setDuration(duration);
        epicTask.setStartTime(startTime);
        epicTask.setEndTime(endTime);

        return epicTask;
    }

    //Задаем длительность выполнения задачи
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    //Получаем время завершения выполнения Epic-задачи
    @Override
    public LocalDateTime getEndTime(){
        return endTime;
    }

    @Override
    public String toString() {
        return "EpicTask{title='" + title + "', description='" + description + "', taskStatus='"
                + taskStatus + "', epicTaskID='" + taskID + "', epicSubTaskList='" + epicSubTaskIDList
                + "', duration='" + duration + "', startTime='" + startTime + "', endTime='" + endTime + "'}";
    }

}