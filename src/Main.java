import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = taskManager.getHistoryManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String command = scanner.nextLine();

            if (Integer.parseInt(command) == 1) { //Получаем список всех задач
                System.out.println("Список каких задач вы хотите получить?\n"
                        + "1 - Простые задачи\n2 - Epic-задачи\n3 - Подзадачи всех Epic-задач\n"
                        + "4 - Подзадачи определенной Epic-задачи\n5 - Показать все задачи");
                String taskType = scanner.nextLine();
                switch (Integer.parseInt(taskType)) {
                    case 1:
                        ArrayList<Task> returnedTasksList = taskManager.getTasksList();
                        returnedTasksList.forEach(System.out::println);
                        break;
                    case 2:
                        ArrayList<EpicTask> returnedEpicTasksList = taskManager.getEpicTasksList();
                        returnedEpicTasksList.forEach(System.out::println);
                        break;
                    case 3:
                        ArrayList<SubTask> returnedSubTasksList = taskManager.getSubTasksList();
                        returnedSubTasksList.forEach(System.out::println);
                        break;
                    case 4:
                        System.out.println("Введите ID Epic-задачи:");
                        String epicTaskID = scanner.nextLine();
                        ArrayList<SubTask> returnedSubTasksOfEpicTask
                                = taskManager.getSubTasksOfEpicTask(Integer.valueOf(epicTaskID));
                        returnedSubTasksOfEpicTask.forEach(System.out::println);
                        break;
                    case 5:
                        printAllTasks(taskManager, historyManager);
                        break;
                    default:
                        System.out.println("Такая команда отсутствует. Повторите попытку.");
                }
            } else if (Integer.parseInt(command) == 2) { //Удаляем все задачи
                System.out.println("Список каких типов задач вы хотите очистить?\n"
                        + "1 - Простые задачи\n2 - Epic-задачи\n3 - Подзадачи Epic-задач");
                String taskType = scanner.nextLine();
                switch (Integer.parseInt(taskType)) {
                    case 1:
                        HashMap<Integer, Task> returnedTasksList = taskManager.clearTasksLists();
                        System.out.println("Список задач очищен");
                        break;
                    case 2:
                        HashMap<Integer, EpicTask> returnedEpicTasksList = taskManager.clearEpicTasksLists();
                        System.out.println("Список Epic-задач очищен");
                        break;
                    case 3:
                        HashMap<Integer, SubTask> returnedSubTasksList = taskManager.clearSubTasksLists();
                        System.out.println("Список подзадач Epic-задач очищен");
                        break;
                    default:
                        System.out.println("Такая команда отсутствует. Повторите попытку.");
                }
            } else if (Integer.parseInt(command) == 3) { //Показываем задачу по ID
                System.out.println("Какого типа задачу вы хотите найти?\n"
                        + "1 - Простые задачи\n2 - Epic-задачи\n3 - Подзадачи Epic-задач");
                String taskType = scanner.nextLine();
                System.out.println("Введите ID задачи:");
                String taskID = scanner.nextLine();
                switch (Integer.parseInt(taskType)) {
                    case 1:
                        Task returnedTask = taskManager.getTaskByID(Integer.valueOf(taskID));
                        if (returnedTask == null) {
                            System.out.println("Задачи с ID '" + taskID + "' не найдено.");
                        } else {
                            System.out.println(returnedTask);
                        }
                        break;
                    case 2:
                        EpicTask returnedEpicTask = taskManager.getEpicTaskByID(Integer.valueOf(taskID));
                        if (returnedEpicTask == null) {
                            System.out.println("Epic-задачи с ID '" + taskID + "' не найдено.");
                        } else {
                            System.out.println(returnedEpicTask);
                        }
                        break;
                    case 3:
                        SubTask returnedSubTask = taskManager.getSubTaskByID(Integer.valueOf(taskID));
                        if (returnedSubTask == null) {
                            System.out.println("Подзадачи с ID '" + taskID + "' не найдено.");
                        } else {
                            System.out.println(returnedSubTask);
                        }
                        break;
                    default:
                        System.out.println("Такая команда отсутствует. Повторите попытку.");
                }
            } else if (Integer.parseInt(command) == 4) { //Создаем новую задачу
                System.out.println("Введите название задачи:");
                String title = scanner.nextLine();
                System.out.println("Введите описание задачи:");
                String description = scanner.nextLine();
                TaskStatus taskStatus = TaskStatus.NEW;
                System.out.println("Какой тип задачи вы хотите добавить?\n"
                        + "1 - Простая задача\n2 - Epic-задача\n3 - Подзадача Epic-задачи");
                String taskType = scanner.nextLine();
                if (Integer.parseInt(taskType) == 1) {
                    Task task = new Task(title, description, taskStatus);
                    Task returnedTask = taskManager.addTask(task);
                    System.out.println("Создана новая задача: " + returnedTask);
                } else if (Integer.parseInt(taskType) == 2) {
                    EpicTask epicTask = new EpicTask(title, description, taskStatus);
                    EpicTask returnedEpicTask = taskManager.addEpicTask(epicTask);
                    System.out.println("Создана новая задача: " + returnedEpicTask);
                } else if (Integer.parseInt(taskType) == 3) {
                    System.out.println("В какую Epic-задачу вы хотите добавить подзадачу?");
                    String epicTaskID = scanner.nextLine();
                    SubTask subTask = new SubTask(title, description, taskStatus, Integer.parseInt(epicTaskID));
                    SubTask returnedSubTask = taskManager.addSubTask(subTask);
                    System.out.println("Добавлена новая подзадача: " + returnedSubTask);
                } else {
                    System.out.println("Извините, такой команды пока нет.");
                }
            } else if (Integer.parseInt(command) == 5) { //Обновляем имеющуюся задачу
                System.out.println("Введите название задачи:");
                String newTitle = scanner.nextLine();
                System.out.println("Введите описание задачи:");
                String newDescription = scanner.nextLine();
                System.out.println("Какой тип задачи вы хотите обновить?\n"
                        + "1 - Простая задача\n2 - Epic-задача\n3 - Подзадача Epic-задачи");
                String taskType = scanner.nextLine();
                System.out.println("Введите ID задачи, которую хотите обновить:");
                String taskID = scanner.nextLine();
                if (Integer.parseInt(taskType) == 1) {
                    Task oldTask = null;
                    ArrayList<Task> taskArrayList = taskManager.getTasksList();
                    for (Task task : taskArrayList) {
                        if (task.getTaskID() == Integer.parseInt(taskID)) oldTask = task;
                    }
                    Task updatedTask = new Task(newTitle, newDescription, chooseTaskStatus(scanner));
                    updatedTask.setTaskID(oldTask.getTaskID());
                    Task returnedTask = taskManager.updateTask(updatedTask);
                    System.out.println("Задача обновлена: " + returnedTask);
                } else if (Integer.parseInt(taskType) == 2) {
                    EpicTask oldEpicTask = null;
                    ArrayList<EpicTask> epicTaskArrayList = taskManager.getEpicTasksList();
                    for (EpicTask epicTask : epicTaskArrayList) {
                        if (epicTask.getTaskID() == Integer.parseInt(taskID)) oldEpicTask = epicTask;
                    }
                    EpicTask updatedEpicTask = new EpicTask(newTitle, newDescription, TaskStatus.NEW);
                    updatedEpicTask.setTaskID(oldEpicTask.getTaskID());
                    EpicTask returnedEpicTask = taskManager.updateEpicTask(updatedEpicTask);
                    System.out.println("Эпик-задача обновлена: " + returnedEpicTask);
                } else if (Integer.parseInt(taskType) == 3) {
                    SubTask oldSubTask = null;
                    ArrayList<SubTask> subTaskArrayList = taskManager.getSubTasksList();
                    for (SubTask subTask : subTaskArrayList) {
                        if (subTask.getTaskID() == Integer.parseInt(taskID)) oldSubTask = subTask;
                    }
                    SubTask updatedSubTask = new SubTask(newTitle, newDescription,
                            chooseTaskStatus(scanner), oldSubTask.getEpicTaskID());
                    updatedSubTask.setTaskID(oldSubTask.getTaskID());
                    SubTask returnedSubTask = taskManager.updateSubTask(updatedSubTask);
                    System.out.println("Подзадача обновлена: " + returnedSubTask);
                } else {
                    System.out.println("Извините, такой команды пока нет. Повторите попытку");
                }
            } else if (Integer.parseInt(command) == 6) { //Удаляем задачу по ID
                System.out.println("Какого типа задачу вы хотите удалить?\n"
                        + "1 - Простые задачи\n2 - Epic-задачи\n3 - Подзадачи Epic-задач");
                String taskType = scanner.nextLine();
                System.out.println("Введите ID задачи для удаления:");
                String taskID = scanner.nextLine();
                switch (Integer.parseInt(taskType)) {
                    case 1:
                        taskManager.removeTaskByID(Integer.valueOf(taskID));
                        System.out.println("Задача удалена");
                        break;
                    case 2:
                        taskManager.removeEpicTaskByID(Integer.valueOf(taskID));
                        System.out.println("Задача удалена");
                        break;
                    case 3:
                        taskManager.removeSubTaskByID(Integer.valueOf(taskID));
                        System.out.println("Задача удалена");
                        break;
                    default:
                        System.out.println("Такая команда отсутствует. Повторите попытку.");
                }
            } else if(Integer.parseInt(command) == 7) { //Показываем десять последних просмотренных задач
                ArrayList<Task> taskViewHistory = historyManager.getHistory();
                System.out.println(taskViewHistory);
            } else if (Integer.parseInt(command) == 0) { //Завершаем работу
                System.out.println("Выход");
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    //Вспомогательный метод для определения статуса задачи, которую Пользователь хочет обновить.
    private static TaskStatus chooseTaskStatus(Scanner scanner) {
        TaskStatus newTaskStatus = null;
        System.out.println("Выберите статус задачи:\n1 - NEW\n2 - IN_PROGRESS\n3 - DONE");
        String status = scanner.nextLine();
        while (newTaskStatus == null) {
            switch (Integer.parseInt(status)) {
                case 1:
                    newTaskStatus = TaskStatus.NEW;
                    break;
                case 2:
                    newTaskStatus = TaskStatus.IN_PROGRESS;
                    break;
                case 3:
                    newTaskStatus = TaskStatus.DONE;
                    break;
                default:
                    System.out.println("Такая команда отсутствует. Повторите попытку.");
                    break;
            }
        }
        return newTaskStatus;
    }

    //Вывод всех задач и списка десяти последних просмотренных задач.
    private static void printAllTasks(TaskManager taskManager, HistoryManager historyManager) {
        System.out.println("Простые задачи:");
        for (Task task : taskManager.getTasksList()) {
            int taskID = task.getTaskID();
            System.out.println(taskManager.getTaskByID(taskID));
        }
        System.out.println("Epic-задачи:");
        for (EpicTask epicTask : taskManager.getEpicTasksList()) {
            int taskID = epicTask.getTaskID();
            System.out.println(taskManager.getEpicTaskByID(taskID));
        }
        System.out.println("Подзадачи Epic-задач:");
        for (SubTask subTask : taskManager.getSubTasksList()) {
            int taskID = subTask.getTaskID();
            System.out.println(taskManager.getSubTaskByID(taskID));
        }

        System.out.println("Десять последних просмотренных задач:");
        System.out.println(historyManager.getHistory());
    }

    //Меню для работы через консоль
    private static void printMenu() {
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Получить список всех задач");
        System.out.println("2 - Удалить все задачи");
        System.out.println("3 - Показать задачу по ID");
        System.out.println("4 - Создать новую задачу");
        System.out.println("5 - Обновить имеющуюся задачу");
        System.out.println("6 - Удалить задачу по ID");
        System.out.println("7 - Показать десять последних просмотренных задач");
        System.out.println("0 - Выход");
    }

}