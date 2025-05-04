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
    private int lastCodeRow = 1;
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
        + "|(?i)FORW(?=[\\s.])"
        + "|(?i)BACK(?=[\\s.])"
        + "|(?i)LEFT(?=[\\s.])" 
        + "|(?i)RIGHT(?=[\\s.])"
        + "|(?i)DOWN(?=[\\s.])" 
        + "|(?i)UP(?=[\\s.])" 
        + "|(?i)COLOR(?=[\\s.])"
        + "|(?i)REP(?=[\\s.])"
        + "|#[0-9A-Fa-f]{6}(?=[\\s.])"
        + "|\\d+(?=\\s|\\.|\"|$)"
        + "|\""
        + "|\\."
        + "|[^A-Za-z0-9#](?=[\\s.])"
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

            // ✔️ Hoppa över whitespace och kommentarer, men EFTER att ha uppdaterat rad
            if (m.group().matches("\\s+") || m.group().matches("%.*")) {
                continue;
            }

            if (m.group().matches("(?i)FORW")) {
                tokens.add(new Token(TokenType.Forw, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)BACK")) {
                tokens.add(new Token(TokenType.Back, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)LEFT")) {
                tokens.add(new Token(TokenType.Left, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)RIGHT")) {
                tokens.add(new Token(TokenType.Right, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)DOWN")) {
                tokens.add(new Token(TokenType.Down, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)UP")) {
                tokens.add(new Token(TokenType.Up, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)COLOR")) {
                tokens.add(new Token(TokenType.Color, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("(?i)REP")) {
                tokens.add(new Token(TokenType.Rep, null, row));
                lastCodeRow = row;
            } else if (m.group().matches("#[0-9A-Fa-f]{6}")) {
                tokens.add(new Token(TokenType.Hex, m.group(), row));
                lastCodeRow = row;
            } else if (Character.isDigit(m.group().charAt(0))) {
                tokens.add(new Token(TokenType.Decimal, Integer.parseInt(m.group()), row));
            } else if (m.group().matches("\"")) {
                tokens.add(new Token(TokenType.Quote, null, row));
            } else if (m.group().matches("\\.")) {
                tokens.add(new Token(TokenType.Period, null, row));
            } else {
                throw new SyntaxError(lastrow);
            }
            lastrow = row;
            inputPos = m.end();
        }

        if (inputPos != input.length()) {
            if (input.substring(inputPos).matches("\\s*")) {
                // System.out.println("Ignorerar tomma radbrytningar på slutet.");
            } else {
                // System.out.println("Missad input: '" + input.substring(inputPos) + "'");
                tokens.add(new Token(TokenType.Error, null, row));
            }
        }

        for (int i = 0; i < tokens.size(); i++) {
            // System.out.println(tokens.get(i).getType());
            // System.out.println(tokens.get(i).getData());
        }

    }

    // Kika på nästa token i indata, utan att gå vidare
    public Token peekToken() throws SyntaxError {
        // Slut på indataströmmen
        if (!hasMoreTokens()) {
            // System.out.println("hej");
            throw new SyntaxError(lastCodeRow);
        }

        return tokens.get(currentToken);
    }

    // Hämta nästa token i indata och gå framåt i indata
    public Token nextToken() throws SyntaxError {
        Token res = peekToken();
        currentToken++;
        return res;
    }

    public boolean hasMoreTokens() {
        return currentToken < tokens.size();
    }
}
