import java.util.Map;
import java.util.List;
//Интерфейс менеджера задач.
public interface TaskManager {
    //Получение списка простых задач. Возвращаем список.
    List<Task> getTasksList();

    //Получение списка Epic-задач. Возвращаем список.
    List<EpicTask> getEpicTasksList();

    //Получение списка подзадач Epic-задач. Возвращаем список.
    List<SubTask> getSubTasksList();

    //Получение списка подзадач определенной Epic-задачи. Возвращаем список.
    List<SubTask> getSubTasksOfEpicTask(Integer epicTaskID);

    //Полное очищение списка всех простых задач. Возвращаем пустой список.
    Map<Integer, Task> clearTasksLists();

    //Полное очищение списка всех Epic-задач. Возвращаем пустой список.
    Map<Integer, EpicTask> clearEpicTasksLists();

    //Полное очищение списка всех подзадач. Возвращаем пустой список.
    Map<Integer, SubTask> clearSubTasksLists();

    //Получение простой задачи по ее ID. Возвращаем имеющуюся простую задачу.
    Task getTaskByID(Integer taskID);

    //Получение Epic-задачи по ее ID. Возвращаем имеющуюся Epic-задачу.
    EpicTask getEpicTaskByID(Integer taskID);

    //Получение подзадачи по ее ID. Возвращаем имеющуюся подзадачу.
    SubTask getSubTaskByID(Integer taskID);

    //Добавление в список новой простой задачи. Возвращаем новую простую задачу.
    Task addTask(Task task);

    //Добавление в список новой Epic-задачи. Возвращаем новую Epic-задачу.
    EpicTask addEpicTask(EpicTask epicTask);

    //Добавление в список новой подзадачи. Возвращаем новую подзадачу.
    SubTask addSubTask(SubTask subTask);

    //Обновление информации об имеющейся простой задаче. Возвращаем обновленную простую задачу.
    Task updateTask(Task updatedTask);

    //Обновление информации об имеющейся Epic-задаче. Возвращаем обновленную Epic-задачу.
    EpicTask updateEpicTask(EpicTask updatedEpicTask);

    //Обновление информации об имеющейся подзадаче. Возвращаем обновленную подзадачу.
    SubTask updateSubTask(SubTask updatedSubTask);

    //Удаление простой задачи по ее ID.
    void removeTaskByID(Integer taskID);

    //Удаление Epic-задачи по ее ID.
    void removeEpicTaskByID(Integer taskID);

    //Удаление подзадачи по ее ID.
    void removeSubTaskByID(Integer taskID);

    //Определение статуса Epic-задачи на основе статусов входящих в нее подзадач.
    void chooseEpicTaskStatus(EpicTask epicTask);

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