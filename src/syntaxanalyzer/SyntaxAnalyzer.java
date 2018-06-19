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
        Scene scene = new Scene(root, 900, 600);
        Label jackCode = new Label("Jack-Code");
        Label tokenizedCode = new Label("Tokenized-Code");
        Button saveButton = new Button("Save as XML File");
        
        TextArea vmText = new TextArea();
        TextArea asmText = new TextArea();
        vmText.setPrefHeight(500);
        
        root.add(jackCode, 0, 0);
        root.add(tokenizedCode, 1, 0);
        root.add(vmText, 0, 1);
        root.add(asmText, 1, 1);
        root.add(saveButton, 1, 2);
        
        //Datei angeben
        FileChooser fc = new FileChooser();
        fc.setTitle("Open VM File");
        fc.getExtensionFilters().addAll(new ExtensionFilter("Jack File", "*.jack"));
        File f = fc.showOpenDialog(primaryStage);

        //vmFile anzeigen 
        String ao = "";
        Scanner scanner = new Scanner(f);
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            ao += line + "\n";
        }
        vmText.setText(ao);
        
        JackTokenizer jt = new JackTokenizer(f);
        jt.tokenize();
        Parser p = new Parser(jt.getOut(), jt);
        p.compileClass();
        
        asmText.setText(p.getOut());
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
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
        
        primaryStage.setTitle("Project 10 - by Baris Üctas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
