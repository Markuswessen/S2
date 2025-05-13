import java.util.ArrayList;
import java.util.List;

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
            // System.out.println("empty");
            throw new SyntaxError(1);
        }
        return result;
    }

    private ParseTree program() throws SyntaxError {

        ParseTree parseTree = new ParseTree(new Token(TokenType.Root, "Program", 1));
        // System.out.println(t.getType() + " " + t.getData());
        while (lexer.hasMoreTokens()) {
            Token t = lexer.peekToken();
            // System.out.println("while? " + t.getType() + " " + t.getData());
            if (t.getType() == TokenType.Forw || t.getType() == TokenType.Back || t.getType() == TokenType.Left
                    || t.getType() == TokenType.Right || t.getType() == TokenType.Down || t.getType() == TokenType.Up
                    || t.getType() == TokenType.Color) {
                // System.out.println("instruction? " + t.getType() + " " + t.getData());
                parseTree.addChild(instruction());
            } else if (t.getType() == TokenType.Rep) {
                // System.out.println("repeat?");
                parseTree.addChild(repeat());
            } else {
                // System.out.println(t.getType());
                // System.out.println("what");
                throw new SyntaxError(t.getRow());
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
        // System.out.println(t.getType() + " + " + t.getData() + " im up here");
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
            // System.out.println(t.getType() + " im down here");
            throw new SyntaxError(t.getRow());
        }
    }

    private ParseTree movement() throws SyntaxError {

        Token param = lexer.nextToken(); // ät upp t
        // System.out.println(param.getType() + " " + param.getData() + " movement?");

        Token decimal = lexer.nextToken();
        // System.out.println(decimal.getType() + " " + decimal.getData() + "
        // decimal?");
        if (decimal.getType() != TokenType.Decimal) {
            // System.out.println("decimal error?");
            throw new SyntaxError(decimal.getRow());
        }
        Token period = lexer.nextToken();
        // System.out.println(period.getType() + " period?");
        if (period.getType() != TokenType.Period) {
            // System.out.println(0 + " period error?");
            throw new SyntaxError(period.getRow());

        }
        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(decimal));
        parseTree.addChild(new ParseTree(period));

        return parseTree;

    }

    private ParseTree turn() throws SyntaxError {
        Token param = lexer.nextToken(); // ät upp t

        Token decimal = lexer.nextToken();
        // System.out.println(decimal.getType() + " " + decimal.getData() + "
        // decimal?");

        if (decimal.getType() != TokenType.Decimal)
            throw new SyntaxError(decimal.getRow());

        Token period = lexer.nextToken();
        if (period.getType() != TokenType.Period)
            throw new SyntaxError(period.getRow());

        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(decimal));
        parseTree.addChild(new ParseTree(period));

        return parseTree;
    }

    private ParseTree pen() throws SyntaxError {
        Token param = lexer.nextToken();

        Token period = lexer.nextToken();
        if (!lexer.hasMoreTokens())
            throw new SyntaxError(param.getRow());

        if (period.getType() != TokenType.Period) {
            throw new SyntaxError(period.getRow());
        }
        ParseTree parseTree = new ParseTree(param);
        parseTree.addChild(new ParseTree(period));

        return parseTree;
    }

    private ParseTree color() throws SyntaxError {
        Token color = lexer.nextToken(); // ät upp t
        // System.out.println(color.getType() + " color?");

        Token hex = lexer.nextToken();
        // System.out.println(hex.getType() + " + " + hex.getData() + " hex?");

        if (hex.getType() != TokenType.Hex) {
            // System.out.println("hex error?");
            throw new SyntaxError(hex.getRow());
        }
        Token period = lexer.nextToken();
        // System.out.println(period.getType() + " period?");
        if (period.getType() != TokenType.Period)
            throw new SyntaxError(period.getRow());
        // System.out.println("Im this now:" + color.getType());
        ParseTree parseTree = new ParseTree(color);
        parseTree.addChild(new ParseTree(hex));
        parseTree.addChild(new ParseTree(period));

        return parseTree;
    }

    private ParseTree repeat() throws SyntaxError {
        Token rep = lexer.nextToken();
        Token decimal = lexer.nextToken();

        if (decimal.getType() != TokenType.Decimal)
            throw new SyntaxError(decimal.getRow());

        ParseTree repNode = new ParseTree(rep);
        repNode.addChild(new ParseTree(decimal));

        Token next = lexer.peekToken();

        List<ParseTree> instructions = new ArrayList<>();

        if (next.getType() == TokenType.Quote) {
            lexer.nextToken(); // consume the starting "

            int quoteLevel = 1;

            while (quoteLevel > 0) {
                Token t = lexer.peekToken();

                if (t.getType() == TokenType.Quote) {
                    lexer.nextToken(); // consume "
                    quoteLevel--;
                    if (quoteLevel == 0) {
                        break;
                    }
                } else if (t.getType() == TokenType.Rep) {
                    // Kolla om rep är följt av en quote
                    Token ahead = lexer.aheadToken();
                    if (ahead.getType() == TokenType.Quote) {
                        quoteLevel++;
                    }
                    instructions.add(repeat());
                } else {
                    instructions.add(instruction());
                }
            }
        } else {
            instructions.add(instruction());
        }

        ParseTree result = new ParseTree(rep);
        result.addChild(new ParseTree(decimal));
        for (int i = 0; i < (Integer) decimal.getData(); i++) {
            for (ParseTree instr : instructions) {
                result.addChild(instr);
            }
        }

        return result;
    }

}
