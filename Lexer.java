import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    private List<Token> tokens;
    public int currentToken;
    private int lastrow = 1;
    private int row = 1;
    /*
     * private static String readInput(InputStream f) throws java.io.IOException {
     * // Reader stdin = new InputStreamReader(f);
     * // StringBuilder buf = new StringBuilder();
     * // char[] input = new char[1024];
     * // int read;
     * Scanner scanner = new Scanner(f);
     * StringBuilder inputBuilder = new StringBuilder();
     * while (scanner.hasNextLine()) {
     * inputBuilder.append(scanner.nextLine()).append("\n");
     * System.out.println("row");
     * }
     * String result = inputBuilder.toString().replaceAll("\r\n", "\n"); //
     * Standardisera radbrytningar;
     * scanner.close();
     * 
     * System.out.println("### Raw Input: ###\n" + result);
     * 
     * return result;
     * }
     */

    public Lexer(String input) throws java.io.IOException, SyntaxError {

        String regex = "%.*"
                + "|(?i)FORW(?=[\\s.%])"
                + "|(?i)BACK(?=[\\s.%])"
                + "|(?i)LEFT(?=[\\s.%])"
                + "|(?i)RIGHT(?=[\\s.%])"
                + "|(?i)DOWN(?=[\\s.%])"
                + "|(?i)UP(?=[\\s.%])"
                + "|(?i)COLOR(?=[\\s.%])"
                + "|(?i)REP(?=[\\s+.%])"
                + "|#[0-9A-Fa-f]{6}(?=[\\s.%])"
                + "|[1-9][0-9]*(?=[\\s.%]|$)"
                + "|\""
                + "|\\."
                + "|[^A-Za-z0-9#](?=[\\s.%])"
                + "|\\s+"
                + "|\\S+";

        // System.out.println("Hejsan");
        Pattern tokenPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        // System.out.println("Hejsan2");

        Matcher m = tokenPattern.matcher(input);
        // System.out.println(m);
        int inputPos = 0;
        tokens = new ArrayList<>();
        currentToken = 0;

        while (m.find()) {
            int lines = m.group().length() - m.group().replace("\n", "").length();
            row += lines;
            //System.out.println("Match: '" + m.group() + "' " + row);
           // System.out.println("charat: " + m.group().charAt(0));
            if (m.group().startsWith("%") || m.group().matches("\\s+")) {
               // System.out.println("Comment or whitespace: " + m.group());
                inputPos = m.end();
                continue;
            }

            if (m.group().matches("(?i)FORW")) {
                tokens.add(new Token(TokenType.Forw, null, row));
            } else if (m.group().matches("(?i)BACK")) {
                tokens.add(new Token(TokenType.Back, null, row));
            } else if (m.group().matches("(?i)LEFT")) {
                tokens.add(new Token(TokenType.Left, null, row));
            } else if (m.group().matches("(?i)RIGHT")) {
                tokens.add(new Token(TokenType.Right, null, row));
            } else if (m.group().matches("(?i)DOWN")) {
                tokens.add(new Token(TokenType.Down, null, row));
            } else if (m.group().matches("(?i)UP")) {
                tokens.add(new Token(TokenType.Up, null, row));
            } else if (m.group().matches("(?i)COLOR")) {
                tokens.add(new Token(TokenType.Color, null, row));
            } else if (m.group().matches("(?i)REP")) {
                tokens.add(new Token(TokenType.Rep, null, row));
            } else if (m.group().matches("#[0-9A-Fa-f]{6}")) {
                tokens.add(new Token(TokenType.Hex, m.group(), row));
            } else if (m.group().matches("\\d+")) {
                tokens.add(new Token(TokenType.Decimal, Integer.parseInt(m.group()), row));
            } else if (m.group().matches("\"")) {
                tokens.add(new Token(TokenType.Quote, null, row));
            } else if (m.group().matches("\\.")) {
                tokens.add(new Token(TokenType.Period, null, row));
            } else {
                //System.out.println("Error1: " + m.group());
                tokens.add(new Token(TokenType.Error, null, row));
            }
            lastrow = row;
            inputPos = m.end();
        }

        if (inputPos != input.length()) {
            String remaining = input.substring(inputPos);

            if (remaining.matches("\\s*") || remaining.matches("%.*")) {
            } else {
                //System.out.println("Error2: " + remaining);
                tokens.add(new Token(TokenType.Error, null, row));
            }
        }
    }

    // Kika på nästa token i indata, utan att gå vidare
    public Token peekToken() throws SyntaxError {
        // Slut på indataströmmen
        if (!hasMoreTokens()) {
            // System.out.println("hej");
            throw new SyntaxError(lastrow);
        }

        return tokens.get(currentToken);
    }

    // Hämta nästa token i indata och gå framåt i indata
    public Token nextToken() throws SyntaxError {
        Token res = peekToken();
        currentToken++;
        return res;
    }

    public Token aheadToken() throws SyntaxError {
        return tokens.get(currentToken + 1);

    }

    public boolean hasMoreTokens() {
        return currentToken < tokens.size();
    }
}