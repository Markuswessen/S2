public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseTree parse() throws SyntaxError {
        // Startsymbol är BinTree
        ParseTree result = program();
        // Borde inte finnas något kvar av indata när vi parsat ett bintree
        if (lexer.hasMoreTokens()) {
            System.out.println("empty");
            throw new SyntaxError();
        }
        return result;
    }

    private ParseTree program() throws SyntaxError {
        Token t = lexer.peekToken();
        
        ParseTree parseTree = new ParseTree(new Token(TokenType.Root, "Program"));

        while (lexer.hasMoreTokens()) {
            if (t.getType() == TokenType.Forw || t.getType() == TokenType.Back || t.getType() == TokenType.Left
                    || t.getType() == TokenType.Right || t.getType() == TokenType.Down || t.getType() == TokenType.Up
                    || t.getType() == TokenType.Color) {
                // return parsetree.addbranch(instruction());
                parseTree.addChild(instruction());
            } else if (t.getType() == TokenType.Rep) {
                // return parsetree.addbranch(repeat());
                parseTree.addChild(repeat());
            } else {
                System.out.println(t.getType());
                System.out.println("what");
                throw new SyntaxError();
            }
        }
        return parseTree;
    }

    // Implementera metoder för att bearbeta och parsa olika instruktioner,
    // repetitioner etc.
    // Använd currentPosition och tokens för att navigera genom token-sekvensen och
    // producera syntaxträdet.
    // Varje hjälpmetod bör returnera det motsvarande syntaxträdet för den specifika
    // instruktionen.
    private ParseTree instruction() throws SyntaxError {
        Token t = lexer.peekToken();
        System.out.println(t.getType() + " + " + t.getData());
        if (t.getType() == TokenType.Forw || t.getType() == TokenType.Back) {
            return movement();
        } else if (t.getType() == TokenType.Left || t.getType() == TokenType.Right) {
            return turn();
        } else if (t.getType() == TokenType.Down || t.getType() == TokenType.Up) {
            return pen();
        } else if (t.getType() == TokenType.Color) {
            return color();
        } else if (t.getType() == TokenType.Rep) {
            return repeat();
        } else {
            throw new SyntaxError();
        }
    }

    private ParseTree movement() throws SyntaxError {

        Token param = lexer.nextToken(); // ät upp t

        Token decimal = lexer.nextToken();
        if (decimal.getType() != TokenType.Decimal)
            throw new SyntaxError();

        Token period = lexer.nextToken();
        if (period.getType() != TokenType.Period)
            throw new SyntaxError();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(decimal));
        parseTree.addChild(new ParseTree(period));

        return parseTree;

    }

    private ParseTree turn() throws SyntaxError {
        Token param = lexer.nextToken(); // ät upp t

        Token decimal = lexer.nextToken();
        if (decimal.getType() != TokenType.Decimal)
            throw new SyntaxError();

        Token period = lexer.nextToken();
        if (period.getType() != TokenType.Period)
            throw new SyntaxError();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(decimal));
        parseTree.addChild(new ParseTree(period));

        return parseTree;
    }

    private ParseTree pen() throws SyntaxError {
        Token param = lexer.nextToken();

        Token period = lexer.nextToken();
        if (period.getType() != TokenType.Period)
            throw new SyntaxError();

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(period));

        return parseTree;
    }

    private ParseTree color() throws SyntaxError {
        Token color = lexer.nextToken(); // ät upp t
        System.out.println(color.getType() + " color?");

        Token hex = lexer.nextToken();
        System.out.println(hex.getType() + " + " + hex.getData() + " hex?");
        if (hex.getType() != TokenType.Hex)
            throw new SyntaxError();
        Token period = lexer.nextToken();
        System.out.println(period.getType() + " param2?");
        if (period.getType() != TokenType.Period)
            throw new SyntaxError();

        ParseTree parseTree = new ParseTree(color);
        parseTree.addChild(new ParseTree(hex));
        parseTree.addChild(new ParseTree(period));

        return parseTree;
    }

    private ParseTree repeat() throws SyntaxError {
        Token rep = lexer.nextToken();
        Token decimal = lexer.nextToken();
        System.out.println(decimal.getType() + " number2");
        if (decimal.getType() != TokenType.Decimal)
            throw new SyntaxError();

        Token quote = lexer.nextToken();
        if (quote.getType() != TokenType.Quote)
            throw new SyntaxError();

        ParseTree parseTree = new ParseTree(rep);
        parseTree.addChild(new ParseTree(decimal));
        parseTree.addChild(new ParseTree(quote));

        int currentPosition = lexer.currentToken;
        System.out.println(quote.getType() + "  quote?!!");
        for (int i = 0; i < (Integer) decimal.getData(); i++) {
            parseTree.addChild(program());
            if (i != (Integer) decimal.getData() - 1)
                lexer.currentToken = currentPosition;
        }

        Token param4 = lexer.nextToken();
        System.out.println(param4.getType() + "?");
        if (param4.getType() != TokenType.Quote) {
            throw new SyntaxError();
        }

        parseTree.addChild(new ParseTree(quote));

        return parseTree;
    }

}