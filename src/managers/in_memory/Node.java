package managers.in_memory;

import tasks.Task;

public class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Task task) {
        this.task = task;
        this.next = null;
        this.prev = null;
    }

}