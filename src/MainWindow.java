import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

// Main window class inheriting from JFrame to create a GUI window
public class MainWindow extends JFrame {
    private TaskClient taskClient;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

//Constructor for Main Window
    public MainWindow() {
        super("Time Management Application");
        initializeNetworkConnection();
        initializeComponents();
        layoutComponents();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to initialize the network connection
    private void initializeNetworkConnection() {
        try {
            taskClient = new TaskClient("localhost", 2711, this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Connection FAILED: " + e.getMessage());
            System.exit(1);
        }
    }

    // Method to initialize GUI components
    private void initializeComponents() {
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
    }

    // Method to layout GUI components
    private void layoutComponents() {
        setLayout(new BorderLayout());
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(taskPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField dueDateField = new JTextField();
        JButton addButton = new JButton("Add Task");
        JButton completeButton = new JButton("Complete Task");

        // Add components to form panel
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        formPanel.add(dueDateField);
        formPanel.add(addButton);
        formPanel.add(completeButton);

        // Set action listeners for buttons
        addButton.addActionListener(e -> {
            addTask(titleField.getText(), descField.getText(), dueDateField.getText());
            titleField.setText("");
            descField.setText("");
            dueDateField.setText("");
        });

        completeButton.addActionListener(e -> completeSelectedTask());

        add(formPanel, BorderLayout.SOUTH);
    }

    // Method to add a new task
    private void addTask(String title, String description, String dueDateString) {
        if (title.isEmpty() || description.isEmpty() || dueDateString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Text field(s) left empty. Please fill in all fields.");
            return;
        }
        if (!dueDateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD, include dashes.");
            return;
        }

        try {
            LocalDate dueDate = LocalDate.parse(dueDateString);
            Task newTask = new Task(title, description, dueDate, 1);
            taskClient.addTask(newTask);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add task: " + e.getMessage());
        }
    }

    // Method to complete a selected task
    private void completeSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            try {
                taskClient.completeTask(index);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "ERROR MARKING TASK COMPLETE: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No task selected.");
        }
    }

    // Entry point of the program
    public static void main(String[] args) {
        new MainWindow();
    }

    // Method to update the task list in the GUI
    public void updateTaskList(List<Task> tasks) {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task.toString());
        }
    }

    // Notification method when a task is completed
    public void taskCompletedNotification(String notification) {
        JOptionPane.showMessageDialog(this, notification);
    }
}
