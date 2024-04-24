import java.io.*;
import java.net.*;
import java.util.List;

public class TaskClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private MainWindow mainWindow;

    public TaskClient(String serverAddress, int serverPort, MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        try {
            socket = new Socket(serverAddress, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            new Thread(this::handleServerResponse).start();
        } catch (IOException e) {
            System.err.println("Failed to connect to the server: " + e.getMessage());
        }
    }

    private void handleServerResponse() {
        try {
            while (true) {
                Object response = input.readObject();
                if (response instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<Task> updatedTaskList = (List<Task>) response;
                    mainWindow.updateTaskList(updatedTaskList);
                } else if (response instanceof String) {
                    String notification = (String) response;
                    mainWindow.taskCompletedNotification(notification);
                } else {
                    System.out.println("Unexpected response from server");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling server response: " + e.getMessage());
        }
    }

    public void addTask(Task task) {
        try {
            output.writeUTF("ADD");
            output.writeObject(task);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void completeTask(int index) {
        try {
            output.writeUTF("COMPLETE");
            output.writeInt(index);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
