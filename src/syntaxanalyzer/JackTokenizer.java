package syntaxanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JackTokenizer {
    private File file;
    private String curToke;
    private String out;
    
    public JackTokenizer(File f){
        file = f;
        out = "<tokens>\n";
    }
    
    public void tokenize() throws FileNotFoundException{
        String current;
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            current = scanner.nextLine();
            if(!current.equals("")){
                current = current.replaceAll("//.*", "").trim();
                String[] curWords = current.split(" ");
                for(int i = 0; i < curWords.length; i++){
                    String s = "";
                    int j = 0;
                    curWords[i] = curWords[i].replaceAll("\t", "");

                    while(j < curWords[i].length()){
                        if(Character.isDigit(curWords[i].charAt(j))){
                            out += "<integerConstant> ";
                            while(Character.isDigit(curWords[i].charAt(j))){
                                out += curWords[i].charAt(j);
                                j++;
                            }
                            out += " </integerConstant>\n";
                            out += tokenType(";");
                        }else if(curWords[i].charAt(j) == ';' ||  curWords[i].charAt(j) == '.' || curWords[i].charAt(j) == '(' ||
                            curWords[i].charAt(j) == ')' || curWords[i].charAt(j) == ',' || curWords[i].charAt(j) == ']' || curWords[i].charAt(j) == '['){
                            out += tokenType(s);
                            s = "";
                            out += tokenType(""+curWords[i].charAt(j));
                            curWords[i] = curWords[i].substring(j);
                            j = 0;
                        }else if(curWords[i].charAt(j) == '\"'){
                            out += tokenType(s);
                            out += "<stringConstant>";
                            curWords[i] = curWords[i].substring(j+1);
                            int z = i;

                            while(!curWords[z].contains("\"") && z < curWords.length){
                                out += " " + curWords[z];
                                z++;
                            }
                            System.out.println(curWords[z]);
                            out += " </stringConstant>\n";
                            out += tokenType(")");
                            i = z;
                            s = "";
                        }else{
                            s += curWords[i].charAt(j);
                        }
                        j++;
                    }
                    out += tokenType(s);
                }
            }
        }
        out += "</tokens>";
    }
      
    
    
    public String tokenType(String curToke){
        String out = "";
        switch(curToke){
            case "class": case "constructor": case "function": 
            case "method": case "field": case "static": case "var":
            case "int": case "char": case "boolean":
            case "void": case "true": case "false":
            case "null": case "this": case "let": 
            case "do": case "if": case "else": 
            case "while": case "return":
                                    out = "<keyword> " + curToke + " </keyword>\n"; break;
            case "{": case "}": case "(": case ")": 
            case "[": case "]": case ".": case ",": 
            case ";": case "+": case "-": case "*": 
            case "/": case "&": case "|": 
            case "=": case "~":
                                    out = "<symbol> " + curToke + " </symbol>\n"; break;
            case ">":   out = "<symbol> &gt </symbol>\n"; break;
            case "<":   out = "<symbol> &lt </symbol>\n"; break;
        }
        if(out.equals("") && curToke.length() > 0){
            out = "<identifier> " + curToke + " </identifier>\n";
        }
        return out;
    }
    
    public void strContainsSmth(String token){
        if(token.contains(";")){
            token = token.split(";")[0];
            tokenType(token);
            tokenType(";");
        }
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

    public String getOut(){
        return out;
    }
}
