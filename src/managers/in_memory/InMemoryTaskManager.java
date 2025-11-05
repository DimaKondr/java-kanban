package managers.in_memory;

import managers.Managers;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    //В sortedTaskSet храним задачи простого типа и подзадачи всех Epic-задач, отсортированные по времени начала.
    private Set<Task> sortedTaskSet;

    public InMemoryTaskManager() {
        tasksList = new HashMap<>();
        epicTasksList = new HashMap<>();
        subTasksList = new HashMap<>();
        generalTasksCount = 0;
        historyManager = Managers.getDefaultHistory();
        sortedTaskSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    //Получение списка простых задач. Возвращаем список.
    @Override
    public List<Task> getTasksList() {
        return tasksList.values()
                .stream()
                .map(this::createTaskCopy)
                .collect(Collectors.toList());
    }

    //Получение списка Epic-задач. Возвращаем список.
    @Override
    public List<EpicTask> getEpicTasksList() {
        return epicTasksList.values()
                .stream()
                .map(this::createEpicTaskCopy)
                .collect(Collectors.toList());
    }

    //Получение списка подзадач Epic-задач. Возвращаем список.
    @Override
    public List<SubTask> getSubTasksList() {
        return subTasksList.values()
                .stream()
                .map(this::createSubTaskCopy)
                .collect(Collectors.toList());
    }

    //Получение списка подзадач определенной Epic-задачи. Возвращаем список.
    @Override
    public List<SubTask> getSubTasksOfEpicTask(Integer epicTaskID) {
        if (epicTaskID != null && epicTasksList.containsKey(epicTaskID)) {
            return epicTasksList.get(epicTaskID).getEpicSubTaskIDList()
                    .stream()
                    .map(subTaskID -> subTasksList.get(subTaskID))
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Не существует Epic-задачи с указанным ID: " + epicTaskID);
        }
    }

    //Получение списка задач простого типа и подзадач всех Epic-задач, отсортированные по времени начала.
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTaskSet);
    }

    //Полное очищение списка всех простых задач.
    @Override
    public void clearTasksLists() {
        for (Task task : getTasksList()) {
            if (task.getStartTime() != null) {
                sortedTaskSet.remove(task);
            }
        }
        tasksList.clear();
    }

    //Полное очищение списка всех Epic-задач.
    @Override
    public void clearEpicTasksLists() {
        for (SubTask subTask : getSubTasksList()) {
            if (subTask.getStartTime() != null) {
                sortedTaskSet.remove(subTask);
            }
        }
        epicTasksList.clear();
        subTasksList.clear();
    }

    //Полное очищение списка всех подзадач.
    @Override
    public void clearSubTasksLists() {
        for (SubTask subTask : getSubTasksList()) {
            if (subTask.getStartTime() != null) {
                sortedTaskSet.remove(subTask);
            }
        }
        subTasksList.clear();
        epicTasksList.values()
                .forEach(epicTask -> {
                    epicTask.clearEpicSubTaskIDList();
                    chooseEpicTaskStatus(epicTask);
                    calculateEpicTaskDuration(epicTask);
                });
    }

    //Получение простой задачи по ее ID. Возвращаем имеющуюся простую задачу.
    @Override
    public Task getTaskByID(Integer taskID) {
        if (taskID == null || !tasksList.containsKey(taskID)) {
            throw new NotFoundException("Не существует задачи с указанным ID: " + taskID);
        } else {
            Task task = tasksList.get(taskID);
            historyManager.addToHistory(createTaskCopy(task));
            return createTaskCopy(task);
        }
    }

    //Получение Epic-задачи по ее ID. Возвращаем имеющуюся Epic-задачу.
    @Override
    public EpicTask getEpicTaskByID(Integer taskID) {
        if (taskID == null || !epicTasksList.containsKey(taskID)) {
            throw new NotFoundException("Не существует Epic-задачи с указанным ID: " + taskID);
        } else {
            EpicTask epicTask = epicTasksList.get(taskID);
            historyManager.addToHistory(createEpicTaskCopy(epicTask));
            return createEpicTaskCopy(epicTask);
        }
    }

    //Получение подзадачи по ее ID. Возвращаем имеющуюся подзадачу.
    @Override
    public SubTask getSubTaskByID(Integer taskID) {
        if (taskID == null || !subTasksList.containsKey(taskID)) {
            throw new NotFoundException("Не существует подзадачи с указанным ID: " + taskID);
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
        if (task.getStartTime() == null) {
            tasksList.put(task.getTaskID(), createTaskCopy(task));
        } else {
            if (!isOverlapWithCurrentTasks(task)) {
                tasksList.put(task.getTaskID(), createTaskCopy(task));
                sortedTaskSet.add(createTaskCopy(task));
            }
        }
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
        if (subTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID) && subTask.getStartTime() == null) {
            subTasksList.put(subTask.getTaskID(), createSubTaskCopy(subTask));
            EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
            epicTask.addSubTaskID(subTask.getTaskID());
            chooseEpicTaskStatus(epicTask);
            calculateEpicTaskDuration(epicTask);
        }
        if (subTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID) && subTask.getStartTime() != null) {
            if (!isOverlapWithCurrentTasks(subTask)) {
                subTasksList.put(subTask.getTaskID(), createSubTaskCopy(subTask));
                EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
                epicTask.addSubTaskID(subTask.getTaskID());
                chooseEpicTaskStatus(epicTask);
                calculateEpicTaskDuration(epicTask);
                sortedTaskSet.add(createSubTaskCopy(subTask));
            }
        }
    }

    //Обновление информации об имеющейся простой задаче.
    @Override
    public void updateTask(Task updatedTask) {
        if (updatedTask == null || !tasksList.containsKey(updatedTask.getTaskID())) {
            throw new NotFoundException("Задачи с таким ID не существует.");
        }
        if (updatedTask.getStartTime() == null) {
            tasksList.put(updatedTask.getTaskID(), createTaskCopy(updatedTask));
        }
        if (updatedTask.getStartTime() != null) {
            if (!isOverlapWithCurrentTasks(updatedTask)) {
                Task oldTask = tasksList.get(updatedTask.getTaskID());
                tasksList.put(updatedTask.getTaskID(), createTaskCopy(updatedTask));
                if (oldTask.getStartTime() != null) {
                    sortedTaskSet.remove(oldTask);
                }
                sortedTaskSet.add(createTaskCopy(updatedTask));
            }
        }
    }

    //Обновление информации об имеющейся Epic-задаче.
    @Override
    public void updateEpicTask(EpicTask updatedEpicTask) {
        if (updatedEpicTask == null || !epicTasksList.containsKey(updatedEpicTask.getTaskID())) {
            throw new NotFoundException("Epic-задачи с таким ID не существует.");
        }
        EpicTask oldEpicTask = epicTasksList.get(updatedEpicTask.getTaskID());
        List<Integer> oldEpicSubTaskIDList = oldEpicTask.getEpicSubTaskIDList();
        updatedEpicTask.setEpicSubTaskIDList(oldEpicSubTaskIDList);
        chooseEpicTaskStatus(updatedEpicTask);
        calculateEpicTaskDuration(updatedEpicTask);
        epicTasksList.put(updatedEpicTask.getTaskID(), createEpicTaskCopy(updatedEpicTask));
    }

    //Обновление информации об имеющейся подзадаче.
    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        if (updatedSubTask == null || !subTasksList.containsKey(updatedSubTask.getTaskID())) {
            throw new NotFoundException("Подзадачи с таким ID не существует.");
        }
        int updatedSubTaskID = updatedSubTask.getTaskID();
        int epicTaskID = updatedSubTask.getEpicTaskID();
        if (updatedSubTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID)
                && updatedSubTask.getStartTime() == null) {
            subTasksList.put(updatedSubTask.getTaskID(), updatedSubTask);
            EpicTask epicTask = epicTasksList.get(updatedSubTask.getEpicTaskID());
            chooseEpicTaskStatus(epicTask);
            calculateEpicTaskDuration(epicTask);
        }
        if (updatedSubTaskID != epicTaskID && epicTasksList.containsKey(epicTaskID)
                && updatedSubTask.getStartTime() != null) {
            if (!isOverlapWithCurrentTasks(updatedSubTask)) {
                SubTask oldSubTask = createSubTaskCopy(subTasksList.get(updatedSubTask.getTaskID()));
                subTasksList.put(updatedSubTask.getTaskID(), updatedSubTask);
                EpicTask epicTask = epicTasksList.get(updatedSubTask.getEpicTaskID());
                chooseEpicTaskStatus(epicTask);
                calculateEpicTaskDuration(epicTask);
                if (oldSubTask.getStartTime() != null) {
                    sortedTaskSet.remove(oldSubTask);
                }
                sortedTaskSet.add(createSubTaskCopy(updatedSubTask));
            }
        }
    }

    //Удаление простой задачи по ее ID.
    @Override
    public void removeTaskByID(Integer taskID) {
        Task task = tasksList.get(taskID);
        if (task == null) {
            throw new NotFoundException("Не существует задачи с указанным ID: " + taskID);
        }
        if (task.getStartTime() != null) {
            sortedTaskSet.remove(task);
        }
        tasksList.remove(taskID);
        historyManager.removeFromHistory(taskID);
    }

    //Удаление Epic-задачи по ее ID.
    @Override
    public void removeEpicTaskByID(Integer taskID) {
        EpicTask epicTask = epicTasksList.get(taskID);
        if (epicTask == null) {
            throw new NotFoundException("Не существует Epic-задачи с указанным ID: " + taskID);
        }
        for (Integer subTaskID : epicTask.getEpicSubTaskIDList()) {
            if (subTasksList.get(subTaskID) != null && subTasksList.get(subTaskID).getStartTime() != null) {
                sortedTaskSet.remove(subTasksList.get(subTaskID));
            }
            subTasksList.remove(subTaskID);
            historyManager.removeFromHistory(subTaskID);
        }
        epicTasksList.remove(taskID);
        historyManager.removeFromHistory(taskID);
    }

    //Удаление подзадачи по ее ID.
    @Override
    public void removeSubTaskByID(Integer taskID) {
        SubTask subTask = subTasksList.get(taskID);
        if (subTask == null) {
            throw new NotFoundException("Не существует подзадачи с указанным ID: " + taskID);
        }
        EpicTask epicTask = epicTasksList.get(subTask.getEpicTaskID());
        epicTask.removeEpicSubTaskByID(subTask.getTaskID());
        subTasksList.remove(taskID);
        historyManager.removeFromHistory(taskID);
        chooseEpicTaskStatus(epicTask);
        calculateEpicTaskDuration(epicTask);
        if (subTask.getStartTime() != null) {
            sortedTaskSet.remove(subTask);
        }
    }

    //Определение статуса Epic-задачи на основе статусов входящих в нее подзадач.
    @Override
    public void chooseEpicTaskStatus(EpicTask epicTask) {
        List<TaskStatus> statusOfSubTasks = epicTask.getEpicSubTaskIDList()
                .stream()
                .map(subTaskID -> subTasksList.get(subTaskID))
                .map(SubTask::getTaskStatus)
                .collect(Collectors.toList());
        if (!statusOfSubTasks.isEmpty() && !statusOfSubTasks.contains(TaskStatus.IN_PROGRESS)
                && !statusOfSubTasks.contains(TaskStatus.NEW)) {
            epicTask.setTaskStatus(TaskStatus.DONE);
        } else if (!statusOfSubTasks.contains(TaskStatus.DONE) && !statusOfSubTasks.contains(TaskStatus.IN_PROGRESS)) {
            epicTask.setTaskStatus(TaskStatus.NEW);
        } else {
            epicTask.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    //Расчет длительности, времени начала и окончания Epic-задачи на основе длительности входящих в нее подзадач.
    @Override
    public void calculateEpicTaskDuration(EpicTask epicTask) {
        List<SubTask> epicSubTaskList = new ArrayList<>();
        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;
        if (!epicTask.getEpicSubTaskIDList().isEmpty()) {
            for (Integer subTaskID : epicTask.getEpicSubTaskIDList()) {
                epicSubTaskList.add(subTasksList.get(subTaskID));
            }
            for (SubTask subTask : epicSubTaskList) {
                totalDuration = totalDuration.plus(subTask.getDuration());
                if (earliestStartTime == null || (subTask.getStartTime() != null
                        && subTask.getStartTime().isBefore(earliestStartTime))) {
                    earliestStartTime = subTask.getStartTime();
                }
                if (latestEndTime == null || (subTask.getEndTime() != null
                        && subTask.getEndTime().isAfter(latestEndTime))) {
                    latestEndTime = subTask.getEndTime();
                }
            }
        }
        epicTask.setDuration(totalDuration);
        epicTask.setStartTime(earliestStartTime);
        epicTask.setEndTime(latestEndTime);
    }

    //Метод для проверки пересечения по времени новой задачи с уже имеющимися задачами.
    @Override
    public boolean isOverlapWithCurrentTasks(Task task) {
        return getPrioritizedTasks()
                .stream()
                .anyMatch(currentTask -> isOverlappedTasks(task, currentTask));
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

    //Метод для проверки пересечения по времени двух задач.
    private boolean isOverlappedTasks(Task o1, Task o2) {
        return o1.getStartTime().isBefore(o2.getEndTime()) && o2.getStartTime().isBefore(o1.getEndTime());
    }

}