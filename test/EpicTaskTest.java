import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    EpicTask epicTask;

    @BeforeEach
    void createEpicTask() {
        epicTask = new EpicTask("Тест", "Тестовый эпик", TaskStatus.NEW);
        epicTask.addSubTaskID(5);
        epicTask.addSubTaskID(7);
        epicTask.setTaskID(15);
    }

    //Проверяем получения списка ID сабтасков.
    @Test
    void getEpicSubTaskIDListTesting() {
        List<Integer> epicSubTaskIDList = epicTask.getEpicSubTaskIDList();

        //Проверяем наличие ожидаемых ID сабтасков.
        assertTrue(epicSubTaskIDList.contains(5), "ID не найден.");
        assertTrue(epicSubTaskIDList.contains(7), "ID не найден.");
    }

    //Проверяем присвоение нужного списка ID сабтасков.
    @Test
    void setEpicSubTaskIDListTesting() {
        ArrayList<Integer> newEpicSubTaskIDList = new ArrayList<>();
        newEpicSubTaskIDList.add(6);
        newEpicSubTaskIDList.add(8);
        epicTask.setEpicSubTaskIDList(newEpicSubTaskIDList);

        //Проверяем правильность замены списка
        assertTrue(epicTask.epicSubTaskIDList.contains(6), "ID не найден.");
        assertFalse(epicTask.epicSubTaskIDList.contains(7), "ID найден.");

        ArrayList<Integer> newEpicSubTaskIDList2 = new ArrayList<>();
        newEpicSubTaskIDList2.add(14);
        newEpicSubTaskIDList2.add(15);
        epicTask.setEpicSubTaskIDList(newEpicSubTaskIDList2);

        //Проверяем, что список ID сабтасков не заменится, если передать в него ID самого эпика.
        assertTrue(epicTask.epicSubTaskIDList.contains(8), "ID не найден.");
        assertFalse(epicTask.epicSubTaskIDList.contains(15), "ID найден.");
    }

    //Проверка корректности добавления ID нового сабтаска
    @Test
    void addSubTaskIDTesting() {
        epicTask.addSubTaskID(9);

        assertTrue(epicTask.epicSubTaskIDList.contains(9), "ID не найден.");

        epicTask.addSubTaskID(15);

        //Проверяем, что в список ID сабтасков не внесется ID самого эпика.
        assertFalse(epicTask.epicSubTaskIDList.contains(15), "ID найден.");
    }

    //Проверка метода полного очищения списка ID сабтасков.
    @Test
    void clearEpicSubTaskIDListTesting() {
        epicTask.clearEpicSubTaskIDList();

        assertTrue(epicTask.epicSubTaskIDList.isEmpty(), "Список не пуст!");
    }

    //Проверка метода удаления из списка ID сабтасков эпика одного определенного сабтаска по ID.
    @Test
    void removeEpicSubTaskByIDTesting() {
        epicTask.removeEpicSubTaskByID(5);

        assertTrue(epicTask.epicSubTaskIDList.contains(7), "ID не найден.");
        assertFalse(epicTask.epicSubTaskIDList.contains(5), "ID найден.");
    }

    //Проверяем корректность назначения и получения времени окончания выполнения задачи.
    @Test
    void setAndGetEndTimeTesting() {
        epicTask.setEndTime(LocalDateTime.of(2025, 10, 31, 15, 0));
        LocalDateTime dateTimeTest = epicTask.getEndTime();

        assertEquals(LocalDateTime.of(2025, 10, 31, 15, 0), dateTimeTest, "Время не совпадает.");
    }

    //Тестируем корректность метода Equals родительского класса.
    @Test
    void testParentEquals() {
        EpicTask epicTask1 = new EpicTask("Тест-1", "Тестовый эпик-1", TaskStatus.DONE);
        epicTask1.setTaskID(15);
        EpicTask epicTask2 = new EpicTask("Тест", "Тестовый эпик", TaskStatus.NEW);
        epicTask2.setTaskID(20);

        assertTrue(epicTask.equals(epicTask1));
        assertTrue(epicTask1.equals(epicTask));

        assertFalse(epicTask.equals(epicTask2));
        assertFalse(epicTask2.equals(epicTask));
    }

    //Тестируем корректность метода HashCode родительского класса.
    @Test
    void testParentHashCode() {
        EpicTask epicTask1 = new EpicTask("Тест-1", "Тестовый эпик-1", TaskStatus.DONE);
        epicTask1.setTaskID(15);
        EpicTask epicTask2 = new EpicTask("Тест", "Тестовый эпик", TaskStatus.NEW);
        epicTask2.setTaskID(20);

        assertEquals(epicTask, epicTask1);
        assertNotEquals(epicTask, epicTask2);

        assertEquals(epicTask.hashCode(), epicTask1.hashCode());
        assertNotEquals(epicTask.hashCode(), epicTask2.hashCode());
    }

    //toString() не тестируем, так как он используется только для отладки в Main.
}