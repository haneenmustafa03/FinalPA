import javax.print.DocFlavor;
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
        super("Time Management Application"); //Title of JFrame
        taskManager = new TaskManager();
        initializeComponents();
        layoutComponents();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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

        //add new tasks
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

        //gives add button functionality
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask(titleField.getText(), descField.getText(), dueDateField.getText());
                titleField.setText("");
                descField.setText("");
                dueDateField.setText("");
            }
        });

        //gives complete button functionality
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completeSelectedTask();;
            }
        });

        add(formPanel, BorderLayout.SOUTH);
    }

    private void addTask(String title, String description, String dueDateString) {

        //Check if any boxes are left empty
        if(title.isEmpty() || description.isEmpty() || dueDateString.isEmpty()){
            JOptionPane.showMessageDialog(this, "Error: Text field(s) left empty. Please fill in all fields.");
            return;
        }

        //Check if date is inputted correctly
        if(!dueDateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD, include dashes.");
            return;
        }

        //Parse and make sure that year month and day values are correct
        String [] separateDateDigits = dueDateString.split("-");
        int year = Integer.parseInt(separateDateDigits[0]);
        int month = Integer.parseInt(separateDateDigits[1]);
        int day = Integer.parseInt(separateDateDigits[2]);

        //Validate month is between one and 12, and day is between 1 and 31
        if (month < 1 || month > 12 || day < 1 || day > 31){
            JOptionPane.showMessageDialog(this,"Invalid date vales. Please follow correct format YYYY-MM-DD.");
            return;
        }

        //Validate single digit months have a zero in front
        if(month < 10 && separateDateDigits[1].charAt(0) != '0'){
            JOptionPane.showMessageDialog(this, "Invalid month format. Use 0M for single digit months.");
            return;
        }

        //Validate single digit days
        if(day < 10 && separateDateDigits[2].charAt(0) != '0'){
            JOptionPane.showMessageDialog(this, "Invalid month format. Use 0M for single digit months.");
            return;
        }

        //Adds task with correct due date
        LocalDate dueDate = LocalDate.parse(dueDateString); //Parse date string
        Task newTask = new Task(title, description, dueDate, 1); //Priority is fixed for simplicity
        taskManager.addTask(newTask);
        updateTaskList();
    }

    private void loadTasks() {
        taskListModel.clear();
        for (Task task : taskManager.getTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    private void updateTaskList() {
        loadTasks();
    }

    private void completeSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            //Retrieves selected task
            Task selectedTask = taskManager.getTasks().get(index);

            //Marks the task selected as complete
            selectedTask.setCompleted(true);

            updateTaskList();
        } else {
            JOptionPane.showMessageDialog(this, "No task selected.");
        }

    }


    public static void main(String[] args) {
        new MainWindow();
    }

}


