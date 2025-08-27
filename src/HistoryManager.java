import java.util.ArrayList;

public interface HistoryManager {
    //Добавление просмотренной задачи в список просмотренных задач.
    void addToHistory(Task someTask);

    //Получение списка десяти последних просмотренных задач. Возвращаем список.
    ArrayList<Task> getHistory();
}