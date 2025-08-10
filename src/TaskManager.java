import java.util.ArrayList;
import java.util.HashMap;
//Менеджер задач.
public class TaskManager {
    //В tasksList храним все задачи простого типа, с учетом их ID.
    public static HashMap<Integer, Task> tasksList = new HashMap<>();
    //В epicTasksList храним все Epic-задачи, с учетом их ID.
    public static HashMap<Integer, EpicTask> epicTasksList = new HashMap<>();
    //В subTasksList храним все подзадачи всех Epic-задач, с учетом их ID.
    public static HashMap<Integer, SubTask> subTasksList = new HashMap<>();
    //Через generalTasksCoun реализуем присвоение ID для новых задач.
    public static int generalTasksCount = 1;

    //Получение списка простых задач. Возвращаем список.
    public static HashMap<Integer, Task> getTasksList() {
        tasksList.forEach((k, v) -> System.out.println(k + "=" + v));
        return tasksList;
    }

    //Получение списка Epic-задач. Возвращаем список.
    public static HashMap<Integer, EpicTask> getEpicTasksList() {
        epicTasksList.forEach((k, v) -> System.out.println(k + "=" + v));
        return epicTasksList;
    }

    //Получение списка подзадач Epic-задач. Возвращаем список.
    public static HashMap<Integer, SubTask> getSubTasksList() {
        subTasksList.forEach((k, v) -> System.out.println(k + "=" + v));
        return subTasksList;
    }

    //Получение списка подзадач определенной Epic-задачи. Возвращаем список.
    public static HashMap<Integer, SubTask> getSubTasksOfEpicTask(Integer epicTaskID) {
        EpicTask epicTask = epicTasksList.get(epicTaskID);
        HashMap<Integer, SubTask> subTasksOfEpicTask = new HashMap<>();
        for (Integer epicSubTaskID : epicTask.epicSubTaskIDList) {
            SubTask subTask = subTasksList.get(epicSubTaskID);
            subTasksOfEpicTask.put(epicSubTaskID, subTask);
        }
        subTasksOfEpicTask.forEach((k, v) -> System.out.println(k + "=" + v));
        return subTasksOfEpicTask;
    }

    //Полное очищение списка всех простых задач. Возвращаем пустой список.
    public static HashMap<Integer, Task> clearTasksLists() {
        tasksList.clear();
        System.out.println("Список задач очищен");
        return tasksList;
    }

    //Полное очищение списка всех Epic-задач. Возвращаем пустой список.
    public static HashMap<Integer, EpicTask> clearEpicTasksLists() {
        epicTasksList.clear();
        subTasksList.clear();
        System.out.println("Список Epic-задач очищен");
        return epicTasksList;
    }

    //Полное очищение списка всех подзадач. Возвращаем пустой список.
    public static HashMap<Integer, SubTask> clearSubTasksLists() {
        subTasksList.clear();
        for (Integer key : epicTasksList.keySet()) {
            EpicTask epicTask = epicTasksList.get(key);
            epicTask.clearEpicSubTaskIDList();
        }
        System.out.println("Список подзадач Epic-задач очищен");
        return subTasksList;
    }

    //Получение простой задачи по ее ID. Возвращаем имеющуюся простую задачу.
    public static Task getTaskByID(Integer taskID) {
        if (!tasksList.containsKey(taskID)) {
            System.out.println("Задачи с ID '" + taskID + "' не найдено.");
            return null;
        } else {
            Task task = tasksList.get(taskID);
            System.out.println(task);
            return task;
        }
    }

    //Получение Epic-задачи по ее ID. Возвращаем имеющуюся Epic-задачу.
    public static EpicTask getEpicTaskByID(Integer taskID) {
        if (!epicTasksList.containsKey(taskID)) {
            System.out.println("Epic-задачи с ID '" + taskID + "' не найдено.");
            return null;
        } else {
            EpicTask epicTask = epicTasksList.get(taskID);
            System.out.println(epicTask);
            return epicTask;
        }
    }

    //Получение подзадачи по ее ID. Возвращаем имеющуюся подзадачу.
    public static SubTask getSubTaskByID(Integer taskID) {
        if (!subTasksList.containsKey(taskID)) {
            System.out.println("Подзадачи с ID '" + taskID + "' не найдено.");
            return null;
        } else {
            SubTask subTask = subTasksList.get(taskID);
            System.out.println(subTask);
            return subTask;
        }
    }

