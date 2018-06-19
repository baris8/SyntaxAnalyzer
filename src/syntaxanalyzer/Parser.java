package syntaxanalyzer;

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
    }
    
    public void compileClass(){
        if(jt.tokenType(input[pointer]).equals("KEYWORD") && jt.keyWord(input[pointer]).equals("CLASS")){
            //'class'
            out += "<class>\n";
            out += "<keyword> class </keyword>\n";
            pointer++;
        }        
        //className
        compileVarName();
        //{
        if(jt.tokenType(input[pointer]).equals("SYMBOL") && input[pointer].equals("{")){
                out += "<symbol> { </symbol>\n";
                pointer++;
        }
        while(input[pointer].equals("static") || input[pointer].equals("field")){
            compileVarDec();
        }
        while(input[pointer].equals("constructor") || input[pointer].equals("function") || input[pointer].equals("method")){
            compileSubroutineDec();
        }
        if(jt.tokenType(input[pointer]).equals("SYMBOL") && input[pointer].equals("}")){
                out += "<symbol> } </symbol>\n";
                pointer++;
        }
        out += "</class>\n";
    }
    public void compileClassVarDec(){
        //('static'|'field')
        if(jt.tokenType(input[pointer]).equals("KEYWORD") && (jt.keyWord(input[pointer]).equals("static") || jt.keyWord(input[pointer]).equals("field"))){
            out += "<keyword> " + input[pointer] + " </keyword>\n";
            pointer++;
            
            //type
            compileType();
            
            //(',' varName)*
            while(jt.tokenType(input[pointer]).equals("SYMBOL") && jt.keyWord(input[pointer]).equals(",")){
                out += "<keyword> , </keyword>\n";
                pointer++;
                compileVarName();
            }
            
            //';'
            if(jt.tokenType(input[pointer]).equals("SYMBOL") && jt.keyWord(input[pointer]).equals(";")){
                out += "<keyword> ; </keyword>\n";
            }
        }
    }
    public void compileType(){
        //type --> int char boolean
        //type --> className
        if(jt.tokenType(input[pointer]).equals("KEYWORD") && (input[pointer].equals("int") || input[pointer].equals("char") || input[pointer].equals("boolean"))){
            out += "<keyword> " + input[pointer] + " </keyword>\n";
            pointer++;
        }else if(jt.tokenType(input[pointer]).equals("IDENTIFIER")){
            out += "<identifier> " + input[pointer] + " </identifier>\n";
            pointer++;
        }
    }
    public void compileSubroutineDec(){
        out += "<subroutineDec>\n";
        if(input[pointer].equals("constructor") || input[pointer].equals("function") || input[pointer].equals("method")){
            out += "<keyword> " + input[pointer] + "</keyword>\n";
            pointer++;
        }
        
        if(input[pointer].equals("void")){
            out += "<keyword> " + input[pointer] + " </keyword>\n";
            pointer++;
        }else{
            compileType();
        }
        
        compileVarName();
        if(input[pointer].equals("(")){
            out += "<symbol> " + input[pointer] + " </symbol>\n";
            pointer++;
        }
        
        compileParameterList();
        if(input[pointer].equals(")")){
            out += "<symbol> " + input[pointer] + " </symbol>\n";
            pointer++;
        }
        compileSubroutineBody();
        out += "</subroutineDec>\n";
    }
    public void compileParameterList(){
        out += "<parameterList>\n";
        compileType();
        compileVarName();
        while(jt.symbol(input[pointer]).equals(",")){
            System.out.println("Hier");
            out += "<symbol> , </symbol>";
            pointer++;
            compileVarName();
        }
        out += "</parameterList>\n";
    }
    public void compileSubroutineBody(){
        out += "<subroutineBody>\n";
        if(jt.tokenType(input[pointer]).equals("SYMBOL") && input[pointer].equals("{")){
            out += "<symbol> { </symbol>\n";
            pointer++;
        }
        while(jt.tokenType(input[pointer]).equals("KEYWORD") && input[pointer].equals("var")){
            compileVarDec();
        }
        compileStatements();
        if(jt.tokenType(input[pointer]).equals("SYMBOL") && input[pointer].equals("}")){
            out += "<symbol> } </symbol>\n";
            pointer++;
        }
        out += "<subroutineBody>\n";
    }
    public void compileVarDec(){
        out += "<varDec>\n";
        //var
        if(input[pointer].equals("var")){
            out += "<keyword> var </keyword>\n";
            pointer++;
        }
        //type
        //varName
        compileType();
        compileVarName();
            
        //(',' varName)*
        while(input[pointer].equals(",")){
            out += "<symbol> , </symbol>\n";
            pointer++;
            compileVarName();
        }
        //';'
        if(input[pointer].equals(";")){
            out += "<symbol> ; </symbol>\n";
            pointer++;
        }
        out += "</varDec>\n";
    }
    public void compileVarName(){
        if(jt.tokenType(input[pointer]).equals("IDENTIFIER")){
            out += "<identifier> " + input[pointer] + " </identifier>\n";
            pointer++;
        }
    }
    

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
            System.out.println(input[pointer]);
        }
        out += "</statements>\n";
    }
    public void compileLetStatement(){
        out += "<letStatement>\n";
        if(input[pointer].equals("let")){
            out += "<keyword> let </keyword>\n";
            pointer++;
        }
        compileVarName();
        if(input[pointer].equals("[")){
            out += "<symbol> [ </symbol>\n";
            pointer++;
            compileExpression();
            if(input[pointer].equals("]")){
                out += "<symbol> ] </symbol>\n";
                pointer++;
            }
        }
        if(input[pointer].equals("=")){
            out += "<symbol> = </symbol>\n";
            pointer++;
        }
        compileExpression();
        if(input[pointer].equals(";")){
            out += "<symbol> ; </symbol>\n";
            pointer++;
        }
        out += "</letStatement>\n";
    }
    public void compileIfStatement(){
        out += "<ifStatement>\n";
        if(input[pointer].equals("if")){
            out += "<keyword> if </keyword>\n";
            pointer++;
        }
        if(input[pointer].equals("(")){
            out += "<symbol> ( </symbol>\n";
            pointer++;
        }
        compileExpression();
        if(input[pointer].equals(")")){
            out += "<symbol> ) </symbol>\n";
            pointer++;
        }
        if(input[pointer].equals("{")){
            out += "<symbol> { </symbol>\n";
            pointer++;
        }
        compileStatements();
        if(input[pointer].equals("}")){
            out += "<symbol> } </symbol>\n";
            pointer++;
        }
        
        if(input[pointer].equals("else")){
            out += "<keyword> else </keyword>\n";
            pointer++;
            if(input[pointer].equals("{")){
                out += "<symbol> { </symbol>\n";
                pointer++;
            }
            compileStatements();
            if(input[pointer].equals("}")){
                out += "<symbol> } </symbol>\n";
                pointer++;
            }
        }
        out += "</ifStatement>\n";
    }
    public void compileWhileStatement(){
        out += "<whileStatement>\n";
        if(input[pointer].equals("while")){
            out += "<keyword> while </keyword>\n";
            pointer++;
        }
        if(input[pointer].equals("(")){
            out += "<symbol> ( </symbol>\n";
            pointer++;
        }
        compileExpression();
        if(input[pointer].equals(")")){
            out += "<symbol> ) </symbol>\n";
            pointer++;
        }
        
        if(input[pointer].equals("{")){
            out += "<symbol> { </symbol>\n";
            pointer++;
        }
        compileStatements();
        if(input[pointer].equals("}")){
            out += "<symbol> } </symbol>\n";
            pointer++;
        }
        out += "</whileStatement>\n";
    }
    public void compileDoStatement(){
        out += "<doStatement>\n";
        if(input[pointer].equals("do")){
            out += "<keyword> do </keyword>\n";
            pointer++;
        }
        compileSubroutineCall();
        if(input[pointer].equals(";")){
            out += "<symbol> ; </symbol>\n";
            pointer++;
        }
        out += "</doStatement>\n";
    }
    public void compileReturnStatement(){
        out += "<returnStatement>\n";
        if(input[pointer].equals("return")){
            out += "<keyword> return </keyword>\n";
            pointer++;
        }
        if(input[pointer].equals(";")){
            out += "<symbol> ; </symbol>\n";
            pointer++;
        }else{
            compileExpression();
            if(input[pointer].equals(";")){
                out += "<symbol> ; </symbol>\n";
                pointer++;
            }
        }
        out += "</returnStatement>\n";
    }
    
    public void compileExpression(){
        out += "<expression>\n";
        compileTerm();
        
        boolean tmp = true;
        while(tmp){
            switch(input[pointer]){
                case "+": case "-": case "=":
                case "*": case "/": case "&": case "|":
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
                default: tmp = false;
            }
        }
        out += "</expression>\n";
    }
    public void compileTerm(){
        out += "<term>\n";
        if(jt.tokenType(input[pointer]).equals("IDENTIFIER")){
            String tmp = input[pointer];
            pointer++;
            //varName[expression]
            if(input[pointer].equals("[")){
                out += "<identifier> " + tmp + " </identifier>\n";
                out += "<symbol> " + input[pointer]+ " </symbol>\n";
                pointer++;
                compileExpression();
                if(input[pointer].equals("]")){
                    out += "<symbol> " + input[pointer]+ " </symbol>\n";
                    pointer++;
                }
            //subroutineCall
            }else if(input[pointer].equals("(") || input[pointer].equals(".")){
                pointer--;
                compileSubroutineCall();
            }else{
                //varName
                out += "<identifier> " + tmp + " </identifier>\n";
            }
        //
        }else{
            if(jt.tokenType(input[pointer]).equals("INT_CONST")){
                out += "<integerConstant> " + input[pointer] + " </integerConstant>\n";
                pointer++;
            }else if(jt.tokenType(input[pointer]).equals("STRING_CONST")){
                out += "<stringConstant> " + input[pointer] + " </stringConstant>\n";
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
    
    public void compileSubroutineCall(){
        compileVarName();
        if(input[pointer].equals("(")){
            out += "<symbol> ( </symbol>\n";
            pointer++;
            compileExpressionList();
            if(input[pointer].equals(")")){
                out += "<symbol> ) </symbol>\n";
                pointer++;
            }
        }else if(input[pointer].equals(".")){
            out += "<symbol> . </symbol>\n";
            pointer++;
            compileVarName();
            if(input[pointer].equals("(")){
                out += "<symbol> ( </symbol>\n";
                pointer++;
                compileExpressionList();
                if(input[pointer].equals(")")){
                    out += "<symbol> ) </symbol>\n";
                    pointer++;
                }
            }
        }
    }
    
    public void compileExpressionList(){
        out += "<expressionList>\n";
        if(!input[pointer].equals(")")){
            compileExpression();
            while(input[pointer].equals(",")){
                out += "<symbol> , </symbol>\n";
                pointer++;
                compileExpression();
            }
        }
        out += "</expressionList>\n";
    }
    
    public void error(){
        out = "ERROR";
    }
    
    public String getOut(){
        return out;
    }
    
}
