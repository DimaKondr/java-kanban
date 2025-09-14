import java.util.ArrayList;
import java.util.HashMap;
//Менеджер задач.
public class InMemoryTaskManager implements TaskManager {
    //В tasksList храним все задачи простого типа, с учетом их ID.
    public HashMap<Integer, Task> tasksList;
    //В epicTasksList храним все Epic-задачи, с учетом их ID.
    public HashMap<Integer, EpicTask> epicTasksList;
    //В subTasksList храним все подзадачи всех Epic-задач, с учетом их ID.
    public HashMap<Integer, SubTask> subTasksList;
    //Через generalTasksCoun реализуем присвоение ID для новых задач.
    public int generalTasksCount;
    //Инициализируем менеджера истории, который будет работать с конкретным экземпляром менеджера задач.
    public HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasksList = new HashMap<>();
        epicTasksList = new HashMap<>();
        subTasksList = new HashMap<>();
        generalTasksCount = 0;
        historyManager = Managers.getDefaultHistory();
    }

    //Получение списка простых задач. Возвращаем список.
    @Override
    public ArrayList<Task> getTasksList() {
        ArrayList<Task> tasksArrayList = new ArrayList<>();
        for (Integer key : tasksList.keySet()) {
            Task task = tasksList.get(key);
            tasksArrayList.add(task);
        }
        return tasksArrayList;
    }

    //Получение списка Epic-задач. Возвращаем список.
    @Override
    public ArrayList<EpicTask> getEpicTasksList() {
        ArrayList<EpicTask> epicTasksArrayList = new ArrayList<>();
        for (Integer key : epicTasksList.keySet()) {
            EpicTask epicTask = epicTasksList.get(key);
            epicTasksArrayList.add(epicTask);
        }
        return epicTasksArrayList;
    }

    //Получение списка подзадач Epic-задач. Возвращаем список.
    @Override
    public ArrayList<SubTask> getSubTasksList() {
        ArrayList<SubTask> subTasksArrayList = new ArrayList<>();
        for (Integer key : subTasksList.keySet()) {
            SubTask subTask = subTasksList.get(key);
            subTasksArrayList.add(subTask);
        }
        return subTasksArrayList;
    }

    //Получение списка подзадач определенной Epic-задачи. Возвращаем список.
    @Override
    public ArrayList<SubTask> getSubTasksOfEpicTask(Integer epicTaskID) {
        EpicTask epicTask = epicTasksList.get(epicTaskID);
        ArrayList<SubTask> subTasksOfEpicTask = new ArrayList<>();
        for (Integer epicSubTaskID : epicTask.getEpicSubTaskIDList()) {
            SubTask subTask = subTasksList.get(epicSubTaskID);
            subTasksOfEpicTask.add(subTask);
        }
        return subTasksOfEpicTask;
    }

    //Полное очищение списка всех простых задач. Возвращаем пустой список.
    @Override
    public HashMap<Integer, Task> clearTasksLists() {
        tasksList.clear();
        return tasksList;
    }

    //Полное очищение списка всех Epic-задач. Возвращаем пустой список.
    @Override
    public HashMap<Integer, EpicTask> clearEpicTasksLists() {
        epicTasksList.clear();
        subTasksList.clear();
        return epicTasksList;
    }

    //Полное очищение списка всех подзадач. Возвращаем пустой список.
    @Override
    public HashMap<Integer, SubTask> clearSubTasksLists() {
        subTasksList.clear();
        for (Integer key : epicTasksList.keySet()) {
            EpicTask epicTask = epicTasksList.get(key);
            epicTask.clearEpicSubTaskIDList();
            chooseEpicTaskStatus(epicTask);
        }
        return subTasksList;
    }

    //Получение простой задачи по ее ID. Возвращаем имеющуюся простую задачу.
    @Override
    public Task getTaskByID(Integer taskID) {
        if (!tasksList.containsKey(taskID)) {
            return null;
        } else {
            Task task = tasksList.get(taskID);
            historyManager.addToHistory(createTaskCopyForHistory(task));
            return task;
        }
    }

    //Получение Epic-задачи по ее ID. Возвращаем имеющуюся Epic-задачу.
    @Override
    public EpicTask getEpicTaskByID(Integer taskID) {
        if (!epicTasksList.containsKey(taskID)) {
            return null;
        } else {
            EpicTask epicTask = epicTasksList.get(taskID);
            historyManager.addToHistory(createEpicTaskCopyForHistory(epicTask));
            return epicTask;
        }
    }

    //Получение подзадачи по ее ID. Возвращаем имеющуюся подзадачу.
    @Override
    public SubTask getSubTaskByID(Integer taskID) {
        if (!subTasksList.containsKey(taskID)) {
            return null;
        } else {
            SubTask subTask = subTasksList.get(taskID);
            historyManager.addToHistory(createSubTaskCopyForHistory(subTask));
            return subTask;
        }
    }

    //Добавление в список новой простой задачи. Возвращаем новую простую задачу.
    @Override
    public Task addTask(Task task) {
        task.setTaskID(generateTaskID());
        tasksList.put(task.getTaskID(), task);
        return task;
    }

    //Добавление в список новой Epic-задачи. Возвращаем новую Epic-задачу.
    @Override
    public EpicTask addEpicTask(EpicTask epicTask) {
        epicTask.setTaskID(generateTaskID());
        epicTasksList.put(epicTask.getTaskID(), epicTask);
        return epicTask;
    }

    //Добавление в список новой подзадачи. Возвращаем новую подзадачу.
    @Override
    public SubTask addSubTask(SubTask subTask) {
        subTask.setTaskID(generateTaskID());
        int subTaskID = subTask.getTaskID();
        int epicTaskID = subTask.getEpicTaskID();
        if (subTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID)) {
            subTasksList.put(subTask.getTaskID(), subTask);
            EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
            epicTask.addSubTaskID(subTask.getTaskID());
            chooseEpicTaskStatus(epicTask);
        }
        return subTask;
    }

    //Обновление информации об имеющейся простой задаче. Возвращаем обновленную простую задачу.
    @Override
    public Task updateTask(Task updatedTask) {
        tasksList.put(updatedTask.getTaskID(), updatedTask);
        return updatedTask;
    }

    //Обновление информации об имеющейся Epic-задаче. Возвращаем обновленную Epic-задачу.
    @Override
    public EpicTask updateEpicTask(EpicTask updatedEpicTask) {
        EpicTask oldEpicTask = getEpicTaskByID(updatedEpicTask.getTaskID());
        ArrayList<Integer> oldEpicSubTaskIDList = oldEpicTask.getEpicSubTaskIDList();
        updatedEpicTask.setEpicSubTaskIDList(oldEpicSubTaskIDList);
        chooseEpicTaskStatus(updatedEpicTask);
        epicTasksList.put(updatedEpicTask.getTaskID(), updatedEpicTask);
        return updatedEpicTask;
    }

    //Обновление информации об имеющейся подзадаче. Возвращаем обновленную подзадачу.
    @Override
    public SubTask updateSubTask(SubTask updatedSubTask) {
        int updatedSubTaskID = updatedSubTask.getTaskID();
        int epicTaskID = updatedSubTask.getEpicTaskID();
        if (updatedSubTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID)) {
            subTasksList.put(updatedSubTask.getTaskID(), updatedSubTask);
            EpicTask epicTask = epicTasksList.get(updatedSubTask.getEpicTaskID());
            chooseEpicTaskStatus(epicTask);
        }
        return updatedSubTask;
    }

    //Удаление простой задачи по ее ID.
    @Override
    public void removeTaskByID(Integer taskID) {
        tasksList.remove(taskID);
    }

    //Удаление Epic-задачи по ее ID.
    @Override
    public void removeEpicTaskByID(Integer taskID) {
        EpicTask epicTask = epicTasksList.get(taskID);
        for (Integer subTaskID : epicTask.getEpicSubTaskIDList()) {
            subTasksList.remove(subTaskID);
        }
        epicTasksList.remove(taskID);
    }

    //Удаление подзадачи по ее ID.
    @Override
    public void removeSubTaskByID(Integer taskID) {
        SubTask subTask = subTasksList.get(taskID);
        EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
        epicTask.removeEpicSubTaskByID(subTask.getTaskID());
        subTasksList.remove(taskID);
        chooseEpicTaskStatus(epicTask);
    }

    //Определение статуса Epic-задачи на основе статусов входящих в нее подзадач.
    @Override
    public void chooseEpicTaskStatus(EpicTask epicTask) {
        ArrayList<TaskStatus> statusOfSubTasks = new ArrayList<>();
        for (Integer subTaskID : epicTask.getEpicSubTaskIDList()) {
            SubTask subTask = subTasksList.get(subTaskID);
            statusOfSubTasks.add(subTask.getTaskStatus());
        }
        if (!statusOfSubTasks.isEmpty() && !statusOfSubTasks.contains(TaskStatus.IN_PROGRESS)
                && !statusOfSubTasks.contains(TaskStatus.NEW)) {
            epicTask.setTaskStatus(TaskStatus.DONE);
        } else if (!statusOfSubTasks.contains(TaskStatus.DONE) && !statusOfSubTasks.contains(TaskStatus.IN_PROGRESS)) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else {
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    //Метод для генерации ID задач.
    @Override
    public int generateTaskID() {
        int maxID = 0;
        if (!tasksList.isEmpty()) {
            for (Integer key : tasksList.keySet()){
                if (key > maxID) maxID = key;
            }
        }
        if (!epicTasksList.isEmpty()) {
            for (Integer key : epicTasksList.keySet()){
                if (key > maxID) maxID = key;
            }
        }
        if (!subTasksList.isEmpty()) {
            for (Integer key : subTasksList.keySet()){
                if (key > maxID) maxID = key;
            }
        }
        generalTasksCount = maxID + 1;
        return generalTasksCount;
    }

    //Метод для создания копии задачи для сохранения в истории просмотра.
    @Override
    public Task createTaskCopyForHistory(Task task) {
        Task returnedTask = new Task(task.title, task.description, task.getTaskStatus());
        returnedTask.setTaskID(task.getTaskID());
        return returnedTask;
    }

    //Метод для создания копии эпик-задачи для сохранения в истории просмотра.
    @Override
    public EpicTask createEpicTaskCopyForHistory(EpicTask epicTask) {
        EpicTask returnedEpicTask = new EpicTask(epicTask.title, epicTask.description, epicTask.getTaskStatus());
        ArrayList<Integer> epicSubTaskIDList = new ArrayList<>(epicTask.getEpicSubTaskIDList());
        returnedEpicTask.setEpicSubTaskIDList(epicSubTaskIDList);
        returnedEpicTask.setTaskID(epicTask.getTaskID());
        return returnedEpicTask;
    }

    //Метод для создания копии подзадачи для сохранения в истории просмотра.
    @Override
    public SubTask createSubTaskCopyForHistory(SubTask subTask) {
        SubTask returnedSubTask = new SubTask(subTask.title, subTask.description,
                subTask.getTaskStatus(), subTask.getEpicTaskID());
        returnedSubTask.setTaskID(subTask.getTaskID());
        return returnedSubTask;
    }

    //Метод для получения менеджера истории.
    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}