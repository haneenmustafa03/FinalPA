import java.io.Serializable;
import java.time.LocalDate;

// Task class which implements Serializable to allow object serialization for storage or network transfer
public class Task implements Serializable {
    private static final long serialVersionUID = 1L; // Added serialVersionUID to avoid warnings

    // Private member variables for task details
    private String title;
    private String description;
    private LocalDate dueDate;
    private int priority;
    private boolean isTaskComplete;

    // Constructor to initialize a Task object with title, description, due date, and priority
    public Task(String title, String description, LocalDate dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    // Getter methods to access the private fields
    public String getTitle() {
        return title;
    } // Returns the task's title

    public String getDescription() {
        return description;
    } // Returns the task's description

    public LocalDate getDueDate() {
        return dueDate;
    } // Returns the task's due date

    public int getPriority() {
        return priority;
    } // Returns the task's priority

    public boolean isCompleted(){
        return isTaskComplete;
    }   // Returns the completion status of the task

    // Setter methods to modify the private fields
    public void setTitle(String title) {
        this.title = title;
    } // Sets a new title for the task

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    } // Sets a new due date for the task

    public void setDescription(String description) {
        this.description = description;
    } // Sets a new description for the task

    public void setPriority(int priority) {
        this.priority = priority;
    } // Sets a new priority level for the task

    public void setCompleted(boolean isTaskComplete){ this.isTaskComplete = isTaskComplete; } // Sets the task's completion status

    @Override
    public String toString() {

        String status = isTaskComplete ? "[Completed] " : "";
        return status + title + "-" + description + " Due: " + dueDate;
    }
}