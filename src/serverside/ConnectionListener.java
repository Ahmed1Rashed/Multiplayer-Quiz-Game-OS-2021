/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverside;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class ConnectionListener extends Thread {
    private final ServerSocket serverSocket;
    private volatile Queue<Client> waitingClients;
    private int lastClientID = -1;
    
    public ConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        waitingClients = new LinkedList<>();
    }
    
    public Queue<Client> getWaitingClients() {
        return waitingClients;
    }
    
    private void startListening() {
        while(true) {
            Socket clientSocket = null;
            String clientName = null;
            try {
                clientSocket = serverSocket.accept();
                DataInputStream inputReader = new DataInputStream(clientSocket.getInputStream());
                clientName = inputReader.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(clientSocket != null) {
                System.out.println("Added client to queue.");
                waitingClients.add(new Client(clientSocket,++lastClientID,clientName));
            }
        }
    }
    
    @Override
    public void run() {
        startListening();
    }
}
