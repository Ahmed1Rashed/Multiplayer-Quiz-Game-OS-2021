/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientside;

import view.GUI;

/**
 *
 * @author hp
 */
public class QuizGameClient {
    private final ConnectionHandler connectionHandler;
    private final GUI gui;
    
    public void receiveQuestion(String[] question) {
        gui.receiveQuestion(question);
    }
    
    public void receiveRoundDuration(long roundDuration) {
        gui.receiveRoundDuration(roundDuration);
    }
    
    public void receiveScoreboard(String[] scoreboard) {
        gui.receiveScoreboard(scoreboard);
    }
    
    public void receiveVerdict(Boolean verdict) {
        gui.receiveVerdict(verdict);
    }
    
    public void sendToServer(String message) {
        connectionHandler.sendMessage(message);
    }
    
    public QuizGameClient() {
        gui = new GUI(this);
        gui.setVisible(true);
        String userName = gui.requestName();
        connectionHandler = new ConnectionHandler(this,userName);
        connectionHandler.start();
    }
    
    public static void main(String[] args) {
        new QuizGameClient();
    }
}
