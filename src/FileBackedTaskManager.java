import java.io.*;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File tasksStorageFile; //Здесь храним путь к файлу для сохранения задач.

    public FileBackedTaskManager(File tasksStorageFile) {
        super();
        this.tasksStorageFile = tasksStorageFile;
    }

    //Полное очищение списка всех простых задач в файле.
    @Override
    public void clearTasksLists() {
        super.clearTasksLists();
        saveTasksToFile();
    }

    //Полное очищение списка всех Epic-задач в файле.
    @Override
    public void clearEpicTasksLists() {
        super.clearEpicTasksLists();
        saveTasksToFile();
    }

    //Полное очищение списка всех подзадач в файле.
    @Override
    public void clearSubTasksLists() {
        super.clearSubTasksLists();
        saveTasksToFile();
    }

    //Добавление в файл новой простой задачи.
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        saveTasksToFile();
    }

    //Добавление в файл новой Epic-задачи.
    @Override
    public void addEpicTask(EpicTask epicTask) {
        super.addEpicTask(epicTask);
        saveTasksToFile();
    }

    //Добавление в файл новой подзадачи.
    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        saveTasksToFile();
    }

    //Обновление информации в файле об имеющейся простой задаче.
    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        saveTasksToFile();
    }

    //Обновление информации в файле об имеющейся Epic-задаче.
    @Override
    public void updateEpicTask(EpicTask updatedEpicTask) {
        super.updateEpicTask(updatedEpicTask);
        saveTasksToFile();
    }

    //Обновление информации в файле об имеющейся подзадаче.
    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        super.updateSubTask(updatedSubTask);
        saveTasksToFile();
    }

    //Удаление из файла простой задачи по ее ID.
    @Override
    public void removeTaskByID(Integer taskID) {
        super.removeTaskByID(taskID);
        saveTasksToFile();
    }

    //Удаление из файла Epic-задачи по ее ID.
    @Override
    public void removeEpicTaskByID(Integer taskID) {
        super.removeEpicTaskByID(taskID);
        saveTasksToFile();
    }

    //Удаление из файла подзадачи по ее ID.
    @Override
    public void removeSubTaskByID(Integer taskID) {
        super.removeSubTaskByID(taskID);
        saveTasksToFile();
    }

    //Сохраняем все задачи в файл.
    public void saveTasksToFile() {
        try (Writer fileWriter = new FileWriter(tasksStorageFile)) {
            StringBuilder tasksForFile = new StringBuilder("id,type,title,status,description,epic\n");
            for (Task task : getTasksList()) {
                tasksForFile.append(task.toStringForFile(task)).append("\n");
            }
            for (EpicTask epicTask : getEpicTasksList()) {
                tasksForFile.append(epicTask.toStringForFile(epicTask)).append("\n");
            }
            for (SubTask subTask : getSubTasksList()) {
                tasksForFile.append(subTask.toStringForFile(subTask)).append("\n");
            }
            fileWriter.write(tasksForFile.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла!");
        }
    }

    //Выгружаем из файла все сохраненные в файле задачи.
    static FileBackedTaskManager loadTasksFromFile(File tasksStorageFile) {
        if (!tasksStorageFile.exists()) {
            throw new ManagerSaveException("Файл не существует!");
        }
        FileBackedTaskManager filedBackedTaskManager = new FileBackedTaskManager(tasksStorageFile);
        String allTasks;
        try {
            allTasks = Files.readString(tasksStorageFile.toPath());
            if (allTasks.isBlank()) {
                return filedBackedTaskManager;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла!");
        }
        String[] splitStorageFile = allTasks.split("\n");
        for (int i = 1; i < splitStorageFile.length; i++) {
            String[] splitTask = splitStorageFile[i].split(",");
            if (TaskType.valueOf(splitTask[1]).equals(TaskType.TASK)) {
                Task task = Task.createTaskFromFile(splitStorageFile[i]);
                filedBackedTaskManager.tasksList.put(task.getTaskID(), task);
            } else if (TaskType.valueOf(splitTask[1]).equals(TaskType.EPICTASK)) {
                EpicTask epicTask = EpicTask.createEpicTaskFromFile(splitStorageFile[i]);
                filedBackedTaskManager.epicTasksList.put(epicTask.getTaskID(), epicTask);
            } else {
                SubTask subTask =  SubTask.createSubTaskFromFile(splitStorageFile[i]);
                filedBackedTaskManager.subTasksList.put(subTask.getTaskID(), subTask);
            }
        }
        return filedBackedTaskManager;
    }

    //Реализуем пользовательский сценарий.
    public static void main(String[] args) {
        Task task1 = new Task("task1", "taskDescription", TaskStatus.NEW);
        Task task2 = new Task("task2", "taskDescription_11", TaskStatus.IN_PROGRESS);
        EpicTask epicTask1 = new EpicTask("epic1", "epicDescription", TaskStatus.NEW);
        EpicTask epicTask2 = new EpicTask("epic2", "epicDescription_11", TaskStatus.NEW);
        SubTask subTask1 = new SubTask("subTask1", "subDescription", TaskStatus.DONE, 3);
        SubTask subTask2 = new SubTask("subTask2", "subDescription_11", TaskStatus.NEW, 4);
        File tasksStorageFile = new File("TasksStorageFile.csv");
        try {
            tasksStorageFile.createNewFile();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла!");
        }
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(tasksStorageFile);
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpicTask(epicTask1);
        fileBackedTaskManager.addEpicTask(epicTask2);
        fileBackedTaskManager.addSubTask(subTask1);
        fileBackedTaskManager.addSubTask(subTask2);
        ((FileBackedTaskManager) fileBackedTaskManager).saveTasksToFile();

        TaskManager newManager = FileBackedTaskManager.loadTasksFromFile(tasksStorageFile);

        System.out.println(newManager.getTasksList());
        System.out.println(newManager.getEpicTasksList());
        System.out.println(newManager.getSubTasksList());
    }

}