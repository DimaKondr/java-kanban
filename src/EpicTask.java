import java.util.ArrayList;

public class EpicTask extends Task {
    //В списке храним ID всех подзадач, которые входят в Epic-задачу.
    public ArrayList<Integer> epicSubTaskIDList;

    public EpicTask(String title, String description, TaskStatus taskStatus) {
        super(title, description, taskStatus);
        epicSubTaskIDList = new ArrayList<>();
    }

    //Получаем списка ID подзадач.
    public ArrayList<Integer> getEpicSubTaskIDList() {
        return epicSubTaskIDList;
    }

    //Задаем в качестве списка ID подзадач полученный список.
    public void setEpicSubTaskIDList(ArrayList<Integer> epicSubTaskIDList) {
        this.epicSubTaskIDList = epicSubTaskIDList;
    }

    //Добавление ID подзадачи в список.
    public void addSubTaskID(Integer subTaskID) {
        epicSubTaskIDList.add(subTaskID);
    }

    //Очищаем полностью список ID подзадач.
    public void clearEpicSubTaskIDList() {
        epicSubTaskIDList.clear();
    }

    //Удаляем из списка ID определенной подзадачи.
    public void removeEpicSubTaskByID(Integer subTaskID) {
        epicSubTaskIDList.remove(subTaskID);
    }

    @Override
    public String toString() {
        return "EpicTask{title='" + title + "', description='" + description + "', taskStatus='"
                + taskStatus + "', epicTaskID='" + taskID + "', epicSubTaskList='" + epicSubTaskIDList + "'}";
    }
}