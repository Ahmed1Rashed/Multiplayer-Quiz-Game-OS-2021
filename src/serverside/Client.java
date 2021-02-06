/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverside;

import java.net.Socket;
import java.util.Comparator;

/**
 *
 * @author hp
 */
public class Client implements Comparator<Client> {
    private Socket clientSocket;
    private int clientID;
    private String clientName;
    private int score;
    
    public Client(Socket clientSocket, int clientID, String clientName) {
        this.clientSocket = clientSocket;
        this.clientID = clientID;
        this.clientName = clientName;
        score = 0;
    }
    
    public int getScore() {
        return score;
    }
    
    public void addScore(int increment) {
        score += increment;
    }
    
    public void setName(String clientName) {
        this.clientName = clientName;
    }
    
    public void setID(int clientID) {
        this.clientID = clientID;
    }
    
    public void setSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    public String getName() {
        return clientName;
    }
    
    public Socket getSocket() {
        return clientSocket;
    }
    
    public int getID() {
        return clientID;
    }
    
    @Override
    public int compare(Client c1, Client c2) {
        return (Integer.compare(c2.getScore(),c1.getScore()));
    }
    
}
