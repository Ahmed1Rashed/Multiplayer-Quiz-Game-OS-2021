/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverside;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author hp
 */
public class MessageListener extends Thread {
    private final QuizGameServer server;
    private final Client client;
    public MessageListener(QuizGameServer server, Client client) {
        this.server = server;
        this.client = client;
    }
    
    public void startListening() {
        try {
            DataInputStream inputReader = new DataInputStream(client.getSocket().getInputStream());
            while(true) {
                System.out.println("Server waiting for messages..");
                char answer = inputReader.readUTF().charAt(0);
                System.out.println("Received answer: " + answer);
                server.receiveAnswer(client.getID(), answer);
            }
        } catch (IOException ex) {
            server.removeClient(client.getID());
        }
    }
    
    @Override
    public void run() {
        startListening();
    }
    
}
