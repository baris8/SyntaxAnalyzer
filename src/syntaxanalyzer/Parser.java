package syntaxanalyzer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {
    
    JackTokenizer jt;
    int pointer;
    String[] input;
    String out;
    
    public Parser(String inp, JackTokenizer j){
        input = inp.split("\n");
        pointer = 0;
        jt = j;
        out = "";
        System.out.println("File "+ jt.getFile().getName());
        
        for(int i = 0; i < input.length; i++){
            System.out.println("Zeile "+i+": "+ input[i]);
        }
    }
    
    //class --> 'class' className '{' classVarDec* subroutineDec* '}'
    public void compileClass(){
        out += "<class>\n";
        needTerminal("class");     
        compileVarName();
        needTerminal("{"); 
        while(input[pointer].equals("static") || input[pointer].equals("field")){
            compileClassVarDec();
        }
        while(input[pointer].equals("constructor") || input[pointer].equals("function") || input[pointer].equals("method")){
            compileSubroutineDec();
        }
        needTerminal("}"); 
        out += "</class>";
    }
    //classVarDec --> ('static' | 'field') type varName (',' varName)* ';' 
    public void compileClassVarDec(){
        out += "<classVarDec>\n";
        if(input[pointer].equals("static") || input[pointer].equals("field")){
            out += "<keyword> " + input[pointer] + " </keyword>\n";
            pointer++;
            compileType();
            compileVarName();
            while(input[pointer].equals(",")){
                needTerminal(",");
                compileVarName();
            }
            needTerminal(";"); 
        }
        out += "</classVarDec>\n";
    }
    //type --> 'int' | 'boolean' | 'char' | className
    public void compileType(){
        if(input[pointer].equals("int") || input[pointer].equals("char") || input[pointer].equals("boolean")){
            out += "<keyword> " + input[pointer] + " </keyword>\n";
            pointer++;
        }else if(jt.tokenType(input[pointer]).equals("IDENTIFIER")){
            out += "<identifier> " + input[pointer] + " </identifier>\n";
            pointer++;
        }
    }
    //subroutineDec --> ('constructor'|'function'|'method') ('void'|type) subroutineName '(' parameterList ')' subroutineBody
    public void compileSubroutineDec(){
        out += "<subroutineDec>\n";
        if(input[pointer].equals("constructor") || input[pointer].equals("function") || input[pointer].equals("method")){
            out += "<keyword> " + input[pointer] + "</keyword>\n";
            pointer++;
        }
        if(input[pointer].equals("void")){
            needTerminal("void");
        }else{
            compileType();
        }
        
        compileVarName();
        needTerminal("("); 
        compileParameterList();
        needTerminal(")"); 
        compileSubroutineBody();
        out += "</subroutineDec>\n";
    }
    //parameterList --> ((type varName)(',' type VarName)*)?
    public void compileParameterList(){
        out += "<parameterList>\n";
        compileType();
        compileVarName();
        while(input[pointer].equals(",")){
            needTerminal(",");
            compileType();
            compileVarName();
        }
        out += "</parameterList>\n";
    }
    //subroutineBody --> '{' varDec* statements '}'
    public void compileSubroutineBody(){
        out += "<subroutineBody>\n";
        needTerminal("{"); 
        while(jt.tokenType(input[pointer]).equals("KEYWORD") && input[pointer].equals("var")){
            compileVarDec();
        }
        compileStatements();
        needTerminal("}"); 
        out += "</subroutineBody>\n";
    }
    //varDec --> 'var' type varName (',' varName)* ';'
    public void compileVarDec(){
        out += "<varDec>\n";
        needTerminal("var"); 
        compileType();
        compileVarName();
        while(input[pointer].equals(",")){
            out += "<symbol> , </symbol>\n";
            pointer++;
            compileVarName();
        }
        needTerminal(";"); 
        out += "</varDec>\n";
    }
    //varName --> identifier
    public void compileVarName(){
        if(jt.tokenType(input[pointer]).equals("IDENTIFIER")){
            out += "<identifier> " + input[pointer] + " </identifier>\n";
            pointer++;
        }
    }
    //statement --> (letStatement | ifStatement | whileStatement | doStatement | returnStatement)*
    public void compileStatements(){
        out += "<statements>\n";
        while(jt.tokenType(input[pointer]).equals("KEYWORD")){
            switch(input[pointer]){
                case "let": compileLetStatement(); break;
                case "if": compileIfStatement(); break;
                case "while": compileWhileStatement(); break;
                case "do": compileDoStatement(); break;
                case "return": compileReturnStatement(); break;
            }
        }
        out += "</statements>\n";
    }
    //letStatement --> 'let' varName ('[' expression ']')? '=' expression ';'
    public void compileLetStatement(){
        out += "<letStatement>\n";
        needTerminal("let"); 
        compileVarName();
        if(input[pointer].equals("[")){
            needTerminal("[");
            compileExpression();
            needTerminal("]");
        }
        needTerminal("="); 
        compileExpression();
        needTerminal(";"); 
        out += "</letStatement>\n";
    }
    //ifStatement --> 'if' '(' expression ')' '{' statements '}' ('else' '{' statements '}')?
    public void compileIfStatement(){
        out += "<ifStatement>\n";
        needTerminal("if"); 
        needTerminal("("); 
        compileExpression();
        needTerminal(")"); 
        needTerminal("{"); 
        compileStatements();
        needTerminal("}");
        if(input[pointer].equals("else")){
            needTerminal("else"); 
            needTerminal("{"); 
            compileStatements();
            needTerminal("}"); 
        }
        out += "</ifStatement>\n";
    }
    //whileStatement --> 'while' '(' expression ')' '{' statements '}'
    public void compileWhileStatement(){
        out += "<whileStatement>\n";
        needTerminal("while"); 
        needTerminal("("); 
        compileExpression();
        needTerminal(")"); 
        needTerminal("{"); 
        compileStatements();
        needTerminal("}"); 
        out += "</whileStatement>\n";
    }
    //doStatement --> 'do' subroutineCall ';'
    public void compileDoStatement(){
        out += "<doStatement>\n";
        needTerminal("do"); 
        compileSubroutineCall();
        needTerminal(";"); 
        out += "</doStatement>\n";
    }
    //returnStatement --> 'return' expression? ';'
    public void compileReturnStatement(){
        out += "<returnStatement>\n";
        needTerminal("return"); 
        if(input[pointer].equals(";")){
            needTerminal(";"); 
        }else{
            compileExpression();
            needTerminal(";"); 
        }
        out += "</returnStatement>\n";
    }
    //expression --> term (op term)*
    public void compileExpression(){
        out += "<expression>\n";
        compileTerm();
        boolean tmp = true;
        while(tmp){
            switch(input[pointer]){
                case "+": case "-": case "=":
                case "*": case "/": case "|":
                    out += "<symbol> " + input[pointer] + " </symbol>\n";
                    pointer++;
                    compileTerm();
                    break;
                case ">":
                    out += "<symbol> &gt; </symbol>\n";
                    pointer++;
                    compileTerm();
                    break;
                case "<":
                    out += "<symbol> &lt; </symbol>\n";
                    pointer++;
                    compileTerm();
                    break;
                case "&":
                    out += "<symbol> &amp; </symbol>\n";
                    pointer++;
                    compileTerm();
                    break;
                default: tmp = false;
            }
        }
        out += "</expression>\n";
    }
    //term --> intConst | stringConst | keywordConst | varName | varName '[' expression ']' |
    //subroutineCall | '(' expression ')' | unaryOp term
    public void compileTerm(){
        out += "<term>\n";
        if(jt.tokenType(input[pointer]).equals("IDENTIFIER") && !input[pointer].equals("-")){
            String tmp = input[pointer];
            pointer++;
            if(input[pointer].equals("[")){
                out += "<identifier> " + tmp + " </identifier>\n";
                out += "<symbol> " + input[pointer]+ " </symbol>\n";
                pointer++;
                compileExpression();
                needTerminal("]");
            }else if(input[pointer].equals("(") || input[pointer].equals(".")){
                pointer--;
                compileSubroutineCall();
            }else{
                out += "<identifier> " + tmp + " </identifier>\n";
            }
        }else{
            if(jt.tokenType(input[pointer]).equals("INT_CONST")){
                out += "<integerConstant> " + input[pointer] + " </integerConstant>\n";
                pointer++;
            }else if(jt.tokenType(input[pointer]).equals("STRING_CONST")){
                out += "<stringConstant> " + input[pointer].substring(1) + " </stringConstant>\n";
                pointer++;
            }else if(input[pointer].equals("true") || input[pointer].equals("false") ||
                    input[pointer].equals("null") || input[pointer].equals("this")){
                out += "<keyword> " + input[pointer] + " </keyword>\n";
                pointer++;
            }else if(input[pointer].equals("(")){
                out += "<symbol> " + input[pointer]+ " </symbol>\n";
                pointer++;
                compileExpression();
                if(input[pointer].equals(")")){
                    out += "<symbol> " + input[pointer]+ " </symbol>\n";
                    pointer++;
                }
            }else if(input[pointer].equals("-") || input[pointer].equals("~")){
                out += "<symbol> " + input[pointer]+ " </symbol>\n";
                pointer++;
                compileTerm();
            }
        }
        out += "</term>\n";
    }
    //subroutineCall --> subroutineName '(' expressionList ')' | (className | varName) '.' subroutineName '(' expressionList ')' 
    public void compileSubroutineCall(){
        compileVarName();
        if(input[pointer].equals("(")){
            needTerminal("("); 
            compileExpressionList();
            needTerminal(")"); 
        }else if(input[pointer].equals(".")){
            needTerminal("."); 
            compileVarName();
            needTerminal("("); 
            compileExpressionList();
            needTerminal(")"); 
        }
    }
    //expressionList --> (expression(',' expression)*)?
    public void compileExpressionList(){
        out += "<expressionList>\n";
        if(!input[pointer].equals(")")){
            compileExpression();
            while(input[pointer].equals(",")){
                needTerminal(","); 
                compileExpression();
            }
        }
        out += "</expressionList>\n";
    }
    
    public void needTerminal(String ts){
        if(input[pointer].equals(ts)){
            switch(jt.tokenType(input[pointer])){
                case "KEYWORD": out += "<keyword> " + ts + " </keyword>\n"; break;
                case "SYMBOL": out += "<symbol> " + ts + " </symbol>\n"; break;
                case "STRING_CONST": out += "<stringConstant> " + ts + " </stringConstant>\n"; break;
                case "INT_CONST": out += "<integerConstant> " + ts + " </integerConstant>\n"; break;
            }
            pointer++;
        }else{
            try {
                System.out.println("Es fehlt ein: " + ts);
                
                throw new Exception();
            } catch (Exception ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveParsedXML(){
        PrintWriter writer;
        try {
            String name = jt.getFile().getName().replace(".jack", "");
            writer = new PrintWriter("my"+ name + ".xml");
            writer.println(out);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SyntaxAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getOut(){
        return out;
    }
    
}
