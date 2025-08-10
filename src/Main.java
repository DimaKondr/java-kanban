import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String command = scanner.nextLine();

            if (Integer.parseInt(command) == 1) { //Получаем список всех задач
                System.out.println("Список каких задач вы хотите получить?\n"
                        + "1 - Простые задачи\n2 - Epic-задачи\n3 - Подзадачи всех Epic-задач\n"
                                + "4 - Подзадачи определенной Epic-задачи");
                String taskType = scanner.nextLine();
                switch (Integer.parseInt(taskType)) {
                    case 1:
                        HashMap<Integer, Task> returnedTasksList = TaskManager.getTasksList();
                        break;
                    case 2:
                        HashMap<Integer, EpicTask> returnedEpicTasksList = TaskManager.getEpicTasksList();
                        break;
                    case 3:
                        HashMap<Integer, SubTask> returnedSubTasksList = TaskManager.getSubTasksList();
                        break;
                    case 4:
                        System.out.println("Введите ID Epic-задачи:");
                        String epicTaskID = scanner.nextLine();
                        HashMap<Integer, SubTask> returnedSubTasksOfEpicTask
                                = TaskManager.getSubTasksOfEpicTask(Integer.valueOf(epicTaskID));
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
                        HashMap<Integer, Task> returnedTasksList = TaskManager.clearTasksLists();
                        break;
                    case 2:
                        HashMap<Integer, EpicTask> returnedEpicTasksList = TaskManager.clearEpicTasksLists();
                        break;
                    case 3:
                        HashMap<Integer, SubTask> returnedSubTasksList = TaskManager.clearSubTasksLists();
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
                        Task returnedTask = TaskManager.getTaskByID(Integer.valueOf(taskID));
                        break;
                    case 2:
                        EpicTask returnedEpicTask = TaskManager.getEpicTaskByID(Integer.valueOf(taskID));
                        break;
                    case 3:
                        SubTask returnedSubTask = TaskManager.getSubTaskByID(Integer.valueOf(taskID));
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
                    Task returnedTask = TaskManager.addTask(task);
                } else if (Integer.parseInt(taskType) == 2) {
                    EpicTask epicTask = new EpicTask(title, description, taskStatus);
                    EpicTask returnedEpicTask = TaskManager.addEpicTask(epicTask);
                } else if (Integer.parseInt(taskType) == 3) {
                    System.out.println("В какую Epic-задачу вы хотите добавить подзадачу?");
                    String epicTaskID = scanner.nextLine();
                    SubTask subTask = new SubTask(title, description, taskStatus, Integer.parseInt(epicTaskID));
                    SubTask returnedSubTask = TaskManager.addSubTask(subTask);
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
                    Task updatedTask = new Task(newTitle, newDescription, chooseTaskStatus(scanner));
                    updatedTask.taskID = Integer.parseInt(taskID);
                    Task returnedTask = TaskManager.updateTask(Integer.valueOf(taskID), updatedTask);
                } else if (Integer.parseInt(taskType) == 2) {
                    EpicTask oldEpicTask = TaskManager.epicTasksList.get(Integer.valueOf(taskID));
                    ArrayList<Integer> oldEpicSubTaskIDList = oldEpicTask.epicSubTaskIDList;
                    EpicTask updatedEpicTask = new EpicTask(newTitle, newDescription, oldEpicTask.taskStatus);
                    updatedEpicTask.taskID = Integer.parseInt(taskID);
                    updatedEpicTask.epicSubTaskIDList = oldEpicSubTaskIDList;
                    EpicTask returnedEpicTask = TaskManager.updateEpicTask(Integer.valueOf(taskID), updatedEpicTask);
                } else if (Integer.parseInt(taskType) == 3) {
                    SubTask oldSubTask = TaskManager.subTasksList.get(Integer.valueOf(taskID));
                    SubTask updatedSubTask
                            = new SubTask(newTitle, newDescription, chooseTaskStatus(scanner), oldSubTask.epicTaskID);
                    updatedSubTask.taskID = Integer.parseInt(taskID);
                    SubTask returnedSubTask = TaskManager.updateSubTask(Integer.valueOf(taskID), updatedSubTask);
                } else {
                    System.out.println("Извините, такой команды пока нет. Повторите попытку");
                }
            } else if(Integer.parseInt(command) == 6) { //Удаляем задачу по ID
                System.out.println("Введите ID задачи для удаления:");
                String taskID = scanner.nextLine();
                TaskManager.removeTaskByID(Integer.valueOf(taskID));
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

    //Меню для работы через консоль
    private static void printMenu() {
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Получить список всех задач");
        System.out.println("2 - Удалить все задачи");
        System.out.println("3 - Показать задачу по ID");
        System.out.println("4 - Создать новую задачу");
        System.out.println("5 - Обновить имеющуюся задачу");
        System.out.println("6 - Удалить задачу по ID");
        System.out.println("0 - Выход");
    }

}