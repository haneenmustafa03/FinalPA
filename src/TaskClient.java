import java.io.*;
import java.net.*;
import java.time.LocalDate;

public class TaskClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public TaskClient(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort); // Connect to the server
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Failed to connect to the server: " + e.getMessage());
        }
    }

    public void addTask(Task task) {
        try {
            output.writeUTF("ADD"); // Send command to server
            output.writeObject(task); // Send the Task object
            output.flush();
            System.out.println(input.readUTF()); // Receive confirmation from server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void completeTask(int index) {
        try {
            output.writeUTF("COMPLETE"); // Send command to server
            output.writeInt(index); // Send index of task to complete
            output.flush();
            System.out.println(input.readUTF()); // Receive confirmation from server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
