package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    @Test
    void getDefault() {
        // prepare
        TaskManager taskManager;
        // do
        taskManager = Manager.getDefault();
        // check
        assertNotNull(taskManager);
    }

    @Test
    void getDefaultHistory() {
        // prepare
        HistoryManager historyManager;
        // do
        historyManager = Manager.getDefaultHistory();
        // check
        assertNotNull(historyManager);
    }
}