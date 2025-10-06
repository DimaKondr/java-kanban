import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Менеджер задач.
public class InMemoryTaskManager implements TaskManager {
    //В tasksList храним все задачи простого типа, с учетом их ID.
    public Map<Integer, Task> tasksList;
    //В epicTasksList храним все Epic-задачи, с учетом их ID.
    public Map<Integer, EpicTask> epicTasksList;
    //В subTasksList храним все подзадачи всех Epic-задач, с учетом их ID.
    public Map<Integer, SubTask> subTasksList;
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
    public List<Task> getTasksList() {
        List<Task> returnedTasksList = new ArrayList<>();
        for (Integer key : tasksList.keySet()) {
            Task task = tasksList.get(key);
            returnedTasksList.add(createTaskCopy(task));
        }
        return returnedTasksList;
    }

    //Получение списка Epic-задач. Возвращаем список.
    @Override
    public List<EpicTask> getEpicTasksList() {
        List<EpicTask> returnedEpicTasksList = new ArrayList<>();
        for (Integer key : epicTasksList.keySet()) {
            EpicTask epicTask = epicTasksList.get(key);
            returnedEpicTasksList.add(createEpicTaskCopy(epicTask));
        }
        return returnedEpicTasksList;
    }

    //Получение списка подзадач Epic-задач. Возвращаем список.
    @Override
    public List<SubTask> getSubTasksList() {
        List<SubTask> returnedSubTasksList = new ArrayList<>();
        for (Integer key : subTasksList.keySet()) {
            SubTask subTask = subTasksList.get(key);
            returnedSubTasksList.add(createSubTaskCopy(subTask));
        }
        return returnedSubTasksList;
    }

    //Получение списка подзадач определенной Epic-задачи. Возвращаем список.
    @Override
    public List<SubTask> getSubTasksOfEpicTask(Integer epicTaskID) {
        EpicTask epicTask = epicTasksList.get(epicTaskID);
        List<SubTask> subTasksOfEpicTask = new ArrayList<>();
        for (Integer epicSubTaskID : epicTask.getEpicSubTaskIDList()) {
            SubTask subTask = subTasksList.get(epicSubTaskID);
            subTasksOfEpicTask.add(createSubTaskCopy(subTask));
        }
        return subTasksOfEpicTask;
    }

    //Полное очищение списка всех простых задач.
    @Override
    public void clearTasksLists() {
        tasksList.clear();
    }

    //Полное очищение списка всех Epic-задач.
    @Override
    public void clearEpicTasksLists() {
        epicTasksList.clear();
        subTasksList.clear();
    }

    //Полное очищение списка всех подзадач.
    @Override
    public void clearSubTasksLists() {
        subTasksList.clear();
        for (Integer key : epicTasksList.keySet()) {
            EpicTask epicTask = epicTasksList.get(key);
            epicTask.clearEpicSubTaskIDList();
            chooseEpicTaskStatus(epicTask);
        }
    }

    //Получение простой задачи по ее ID. Возвращаем имеющуюся простую задачу.
    @Override
    public Task getTaskByID(Integer taskID) {
        if (!tasksList.containsKey(taskID)) {
            return null;
        } else {
            Task task = tasksList.get(taskID);
            historyManager.addToHistory(createTaskCopy(task));
            return createTaskCopy(task);
        }
    }

    //Получение Epic-задачи по ее ID. Возвращаем имеющуюся Epic-задачу.
    @Override
    public EpicTask getEpicTaskByID(Integer taskID) {
        if (!epicTasksList.containsKey(taskID)) {
            return null;
        } else {
            EpicTask epicTask = epicTasksList.get(taskID);
            historyManager.addToHistory(createEpicTaskCopy(epicTask));
            return createEpicTaskCopy(epicTask);
        }
    }

    //Получение подзадачи по ее ID. Возвращаем имеющуюся подзадачу.
    @Override
    public SubTask getSubTaskByID(Integer taskID) {
        if (!subTasksList.containsKey(taskID)) {
            return null;
        } else {
            SubTask subTask = subTasksList.get(taskID);
            historyManager.addToHistory(createSubTaskCopy(subTask));
            return createSubTaskCopy(subTask);
        }
    }

    //Добавление в список новой простой задачи.
    @Override
    public void addTask(Task task) {
        task.setTaskID(generateTaskID());
        tasksList.put(task.getTaskID(), createTaskCopy(task));
    }

