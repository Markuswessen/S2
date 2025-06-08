public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseTree parse() throws SyntaxError {
        ParseTree result = program();
        if (lexer.hasMoreTokens()) {
            Token unexpected = lexer.peekToken();
            throw new SyntaxError(unexpected.getRow());
        }
        return result;
    }

    private ParseTree program() throws SyntaxError {
        if (!lexer.hasMoreTokens())
            return null;

        Token next = lexer.peekToken();
        if (next.getType() == TokenType.Quote) {
            return null;
        }

        ParseTree left = instruction();
        ParseTree right = program();

        ParseTree root = new ParseTree(new Token(TokenType.Root, "Program", left.getToken().getRow()));
        root.setLeft(left);
        root.setRight(right);
        return root;
    }

    private ParseTree instruction() throws SyntaxError {
        Token token = lexer.peekToken();
        if (token.getType() == TokenType.Rep) {
            return repeat();
        } else {
            return expression();
        }
    }

    private ParseTree expression() throws SyntaxError {
        Token token = lexer.nextToken();
        ParseTree expr = new ParseTree(token);

        if (token.getType() == TokenType.Color) {
            Token hex = lexer.nextToken();
            if (hex.getType() != TokenType.Hex) {
                // System.out.println("hex error?");
                throw new SyntaxError(hex.getRow());
            }
            ParseTree hexNode = new ParseTree(hex);
            expr.setLeft(hexNode);
        } else if (token.getType() == TokenType.Forw || token.getType() == TokenType.Back
                || token.getType() == TokenType.Left || token.getType() == TokenType.Right) {
            Token decimal = lexer.nextToken();
            if (decimal.getType() != TokenType.Decimal || (int) decimal.getData() < 1) {
                // System.out.println("decimal error?");
                throw new SyntaxError(decimal.getRow());
            }
            ParseTree decimalNode = new ParseTree(decimal);
            expr.setLeft(decimalNode);
        } else if (token.getType() == TokenType.Down || token.getType() == TokenType.Up) {
            expr.setLeft(null);
        } else {
            // System.out.println("Unexpected token type: " + token.getType());
            throw new SyntaxError(token.getRow());
        }

        Token period = lexer.nextToken();
        if (period.getType() != TokenType.Period) {
            // System.out.println("Expected Period token after instruction, got " +
            // period.getType());
            throw new SyntaxError(period.getRow());
        }
        ParseTree periodNode = new ParseTree(period);
        expr.setRight(periodNode);

        return expr;
    }

    private ParseTree repeat() throws SyntaxError {
        Token rep = lexer.nextToken();
        Token repeats = lexer.nextToken();

        if (repeats.getType() != TokenType.Decimal || (int) repeats.getData() < 1) {
            throw new SyntaxError(repeats.getRow());
        }

        ParseTree root = new ParseTree(rep);
        ParseTree countNode = new ParseTree(repeats);
        root.setLeft(countNode);

        Token next = lexer.peekToken();

        if (next.getType() == TokenType.Quote) {
            lexer.nextToken(); // consume opening quote
            Token check = lexer.peekToken();
            if (check.getType() == TokenType.Quote) {
                throw new SyntaxError(check.getRow());
            }

            ParseTree quotedProgram = program();
            Token closing = lexer.nextToken();
            if (closing.getType() != TokenType.Quote) {
                throw new SyntaxError(closing.getRow());
            }

            root.setRight(quotedProgram);
        } else

        {
            ParseTree single = instruction();

            root.setRight(single);
        }

        return root;
    }

}