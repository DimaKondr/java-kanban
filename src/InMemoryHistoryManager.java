import java.util.*;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {
    //В taskViewHistory храним десять последних просмотренных задач.
    private Map<Integer, Node> taskViewHistory = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    //Собираем все задачи двусвязного списка в список просмотренных задач.
    @Override
    public List<Task> getHistory() {
        List<Task> tasksList  = new ArrayList<>();
        if (head != null) {
            Node node = head;
            while (node.next != null) {
                tasksList.add(node.task);
                node = node.next;
            }
            tasksList.add(node.task);
        }
        return tasksList.stream()
                .map(task -> {
                    if (task instanceof SubTask) {
                        return new SubTask((SubTask) task);
                    } else if (task instanceof EpicTask) {
                        return new EpicTask((EpicTask) task);
                    } else {
                        return new Task((Task) task);
                    }
                })
                .collect(Collectors.toList());
    }

    //Добавление просмотренной задачи в список просмотренных задач.
    @Override
    public void addToHistory(Task someTask) {
        if (taskViewHistory.containsKey(someTask.getTaskID())) {
            removeFromHistory(someTask.getTaskID());
        }
        taskViewHistory.put(someTask.getTaskID(), linkLast(someTask));
    }

    //Добавление задачи в конец двусвязного списка.
    private Node linkLast(Task task) {
        Node newNode = new Node(task);
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size += 1;
        return newNode;
    }

    //Удаление задачи из истории просмотра.
    @Override
    public void removeFromHistory(int taskID) {
        Node removedNode = taskViewHistory.remove(taskID);
        if (removedNode != null) removeNode(removedNode);
    }

    //Удаление узла из двусвязного списка.
    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (prevNode == null && nextNode == null) {
            head = null;
            tail = null;
        } else if (prevNode == null) {
            nextNode.prev = null;
            head = nextNode;
        } else if (nextNode == null) {
            prevNode.next = null;
            tail = prevNode;
        } else {
            prevNode.next = node.next;
            nextNode.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
        if (size > 0) size -= 1;
    }

}