    //Добавление в список новой Epic-задачи.
    @Override
    public void addEpicTask(EpicTask epicTask) {
        epicTask.setTaskID(generateTaskID());
        epicTasksList.put(epicTask.getTaskID(), createEpicTaskCopy(epicTask));
    }

    //Добавление в список новой подзадачи.
    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setTaskID(generateTaskID());
        int subTaskID = subTask.getTaskID();
        int epicTaskID = subTask.getEpicTaskID();
        if (subTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID)) {
            subTasksList.put(subTask.getTaskID(), createSubTaskCopy(subTask));
            EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
            epicTask.addSubTaskID(subTask.getTaskID());
            chooseEpicTaskStatus(epicTask);
        }
    }

    //Обновление информации об имеющейся простой задаче.
    @Override
    public void updateTask(Task updatedTask) {
        tasksList.put(updatedTask.getTaskID(), createTaskCopy(updatedTask));
    }

    //Обновление информации об имеющейся Epic-задаче.
    @Override
    public void updateEpicTask(EpicTask updatedEpicTask) {
        EpicTask oldEpicTask = epicTasksList.get(updatedEpicTask.getTaskID());
        List<Integer> oldEpicSubTaskIDList = oldEpicTask.getEpicSubTaskIDList();
        updatedEpicTask.setEpicSubTaskIDList(oldEpicSubTaskIDList);
        chooseEpicTaskStatus(updatedEpicTask);
        epicTasksList.put(updatedEpicTask.getTaskID(), createEpicTaskCopy(updatedEpicTask));
    }

    //Обновление информации об имеющейся подзадаче.
    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        int updatedSubTaskID = updatedSubTask.getTaskID();
        int epicTaskID = updatedSubTask.getEpicTaskID();
        if (updatedSubTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID)) {
            subTasksList.put(updatedSubTask.getTaskID(), updatedSubTask);
            EpicTask epicTask = epicTasksList.get(updatedSubTask.getEpicTaskID());
            chooseEpicTaskStatus(epicTask);
        }
    }

    //Удаление простой задачи по ее ID.
    @Override
    public void removeTaskByID(Integer taskID) {
        tasksList.remove(taskID);
        historyManager.removeFromHistory(taskID);
    }

    //Удаление Epic-задачи по ее ID.
    @Override
    public void removeEpicTaskByID(Integer taskID) {
        EpicTask epicTask = epicTasksList.get(taskID);
        if (epicTask != null) {
            for (Integer subTaskID : epicTask.getEpicSubTaskIDList()) {
                subTasksList.remove(subTaskID);
                historyManager.removeFromHistory(subTaskID);
            }
            epicTasksList.remove(taskID);
            historyManager.removeFromHistory(taskID);
        }
    }

    //Удаление подзадачи по ее ID.
    @Override
    public void removeSubTaskByID(Integer taskID) {
        SubTask subTask = subTasksList.get(taskID);
        if (subTask != null) {
            EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
            epicTask.removeEpicSubTaskByID(subTask.getTaskID());
            subTasksList.remove(taskID);
            historyManager.removeFromHistory(taskID);
            chooseEpicTaskStatus(epicTask);
        }
    }

    //Определение статуса Epic-задачи на основе статусов входящих в нее подзадач.
    @Override
    public void chooseEpicTaskStatus(EpicTask epicTask) {
        List<TaskStatus> statusOfSubTasks = new ArrayList<>();
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
            for (Integer key : tasksList.keySet()) {
                if (key > maxID) maxID = key;
            }
        }
        if (!epicTasksList.isEmpty()) {
            for (Integer key : epicTasksList.keySet()) {
                if (key > maxID) maxID = key;
            }
        }
        if (!subTasksList.isEmpty()) {
            for (Integer key : subTasksList.keySet()) {
                if (key > maxID) maxID = key;
            }
        }
        generalTasksCount = maxID + 1;
        return generalTasksCount;
    }

    //Метод для создания копии задачи для сохранения в истории просмотра.
    @Override
    public Task createTaskCopy(Task task) {
        return new Task(task);
    }

    //Метод для создания копии эпик-задачи для сохранения в истории просмотра.
    @Override
    public EpicTask createEpicTaskCopy(EpicTask epicTask) {
        return new EpicTask(epicTask);
    }

    //Метод для создания копии подзадачи для сохранения в истории просмотра.
    @Override
    public SubTask createSubTaskCopy(SubTask subTask) {
        return new SubTask(subTask);
    }

    //Метод для получения менеджера истории.
    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

}