import java.util.List;

public interface TaskManager {
    //Получение списка простых задач. Возвращаем список.
    List<Task> getTasksList();

    //Получение списка Epic-задач. Возвращаем список.
    List<EpicTask> getEpicTasksList();

    //Получение списка подзадач Epic-задач. Возвращаем список.
    List<SubTask> getSubTasksList();

    //Получение списка подзадач определенной Epic-задачи. Возвращаем список.
    List<SubTask> getSubTasksOfEpicTask(Integer epicTaskID);

    //Получение списка задач простого типа и подзадач всех Epic-задач, отсортированные по времени начала.
    List<Task> getPrioritizedTasks();

    //Полное очищение списка всех простых задач.
    void clearTasksLists();

    //Полное очищение списка всех Epic-задач.
    void clearEpicTasksLists();

    //Полное очищение списка всех подзадач.
    void clearSubTasksLists();

    //Получение простой задачи по ее ID. Возвращаем имеющуюся простую задачу.
    Task getTaskByID(Integer taskID);

    //Получение Epic-задачи по ее ID. Возвращаем имеющуюся Epic-задачу.
    EpicTask getEpicTaskByID(Integer taskID);

    //Получение подзадачи по ее ID. Возвращаем имеющуюся подзадачу.
    SubTask getSubTaskByID(Integer taskID);

    //Добавление в список новой простой задачи.
    void addTask(Task task);

    //Добавление в список новой Epic-задачи.
    void addEpicTask(EpicTask epicTask);

    //Добавление в список новой подзадачи.
    void addSubTask(SubTask subTask);

    //Обновление информации об имеющейся простой задаче.
    void updateTask(Task updatedTask);

    //Обновление информации об имеющейся Epic-задаче.
    void updateEpicTask(EpicTask updatedEpicTask);

    //Обновление информации об имеющейся подзадаче.
    void updateSubTask(SubTask updatedSubTask);

    //Удаление простой задачи по ее ID.
    void removeTaskByID(Integer taskID);

    //Удаление Epic-задачи по ее ID.
    void removeEpicTaskByID(Integer taskID);

    //Удаление подзадачи по ее ID.
    void removeSubTaskByID(Integer taskID);

    //Определение статуса Epic-задачи на основе статусов входящих в нее подзадач.
    void chooseEpicTaskStatus(EpicTask epicTask);

    //Расчет длительности, времени начала и окончания Epic-задачи на основе длительности входящих в нее подзадач.
    void calculateEpicTaskDuration(EpicTask epicTask);

    //Метод для проверки пересечения по времени новой задачи с уже имеющимися задачами.
    boolean isOverlapWithCurrentTasks(Task task);

    //Метод для генерации ID задач.
    int generateTaskID();

    //Создание копии простой задачи для сохранения в истории просмотра.
    Task createTaskCopy(Task task);

    //Создание копии эпик-задачи для сохранения в истории просмотра.
    EpicTask createEpicTaskCopy(EpicTask epicTask);

    //Создание копии подзадачи для сохранения в истории просмотра.
    SubTask createSubTaskCopy(SubTask subTask);

    //Получаем менеджера истории.
    HistoryManager getHistoryManager();

}