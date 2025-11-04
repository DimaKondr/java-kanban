package managers.interfaces;

import tasks.Task;
import java.util.List;

public interface HistoryManager {
    //Добавление просмотренной задачи в список просмотренных задач.
    void addToHistory(Task someTask);

    //Получение списка просмотренных задач. Возвращаем список.
    List<Task> getHistory();

    //Удаление задачи из истории просмотра
    void removeFromHistory(int taskID);
}