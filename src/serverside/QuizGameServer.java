package serverside;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizGameServer {
    private final int portNumber = 9001;
    private final Map<Integer,Client> connectedClients;
    private final ConnectionListener connectionListener;
    private final Map<Integer,Boolean> answeredThisRound;
    private Question[] questions;
    private Question currentQuestion;
    private final int roundDuration = 10000;
    
    
    private Question getAnotherQuestion() {
        Question randomQuestion = questions[(int)(questions.length * Math.random())];
        while(randomQuestion == currentQuestion) {
            randomQuestion = questions[(int)(questions.length * Math.random())];
        }
        return randomQuestion;
    }
    
    private void waitForConnections() {
        Queue<Client> waitingClients = connectionListener.getWaitingClients();
        while(waitingClients.isEmpty()) {
            waitingClients = connectionListener.getWaitingClients();
        }
    }
    
    //Create (for clients)
    private void addWaitingClients() {
        Queue<Client> waitingClients = connectionListener.getWaitingClients();
        while(!waitingClients.isEmpty()) {
            Client client = waitingClients.remove();
            connectedClients.put(client.getID(),client);
            new MessageListener(this,client).start();
        }
    }
    //Read (scoreboard)
    private String getScoreboard() {
        Client[] participants = connectedClients.values().toArray(new Client[0]);
        Arrays.sort(participants,participants[0]);
        String scoreboard = "S_";
        for(Client participant : participants) {
            scoreboard += participant.getName() + " " + participant.getScore() + "_";
        }
        return scoreboard;
    }
    
    //Update (score)
    private void updateScore(int clientID, int scoreChange) {
        connectedClients.get(clientID).addScore(scoreChange);
    }
    
    //Delete (on client disconnect)
    protected void removeClient(int clientID) {
        connectedClients.remove(clientID);
    }
    
    private String getRoundDurationMessage() {
        return ("T_" + roundDuration);
    }
    
    private void sendVerdict(int clientID, boolean verdict) {
        try {
            DataOutputStream output = new DataOutputStream(connectedClients.get(clientID).getSocket().getOutputStream());
            output.writeUTF("V_" + Boolean.toString(verdict));
        } catch (IOException ex) {
            Logger.getLogger(QuizGameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void receiveAnswer(int clientID, char answer) {
        if(!answeredThisRound.containsKey(clientID)) {
            answeredThisRound.put(clientID, true);
            if(answer== currentQuestion.getCorrectChoice()) {
                sendVerdict(clientID,true);
                updateScore(clientID,100);
            }
            else {
                sendVerdict(clientID,false);
                updateScore(clientID,-50);
            }
        }
    }
    
    private void newRound() {
        if(connectedClients.isEmpty()) {
            waitForConnections();
        }
        answeredThisRound.clear();
        addWaitingClients();
        String scoreboard = getScoreboard();
        currentQuestion = getAnotherQuestion();
        String questionMessage = currentQuestion.getQuestionMessage();
        String roundDurationMessage = getRoundDurationMessage();
        for(Map.Entry<Integer,Client> client : connectedClients.entrySet()) {
            try {
                System.out.println("Sending scoreboard: " + scoreboard + " and question message: " + questionMessage + " to client id: " + client.getKey());
                DataOutputStream output = new DataOutputStream(client.getValue().getSocket().getOutputStream());
                output.writeUTF(scoreboard);
                output.writeUTF(questionMessage);
                output.writeUTF(roundDurationMessage);
            } catch (IOException ex) {
                Logger.getLogger(QuizGameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        @Override
        public void run() {
         newRound();
        }}, roundDuration);
    }
    
    public QuizGameServer(String questionsFileName) {
        questions = QuestionsParser.parseFromFile(questionsFileName);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException ex) {
            Logger.getLogger(QuizGameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionListener = new ConnectionListener(serverSocket);
        connectionListener.start();
        connectedClients = new TreeMap<>();
        answeredThisRound = new TreeMap<>();
        System.out.println("Waiting for connections..");
        waitForConnections();
        System.out.println("Connected. Starting game..");
        addWaitingClients();
        newRound();
    }
    
    public static void main(String[] args) {
        new QuizGameServer("questions.xml");
    }
    
}
