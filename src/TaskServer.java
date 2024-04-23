import java.io.*;
import java.net.*;

public class TaskServer {
    private ServerSocket serverSocket;
    private TaskManager taskManager;

    public TaskServer(int port) throws IOException {
        serverSocket = new ServerSocket(port); //Create server socket
        taskManager = new TaskManager();
        clientConnectionHandler(); //Handles a single client connection.
    }

    private void clientConnectionHandler() {
        try {
            System.out.println("Awaiting client connection...");
            Socket clientSocket = serverSocket.accept(); //Accepts client connection
            System.out.println("Client connection successful.");

            try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

                String command;
                while ((command = input.readUTF()) != null) {
                    switch (command) {
                        case "ADD":
                            Task task = (Task) input.readObject(); //Read the task object sent by the client
                            taskManager.addTask(task); //Adds the task to the task manager
                            output.writeUTF("Task added successfully"); //Let the client know that the task was successfully added
                            break;
                        case "COMPLETE":
                            int index = input.readInt();
                            taskManager.getTasks().get(index).setCompleted(true); //Set the task as completed
                            output.writeUTF("Task completed successfully"); //Let client know that complete task was successful
                            break;
                        default:
                            output.writeUTF("Command not recognized"); //Return an error for commands not recognized
                            break;
                    }
                    output.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing server: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            new TaskServer(77371); //Port number last 5 digits of UFID.
        } catch (IOException e) {
            System.out.println("Error! Server not started... " + e.getMessage());
        }
    }
}
