import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MainWindow extends JFrame {
    private TaskManager taskManager;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    public MainWindow() {
        super("Time Management Application"); // Set the title of the JFrame
        taskManager = new TaskManager();
        initializeComponents();
        layoutComponents();
        pack(); // Size the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
        setVisible(true); // Make the frame visible
    }

    private void initializeComponents() {
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        loadTasks();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout()); // Set the layout of the JFrame
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(taskPanel, BorderLayout.CENTER);

        // Form to add new tasks
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField dueDateField = new JTextField();
        JButton addButton = new JButton("Add Task");

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descField);
        formPanel.add(new JLabel("Due Date (YYYY-MM-DD):"));
        formPanel.add(dueDateField);
        formPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask(titleField.getText(), descField.getText(), dueDateField.getText());
                titleField.setText("");
                descField.setText("");
                dueDateField.setText("");
            }
        });

        add(formPanel, BorderLayout.SOUTH);
    }

    private void addTask(String title, String description, String dueDateString) {
        LocalDate dueDate = LocalDate.parse(dueDateString); // Parse the date string
        Task newTask = new Task(title, description, dueDate, 1); // Priority is fixed for simplicity
        taskManager.addTask(newTask);
        updateTaskList();
    }

    private void loadTasks() {
        // Here, you could load tasks from a file or database in a real application
        taskListModel.clear();
        for (Task task : taskManager.getTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    private void updateTaskList() {
        loadTasks();
    }

    public static void main(String[] args) {
        new MainWindow(); // Launch the application
    }
}




