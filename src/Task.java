import java.time.LocalDate;

public class Task {
    private String title;
    private String description;
    private LocalDate dueDate;
    private int priority;
    private boolean isTaskComplete;

    public Task(String title, String description, LocalDate dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    //getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted(){
        return isTaskComplete;
    }

    //setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCompleted(boolean isTaskComplete){ this.isTaskComplete = isTaskComplete; }

    @Override
    public String toString() {

        String status = isTaskComplete ? "[Completed] " : "";
        return status + title + " Due: " + dueDate;
    }
}