    //Добавление в список новой простой задачи. Возвращаем новую простую задачу.
    public static Task addTask(Task task) {
        task.taskID = generalTasksCount;
        generalTasksCount += 1;
        tasksList.put(task.taskID, task);
        System.out.println("Создана новая задача: " + task);
        return task;
    }

    //Добавление в список новой Epic-задачи. Возвращаем новую Epic-задачу.
    public static EpicTask addEpicTask(EpicTask epicTask) {
        epicTask.taskID = generalTasksCount;
        generalTasksCount += 1;
        epicTasksList.put(epicTask.taskID, epicTask);
        System.out.println("Создана новая задача: " + epicTask);
        return epicTask;
    }

    //Добавление в список новой подзадачи. Возвращаем новую подзадачу.
    public static SubTask addSubTask(SubTask subTask) {
        subTask.taskID = generalTasksCount;
        generalTasksCount += 1;
        subTasksList.put(subTask.taskID, subTask);
        EpicTask epicTask = epicTasksList.get(subTask.epicTaskID);
        epicTask.addSubTaskID(subTask.taskID);
        System.out.println("Добавлена новая подзадача: " + subTask + " в epic-задачу: " + epicTask);
        chooseEpicTaskStatus(subTask);
        return subTask;
    }

    //Обновление информации об имеющейся простой задаче. Возвращаем обновленную простую задачу.
    public static Task updateTask(Integer taskID, Task updatedTask) {
        tasksList.put(taskID, updatedTask);
        System.out.println("Задача обновлена: " + updatedTask);
        return updatedTask;
    }

    //Обновление информации об имеющейся Epic-задаче. Возвращаем обновленную Epic-задачу.
    public static EpicTask updateEpicTask(Integer taskID, EpicTask updatedEpicTask) {
        epicTasksList.put(taskID, updatedEpicTask);
        System.out.println("Задача отредактирована: " + updatedEpicTask);
        return updatedEpicTask;
    }

    //Обновление информации об имеющейся подзадаче. Возвращаем обновленную подзадачу.
    public static SubTask updateSubTask(Integer taskID, SubTask updatedSubTask) {
        subTasksList.put(taskID, updatedSubTask);
        System.out.println("Задача отредактирована: " + updatedSubTask);
        chooseEpicTaskStatus(updatedSubTask);
        return updatedSubTask;
    }

    //Определение статуса Epic-задачи на основе статусов входящих в нее подзадач.
    private static void chooseEpicTaskStatus(SubTask subTask) {
        EpicTask epicTask = epicTasksList.get(subTask.epicTaskID);
        ArrayList<Integer> epicSubTaskIDList = epicTask.epicSubTaskIDList;
        ArrayList<TaskStatus> statusOfSubTasks = new ArrayList<>();
        for (Integer subTaskID : epicSubTaskIDList) {
            SubTask otherSubTask = subTasksList.get(subTaskID);
            statusOfSubTasks.add(otherSubTask.taskStatus);
        }
        if (statusOfSubTasks.contains(TaskStatus.IN_PROGRESS)) {
            epicTask.taskStatus = TaskStatus.IN_PROGRESS;
        } else if (!statusOfSubTasks.contains(TaskStatus.NEW)) {
            epicTask.taskStatus = TaskStatus.DONE;
        } else {
            epicTask.taskStatus = TaskStatus.NEW;
        }
        System.out.println("Epic-задача '" + epicTask.taskID + "' сейчас имеет статус '" + epicTask.taskStatus + "'");
    }

    //Удаление любой задачи по ее ID.
    public static void removeTaskByID(Integer taskID) {
        if (tasksList.containsKey(taskID)) {
            tasksList.remove(taskID);
            System.out.println("Задача удалена");
        } else if (epicTasksList.containsKey(taskID)) {
            EpicTask epicTask = epicTasksList.get(taskID);
            for (Integer subTaskID : epicTask.epicSubTaskIDList) {
                subTasksList.remove(subTaskID);
            }
            epicTasksList.remove(taskID);
            System.out.println("Задача удалена");
        } else if (subTasksList.containsKey(taskID)) {
            SubTask subTask = subTasksList.get(taskID);
            EpicTask epicTask = epicTasksList.get(subTask.epicTaskID);
            epicTask.removeEpicSubTaskByID(subTask.taskID);
            subTasksList.remove(taskID);
            System.out.println("Задача удалена");
            chooseEpicTaskStatus(subTask);
        } else {
            System.out.println("Задача с ID: '" + taskID + "' не найдена. Повторите попытку.");
        }
    }

}