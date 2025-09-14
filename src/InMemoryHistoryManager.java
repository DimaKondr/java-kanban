import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    //В taskViewHistory храним десять последних просмотренных задач.
    public ArrayList<Task> taskViewHistory = new ArrayList<>();

    //Добавление просмотренной задачи в список просмотренных задач.
    @Override
    public void addToHistory(Task someTask) {
        if (taskViewHistory.size() < 10) {
                taskViewHistory.add(someTask);
        } else {
            taskViewHistory.remove(0);
            taskViewHistory.add(someTask);
        }
    }

    //Получение списка десяти последних просмотренных задач. Возвращаем список.
    @Override
    public ArrayList<Task> getHistory() {
        return taskViewHistory;
    }
}