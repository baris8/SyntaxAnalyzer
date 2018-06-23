package syntaxanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
        Button saveButton = new Button("Save as XML File");
        
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
        root.add(saveButton, 1, 2);
        
        //Ordner mit JackDateien angeben
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Open Jack Directory");
        //dc.getExtensionFilters().addAll(new ExtensionFilter("Jack File", "*.jack"));
        File directory = dc.showDialog(primaryStage);
        File[] files = directory.listFiles();
        
        //JackFiles anzeigen 
        String programmCode = "";
        String xmlTokensCode = "";
        String parsingCode = "";
        
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
                
                xmlTokensCode += jt.getXML();
                
                Parser p = new Parser(jt.getOut(), jt);
                p.compileClass();
                parsingCode += p.getOut();
            }
            programmCode += "\n";
        }
        
        jackText.setText(programmCode);
        xmlText.setText(xmlTokensCode);
        parsedText.setText(parsingCode);
        
       /** saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PrintWriter writer;
                try {
                    String name = f.getName().replace(".jack", "T");
                    writer = new PrintWriter(name + ".xml");
                    writer.println(jt.getXML());
                    writer.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SyntaxAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        */
        primaryStage.setTitle("Project 10 - by Baris Üctas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
