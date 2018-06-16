package syntaxanalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JackTokenizer {
    private File file;
    private String curToke, xml, out;
    
    public JackTokenizer(File f){
        file = f;
        xml = "<tokens>\n";
        out = "";
    }
    
    public void tokenize() throws FileNotFoundException{
        String current;
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            current = scanner.nextLine();
            if(!current.equals("")){
                current = current.replaceAll("//.*", "").trim();
                current = current.replaceAll("//*/*.*/*/", "").trim();
                
                String s = "";
                for(int i = 0; i < current.length(); i++){
                    if(current.charAt(i) == ';' ||  current.charAt(i) == '.' || current.charAt(i) == '(' ||
                        current.charAt(i) == ')' || current.charAt(i) == ',' || current.charAt(i) == ']' || current.charAt(i) == '['){
                        xml += tokenTypeXML(s);
                        setOut(s);
                        s = "";
                        xml += tokenTypeXML(""+ current.charAt(i));
                        setOut(current.charAt(i)+"");
                    }else if(current.charAt(i) == ' '){
                        xml += tokenTypeXML(s);
                        setOut(s);
                        s = "";
                    }else if(current.charAt(i) == '\"'){
                        s += "$";
                        i++;
                        while(current.charAt(i) != '\"'){
                            s += current.charAt(i);
                            i++;
                        }
                        xml += tokenTypeXML(s);
                        setOut(s);
                        s = "";
                    }else{
                        s += current.charAt(i);
                    }
                }
                xml += tokenTypeXML(s);
                setOut(s);
            }
        }
        xml += "</tokens>\n";
    }
    
    public void setOut(String s){
        if(!s.equals("")){
            out += s + "\n";
        }
    }
    
    public String tokenTypeXML(String curToke){
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
            case ">":   out = "<symbol> &gt; </symbol>\n"; break;
            case "<":   out = "<symbol> &lt; </symbol>\n"; break;
        }
        if(out.equals("") && curToke.length() > 0 && curToke.charAt(0) == '$'){
            out = "<stringConstant> " + curToke.substring(1) + " </stringConstant>\n";
        }
        if(out.equals("") && curToke.length() > 0 && !Character.isDigit(curToke.charAt(0))){
            out = "<identifier> " + curToke + " </identifier>\n";
        }
        if(curToke.length() > 0 && Character.isDigit(curToke.charAt(0))){
            boolean integer = true;
            try {
                Integer num = Integer.parseInt(curToke);
            } catch (NumberFormatException e) {
                integer = false;
            }
            if(integer){
                out = "<integerConstant> " + curToke + " </integerConstant>\n";
            }
        }
        return out;
    }
    
    public String getXML(){
        return xml;
    }
    public String getOut(){
        return out;
    }
}
