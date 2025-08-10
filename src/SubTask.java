public class SubTask extends Task {
    public int epicTaskID; //Здесь храним ID Epic-задачи, в которую входит подзадача.

    public SubTask(String title, String description, TaskStatus taskStatus, int epicTaskID) {
        super(title, description, taskStatus);
        this.epicTaskID = epicTaskID;
    }

    @Override
    public String toString() {
        return "SubTask{epicTaskID='" + epicTaskID + "', title='" + title
                + "', description='" + description + "', taskStatus='"
                + taskStatus + "', subTaskID='" + taskID + "'}";
    }
}