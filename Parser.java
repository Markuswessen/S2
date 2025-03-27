public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
    
    public ParseTree parse() throws SyntaxError {
        // Startsymbol är BinTree
        ParseTree result = program();
        // Borde inte finnas något kvar av indata när vi parsat ett bintree
        if (lexer.hasMoreTokens())
            throw new SyntaxError();
        return result;
    }

    private ParseTree program() throws SyntaxError {
        Token t = lexer.peekToken();
        
        if(t.getType() == TokenType.Forw  || t.getType() == TokenType.Back || t.getType() == TokenType.Left
        || t.getType() == TokenType.Right || t.getType() == TokenType.Down || t.getType() == TokenType.Up
        || t.getType() == TokenType.Color){
           // return parsetree.addbranch(instruction());
           return instruction();
        }
        else if(t.getType() == TokenType.Rep){
            //return parsetree.addbranch(repeat());
            return repeat();
        }
        else{
            throw new SyntaxError();
        }
    }

    // Implementera metoder för att bearbeta och parsa olika instruktioner, repetitioner etc.
    // Använd currentPosition och tokens för att navigera genom token-sekvensen och producera syntaxträdet.
    // Varje hjälpmetod bör returnera det motsvarande syntaxträdet för den specifika instruktionen.
    private ParseTree instruction() throws SyntaxError {
        Token t = lexer.peekToken();
        ParseTree parseTree = new ParseTree(t);
        if (t.getType() == TokenType.Forw || t.getType() == TokenType.Back){
            parseTree.addChild(movement());
        } 
        else if(t.getType() == TokenType.Left || t.getType() == TokenType.Right){
            parseTree.addChild(turn());
        }
        else if(t.getType() == TokenType.Down || t.getType() == TokenType.Up){
            parseTree.addChild(pen());
        }
        else if(t.getType() == TokenType.Color){
            parseTree.addChild(color());
        }
        else if(t.getType() == TokenType.Rep){
            parseTree.addChild(repeat());
        }
        else{
            throw new SyntaxError();
        }
        return parseTree;
    }

    private ParseTree movement() throws SyntaxError {
        
        Token param = lexer.nextToken(); // ät upp t

        Token param2 = lexer.nextToken();
        if (param2.getType() != TokenType.Decimal) throw new SyntaxError();

        Token param3 = lexer.nextToken();
        if (param3.getType() != TokenType.Period) throw new SyntaxError();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(param2));
        parseTree.addChild(new ParseTree(param3));
        
        if (lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.Quote) {
            parseTree.addChild(program());
        }
        
        return parseTree;
        
    }

    private ParseTree turn() throws SyntaxError {
        Token param = lexer.nextToken(); // ät upp t

        Token param2 = lexer.nextToken();
        if (param2.getType() != TokenType.Decimal) throw new SyntaxError();

        Token param3 = lexer.nextToken();
        if (param3.getType() != TokenType.Period) throw new SyntaxError();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(param2));
        parseTree.addChild(new ParseTree(param3));
        
        if (lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.Quote) {
            parseTree.addChild(program());
        }
        
        return parseTree;
    }

    private ParseTree pen() throws SyntaxError {
        Token param = lexer.nextToken();

        Token param2 = lexer.nextToken();
        if (param2.getType() != TokenType.Period) throw new SyntaxError();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(param2));

        if (lexer.hasMoreTokens() && lexer.peekToken().getType() != TokenType.Quote) {
            parseTree.addChild(program());
        }

        return parseTree;
    }

    private ParseTree color() throws SyntaxError {
        Token param = lexer.nextToken();
        if (param.getType() != TokenType.Hex) throw new SyntaxError();
        Token param2 = lexer.nextToken();
        if (param.getType() != TokenType.Period) throw new SyntaxError();
        Token param3 = lexer.nextToken();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(param2));
        parseTree.addChild(new ParseTree(param3));

        if (lexer.hasMoreTokens()) {
            parseTree.addChild(program());
        }

        return parseTree;
    }

    private ParseTree repeat() throws SyntaxError {
        Token param = lexer.nextToken();
        Token param2 = lexer.nextToken();
        System.out.println(param2.getType() + "number2");
        if (param2.getType() != TokenType.Decimal) throw new SyntaxError();

        Token param3 = lexer.nextToken();
        if (param3.getType() != TokenType.Quote) throw new SyntaxError();
        
        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(param2));
        parseTree.addChild(new ParseTree(param3));
    
        int currentPosition = lexer.currentToken;
        System.out.println(param3.getType() + "  quote?");
        for (int i = 0; i < (Integer) param2.getData(); i++) {
            parseTree.addChild(program());
            if(i != (Integer)param2.getData()-1)
                lexer.currentToken = currentPosition;
        }
    
        Token param4 = lexer.nextToken();
        System.out.println(param4.getType() + "?");
        if (param4.getType() != TokenType.Quote) {
            throw new SyntaxError();
        }
    
        parseTree.addChild(new ParseTree(param3));
    
        return parseTree;
    }


}