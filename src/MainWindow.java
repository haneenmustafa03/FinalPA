import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MainWindow extends JFrame {
    private TaskClient taskClient;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    public MainWindow() {
        super("Time Management Application"); //Title of JFrame, maybe change it to something more interesting later?
        initializeNetworkConnection();
        initializeComponents();
        layoutComponents();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeNetworkConnection() {
        try {
            taskClient = new TaskClient("localhost", 7371); //Port number is last 4 of UFID
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Connection FAILED: " + e.getMessage());
            System.exit(1); //exits if connection fails
        }
    }

    private void initializeComponents() {
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        loadTasks();
    }

    //Using Java SWING for the gui
    private void layoutComponents() {
        setLayout(new BorderLayout());
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(taskPanel, BorderLayout.CENTER);

        //Add tasks
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField dueDateField = new JTextField();
        JButton addButton = new JButton("Add Task");
        JButton completeButton = new JButton("Complete Task");

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        formPanel.add(dueDateField);
        formPanel.add(addButton);
        formPanel.add(completeButton);

        //Gives add button functionality
        addButton.addActionListener(e -> {
            addTask(titleField.getText(), descField.getText(), dueDateField.getText());
            titleField.setText("");
            descField.setText("");
            dueDateField.setText("");
        });

        //Gives complete button functionality
        completeButton.addActionListener(e -> completeSelectedTask());

        add(formPanel, BorderLayout.SOUTH);
    }

    private void addTask(String title, String description, String dueDateString) {
        // Validate input
        if (title.isEmpty() || description.isEmpty() || dueDateString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Text field(s) left empty. Please fill in all fields.");
            return;
        }
        if (!dueDateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD, include dashes.");
            return;
        }

        try {
            LocalDate dueDate = LocalDate.parse(dueDateString); // Parse the date string
            Task newTask = new Task(title, description, dueDate, 1); // Priority is fixed for simplicity
            taskClient.addTask(newTask); // Send task to server to add
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add task: " + e.getMessage());
        }
    }

    private void loadTasks() {
        try {
            List<Task> tasks = taskClient.fetchTasks();  // Fetch the task list from the server
            taskListModel.clear();  // Clear existing contents
            for (Task task : tasks) {
                taskListModel.addElement(task.toString());  // Add each task to the list model
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks from server: " + e.getMessage());
            taskListModel.addElement("Failed to load tasks, try refreshing!");
        }
    }

    private void updateTaskList() {
        loadTasks();
    }

    private void completeSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            try {
                taskClient.completeTask(index); //This asks the server to mark the specified task complete (using index)
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "ERROR MARKING TASK COMPLETE: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No task selected.");
        }
    }

    //main is only one line yay
    public static void main(String[] args) {
        new MainWindow();
    }
}
