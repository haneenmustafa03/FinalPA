import java.util.ArrayList;
import java.util.List;
public class TaskManager {
    private List<Task> tasks;

    public TaskManager(){
        tasks = new ArrayList<>();
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public boolean removeTask(Task task){
        return tasks.remove(task);
    }

    public List<Task> getTasks(){
        return tasks;
    }




}