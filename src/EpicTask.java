import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    //В списке храним ID всех подзадач, которые входят в Epic-задачу.
    public List<Integer> epicSubTaskIDList;

    //Конструктор для создания новых объектов.
    public EpicTask(String title, String description, TaskStatus taskStatus) {
        super(title, description, taskStatus);
        epicSubTaskIDList = new ArrayList<>();
    }

    //Конструктор для создания копии объекта.
    public EpicTask(EpicTask epicTaskForCopy) {
        super(epicTaskForCopy);
        this.epicSubTaskIDList = new ArrayList<>(epicTaskForCopy.epicSubTaskIDList);
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
        return epicTaskToString.taskID + "," + TaskType.EPICTASK + "," + epicTaskToString.title + ","
                + epicTaskToString.taskStatus + "," + epicTaskToString.description;
    }

    //Создаем Epic-задачу из полученной строки.
    public static EpicTask createEpicTaskFromFile(String dataFromFile) {
        String[] splitEpicTask = dataFromFile.split(",");
        int taskID = Integer.parseInt(splitEpicTask[0]);
        TaskType taskType = TaskType.valueOf(splitEpicTask[1]);
        String title = splitEpicTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(splitEpicTask[3]);
        String description = splitEpicTask[4];

        EpicTask epicTask = new EpicTask(title, description, taskStatus);
        epicTask.setTaskType(taskType);
        epicTask.setTaskID(taskID);

        return epicTask;
    }

    @Override
    public String toString() {
        return "EpicTask{title='" + title + "', description='" + description + "', taskStatus='"
                + taskStatus + "', epicTaskID='" + taskID + "', epicSubTaskList='" + epicSubTaskIDList + "'}";
    }

}