package manager;

import tasks.Task;

import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> viewsHistory = new ArrayList<>();
    @Override
    public void add(Task task) {
    if (viewsHistory.size() == 10) {
        viewsHistory.remove(0);
        }
    viewsHistory.add(task);
    }
    @Override
    public ArrayList<Task> getHistory() {
        return viewsHistory;
    }


}
