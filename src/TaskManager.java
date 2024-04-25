import java.util.ArrayList;
import java.util.List;
// TaskManager class manages a collection of Task objects
public class TaskManager {
    private List<Task> tasks; // List to store tasks

    // Constructor for TaskManager initializes the task list
    public TaskManager(){
        tasks = new ArrayList<>();
    }

    // Method to add a task to the list
    public void addTask(Task task){
        tasks.add(task);
    }

    // Method to remove a task from the list
    public boolean removeTask(Task task){
        return tasks.remove(task);
    }

    // Method to get the list of all tasks
    public List<Task> getTasks(){
        return tasks;
    } // Returns the list containing all the tasks




}