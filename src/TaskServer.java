import java.io.*;
import java.net.*;

public class TaskServer {
    private ServerSocket serverSocket;
    private TaskManager taskManager;

    public TaskServer(int port) throws IOException {
        serverSocket = new ServerSocket(port); // Create server socket
        taskManager = new TaskManager();
    }

    public void start() {
        Thread serverThread = new Thread(() -> {
            clientConnectionHandler(); // Handles client connections
        });
        serverThread.start();
    }

    private void clientConnectionHandler() {
        try {
            System.out.println("Server started. Awaiting client connection...");
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accepts client connection
                System.out.println("Client connection accepted.");

                // Handle client connection in a separate thread
                Thread clientThread = new Thread(() -> {
                    handleClient(clientSocket);
                });
                clientThread.start();
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

    private void handleClient(Socket clientSocket) {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String command;
            while ((command = input.readUTF()) != null) {
                switch (command) {
                    case "ADD":
                        Task task = (Task) input.readObject(); // Read the task object sent by the client
                        taskManager.addTask(task); // Adds the task to the task manager
                        output.writeUTF("Task added successfully"); // Let the client know that the task was successfully added
                        break;
                    case "COMPLETE":
                        int index = input.readInt();
                        taskManager.getTasks().get(index).setCompleted(true); // Set the task as completed
                        output.writeUTF("Task completed successfully"); // Let client know that complete task was successful
                        break;
                    default:
                        output.writeUTF("Command not recognized"); // Return an error for commands not recognized
                        break;
                }
                output.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            TaskServer taskServer = new TaskServer(2711);
            taskServer.start();
        } catch (IOException e) {
            System.out.println("Error! Server not started... " + e.getMessage());
        }
    }
}
