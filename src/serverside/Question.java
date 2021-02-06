/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverside;

public class Question {
    private String questionStatement;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private char correctChoice;
    
    public String getQuestionStatement() {
        return questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public char getCorrectChoice() {
        return correctChoice;
    }

    public void setCorrectChoice(char correctChoice) {
        this.correctChoice = correctChoice;
    }
    
    public Question(String questionStatement, String choiceA, String choiceB, String choiceC, String choiceD, char correctChoice) {
        this.questionStatement = questionStatement;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correctChoice = correctChoice;
    }
    
    public String getQuestionMessage() {
        String questionMessage = "Q_"+questionStatement + "_" + choiceA + "_" + choiceB + "_" + choiceC + "_" + choiceD;
        return questionMessage;
    }
}
