package manager;

import tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();

    private Integer lastId = null;

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

        if (history.isEmpty()) {
            first = new Node(null, task, null);
            history.put(task.getId(), first);

        } else {
            last = new Node(history.get(lastId), task, null);
            Node prevNode = history.get(lastId);
            prevNode.next = last;
            history.put(task.getId(), last);
        }

        lastId = task.getId();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        Task anotherTask = task;
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(anotherTask);
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            Node deleteNode = history.get(id);
            history.remove(id);
            removeNode(deleteNode);
        }
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
        List<Task> historyList = new ArrayList<>();
        Node anotherNode;

        if (first == null) {
            return historyList;

        } else if (last == null) {
            historyList.add(first.task);
            return historyList;

        } else {
            anotherNode = first.next;
            historyList.add(first.task);
            historyList.add(anotherNode.task);
            while (anotherNode.next != null) {
                anotherNode = anotherNode.next;
                historyList.add(anotherNode.task);
            }
        }

        return historyList;
    }
}
