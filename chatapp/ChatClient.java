import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);

            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to the chat server. Type 'exit' to close the chat.");

            // Reading messages from the server
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = serverIn.readLine()) != null) {
                        System.out.println("Server: " + serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Sending messages to the server
            String userMessage;
            while (true) {
                userMessage = userInput.readLine();
                serverOut.println(userMessage);

                if ("exit".equalsIgnoreCase(userMessage)) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
