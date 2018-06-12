package syntaxanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JackTokenizer {
    private File file;
    private String curToke;
    
    public JackTokenizer(File f){
        file = f;
    }
    
    public void tokenize() throws FileNotFoundException{
        String current;
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            current = scanner.nextLine();
            if(!current.equals("")){
                current = current.replaceAll("//.*", "").trim();
            }
        }
    }
        
    public String tokenType(String curToke){
        String out = "";
        switch(curToke){
            case "class":
            case "constructor":           
            case "function": 
            case "method":
            case "field": 
            case "static":
            case "var":
            case "int": 
            case "char": 
            case "boolean":
            case "void": 
            case "true":
            case "false":
            case "null": 
            case "this": 
            case "let": 
            case "do":
            case "if": 
            case "else": 
            case "while":
            case "return":
                out = "KEYWORD"; break;
            case "{": 
            case "}":
            case "(":
            case ")": 
            case "[": 
            case "]": 
            case ".":
            case ",": 
            case ";": 
            case "+":
            case "-":
            case "*": 
            case "/":
            case "&":
            case "|": 
            case "<": 
            case ">": 
            case "=":
            case "~":
                out = "SYMBOL"; break;
        }
        return out;
    }
    
    public String keyWord(String curToke){
        String out = "";
        switch(curToke){
            case "class": out = "CLASS"; break;
            case "method": out = "METHOD"; break;
            case "function": out = "FUNCTION"; break;
            case "constructor": out = "CONSTRUCTOR"; break;
            case "int": out = "INT"; break;
            case "boolean": out = "BOOLEAN"; break;
            case "char": out = "CHAR"; break;
            case "void": out = "VOID"; break;
            case "var": out = "VAR"; break;
            case "static": out = "STATIC"; break;
            case "field": out = "FIELD"; break;
            case "let": out = "LET"; break;
            case "do": out = "DO"; break;
            case "if": out = "IF"; break;
            case "else": out = "ELSE"; break;
            case "while": out = "WHILE"; break;
            case "return": out = "RETURN"; break;
            case "true": out = "TRUE"; break;
            case "false": out = "FALSE"; break;
            case "null": out = "NULL"; break;
            case "this": out = "THIS"; break;
        }
        return out;
    }

    public String symbol(char curToke){
        char out;
        switch(curToke){
            
        }
    }
}
