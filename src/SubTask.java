public class SubTask extends Task {
    public int epicTaskID; //Здесь храним ID Epic-задачи, в которую входит подзадача.

    //Конструктор для создания новых объектов.
    public SubTask(String title, String description, TaskStatus taskStatus, int epicTaskID) {
        super(title, description, taskStatus);
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

    @Override
    public String toString() {
        return "SubTask{epicTaskID='" + epicTaskID + "', title='" + title
                + "', description='" + description + "', taskStatus='"
                + taskStatus + "', subTaskID='" + taskID + "'}";
    }
}