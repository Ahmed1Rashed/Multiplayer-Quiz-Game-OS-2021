package clientside;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler extends Thread {
    private final int portNumber = 9001;
    private final QuizGameClient client;
    private final String userName;
    private Socket serverSocket;
    public ConnectionHandler(QuizGameClient client, String userName) {
        serverSocket = null;
        this.client = client;
        this.userName = userName;
    }
    
    private Socket attemptConnection() {
        try {
            return(new Socket("localhost",portNumber));
        } catch (IOException ex) {
            return null;
        }
    }
    
    public void sendMessage(String message) {
        try {
            DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
            out.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void handleMessage(String message) {
        char messageTypeChar = message.charAt(0);
        String parsedMessage = message.substring(2);
        switch(messageTypeChar) {
            case 'Q':
                client.receiveQuestion(parsedMessage.split("_"));
                break;
            case 'S':
                client.receiveScoreboard(parsedMessage.split("_"));
                break;
            case 'V':
                client.receiveVerdict(Boolean.valueOf(parsedMessage));
                break;
            case 'T':
                client.receiveRoundDuration(Long.parseLong(parsedMessage));
                break;
        }
    }
    
    private void startListening() {
        try {
            DataInputStream in = new DataInputStream(serverSocket.getInputStream());
            while(true) {
                String message = in.readUTF();
                System.out.println("Received message: " + message);
                handleMessage(message);
            }
        }
        catch(IOException ex) {
            
        }
    }
    
    @Override
    public void run() {
        while(serverSocket == null) {
            serverSocket = attemptConnection();
        }
        System.out.println("Successfully connected to server.");
        sendMessage(userName);
        startListening();
    }
    
    
    
}
