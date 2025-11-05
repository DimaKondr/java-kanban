package managers;

import managers.in_memory.InMemoryHistoryManager;
import managers.in_memory.InMemoryTaskManager;
import managers.interfaces.HistoryManager;
import managers.interfaces.TaskManager;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}