package serverside;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class QuestionsParser {
    public static Question[] parseFromFile(String fileName) {
        Question[] questions = null;
        try {
            File questionsFile = new File(fileName);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document questionsDocument;
            questionsDocument = documentBuilder.parse(questionsFile);
            questionsDocument.getDocumentElement().normalize();
            NodeList questionsList = questionsDocument.getElementsByTagName("question");
            questions = new Question[questionsList.getLength()];
            for (int i = 0; i < questionsList.getLength(); i++) {
                Node node = questionsList.item(i);
                Element e = (Element) node;
                
                String questionStatement = e.getElementsByTagName("statement").item(0).getTextContent();
                String choiceA = e.getElementsByTagName("choiceA").item(0).getTextContent();
                String choiceB = e.getElementsByTagName("choiceB").item(0).getTextContent();
                String choiceC = e.getElementsByTagName("choiceC").item(0).getTextContent();
                String choiceD = e.getElementsByTagName("choiceD").item(0).getTextContent();
                char correctChoice = e.getElementsByTagName("correctChoice").item(0).getTextContent().charAt(0);
                
                questions[i] = new Question(questionStatement,choiceA,choiceB,choiceC,choiceD, correctChoice);
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(QuestionsParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questions;
    }
}
