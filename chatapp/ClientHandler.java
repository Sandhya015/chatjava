import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final List<ClientHandler> clients;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(inputLine)) {
                    break;
                }

                // Broadcast the message to all connected clients
                broadcastMessage(inputLine);
            }

            // Remove the client from the list after they disconnect
            clients.remove(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            if (client != this) {
                client.sendMessage(message);
            }
        }
    }

    private void sendMessage(String message) {
        out.println(message);
    }
}
