import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class TaskServer {
    private ServerSocket serverSocket;
    private TaskManager taskManager;

    public TaskServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        taskManager = new TaskManager();
    }

    public void start() {
        Thread serverThread = new Thread(this::clientConnectionHandler);
        serverThread.start();
    }

    private void clientConnectionHandler() {
        try {
            System.out.println("Server started. Awaiting client connection...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connection accepted.");
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
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
                        Task task = (Task) input.readObject();
                        taskManager.addTask(task);
                        output.writeUnshared(taskManager.getTasks());
                        break;
                    case "COMPLETE":
                        int index = input.readInt();
                        taskManager.getTasks().get(index).setCompleted(true);
                        output.writeUTF("Task completed: " + taskManager.getTasks().get(index).getTitle());
                        break;
                    default:
                        output.writeUTF("Command not recognized");
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
