package syntaxanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SyntaxAnalyzer extends Application {
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        
        //Szene erstellen
        String s = "";
        Scene scene = new Scene(root, 1350, 600);
        Label jackCode = new Label("Jack-Code");
        Label tokenizedCode = new Label("Tokenized-Code");
        Label parsedCode = new Label("Parsed-Code");
        
        TextArea jackText = new TextArea();
        TextArea xmlText = new TextArea();
        TextArea parsedText = new TextArea();
        jackText.setPrefHeight(500);
        
        root.add(jackCode, 0, 0);
        root.add(tokenizedCode, 1, 0);
        root.add(parsedCode, 2, 0);
        root.add(jackText, 0, 1);
        root.add(xmlText, 1, 1);
        root.add(parsedText, 2, 1);
        
        //Ordner mit JackDateien angeben
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Open Jack Directory");
        File directory = dc.showDialog(primaryStage);
        File[] files = directory.listFiles();
        
        //JackFiles anzeigen 
        String programmCode = "";
        String xmlTokensCode = "";
        String parsingCode = "";
        //Durchlaufe alle Dateien und erstelle die XML Dateien
        for (File file : files) {
            System.out.println(file.getName());
            if(file.getName().contains(".jack")){
                Scanner scanner = new Scanner(file);
                while(scanner.hasNext()){
                    String line = scanner.nextLine();
                    programmCode += line + "\n";
                }
                JackTokenizer jt = new JackTokenizer(file);
                jt.tokenize();
                jt.saveTokensXML();
                
                xmlTokensCode += jt.getXML();
                
                Parser p = new Parser(jt.getOut(), jt);
                p.compileClass();
                parsingCode += p.getOut();
                p.saveParsedXML();
            }
        }
        jackText.setText(programmCode);
        xmlText.setText(xmlTokensCode);
        parsedText.setText(parsingCode);
        primaryStage.setTitle("Project 10 - by Baris Üctas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
