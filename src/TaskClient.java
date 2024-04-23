import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.List;

public class TaskClient {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public TaskClient(String serverAddress, int serverPort) throws IOException { //Connect to server and setup input and output stream
        socket = new Socket(serverAddress, serverPort);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    //ADD TASK
    public void addTask(Task task) throws IOException {
        output.writeUTF("ADD");
        output.writeObject(task);
        output.flush();
        System.out.println("Response from server: " + input.readUTF());
    }

    //COMPLETE TASK
    public void completeTask(int index) throws IOException {
        output.writeUTF("COMPLETE");
        output.writeInt(index); //Index specifies the task to complete
        output.flush();
        System.out.println("Response from server: " + input.readUTF());
    }

    public void close() throws IOException {
        socket.close();
    }

    public List<Task> fetchTasks() throws IOException, ClassNotFoundException {
        output.writeUTF("GET_TASKS");  // Command the server recognizes to send back task list
        output.flush();

        Object response = input.readObject();  // Assume server sends a serialized List<Task>
        return (List<Task>) response;  // Cast and return the response
    }

    //Test
    public static void main(String[] args) {
        try {
            TaskClient client = new TaskClient("localhost", 7371); //Last four of UFID used for port number
            client.addTask(new Task("Title", "Description", LocalDate.now(), 1));
            client.completeTask(0);
            client.close();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}

