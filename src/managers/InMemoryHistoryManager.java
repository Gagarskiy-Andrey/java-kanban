package managers;

import tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();

    private Node first;
    private Node last;

    private static class Node {
        Node prev;
        Task task;
        Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }


    private void linkLast(Task task) {
        final Node node = new Node(last, task, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        remove(id);
        linkLast(task);
        history.put(id, last);
    }

    @Override
    public void remove(int id) {
        final Node node = history.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    public void removeNode(Node node) {
        final Node prevNode = node.prev;
        final Node nextNode = node.next;

        if (prevNode == null && nextNode == null) {
            return;
        }

        if (prevNode == null) { // first
            nextNode.prev = null;
            first = nextNode;

        } else if (nextNode == null) { // last
            prevNode.next = null;
            last = prevNode;

        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();

        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }
}
