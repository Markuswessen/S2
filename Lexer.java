import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    private List<Token> tokens;
    public int currentToken;
    
    private static String readInput(InputStream f) throws java.io.IOException {
		Reader stdin = new InputStreamReader(f);
		StringBuilder buf = new StringBuilder();
		char input[] = new char[1024];
        System.out.println(input);
		int read = 0;
		while ((read = stdin.read(input)) != -1) {
            String chunk = new String(input, 0, read);
            // Check for sentinel value "EOF" to stop reading
            if (chunk.contains("EOF")) {
                int endIndex = chunk.indexOf("EOF");
                buf.append(chunk, 0, endIndex);
                break;
            }
            buf.append(chunk);
        }
		return buf.toString();
	}

    public Lexer(InputStream in) throws java.io.IOException {

        String input = Lexer.readInput(in);
        
        String regex = "FORW|BACK|LEFT|RIGHT|DOWN|UP|COLOR|REP|#[0-9A-Fa-f]{6}|\\d+|\"|\\.|[^A-Za-z0-9#]|\\s+";
        Pattern tokenPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = tokenPattern.matcher(input);
        int inputPos = 0;
        tokens = new ArrayList<>();
        currentToken = 0;
        while (m.find()) {
            if (m.start() != inputPos) {
				tokens.add(new Token(TokenType.Error));
			}
            
            if (m.group().matches("(?i)FORW")) {
                tokens.add(new Token(TokenType.Forw));
            } else if (m.group().matches("(?i)BACK")) {
                tokens.add(new Token(TokenType.Back));
            } else if (m.group().matches("(?i)LEFT")) {
                tokens.add(new Token(TokenType.Left));
            } else if (m.group().matches("(?i)RIGHT")) {
                tokens.add(new Token(TokenType.Right));
            } else if (m.group().matches("(?i)DOWN")) {
                tokens.add(new Token(TokenType.Down));
            } else if (m.group().matches("(?i)UP")) {
                tokens.add(new Token(TokenType.Up));
            } else if (m.group().matches("(?i)COLOR")) {
                tokens.add(new Token(TokenType.Color)); 
            } else if (m.group().matches("(?i)REP")) {
                tokens.add(new Token(TokenType.Rep));     
            } else if (m.group().matches("#[0-9A-Fa-f]{6}")) {
                tokens.add(new Token(TokenType.Hex));
            } else if (Character.isDigit(m.group().charAt(0))){
                tokens.add(new Token(TokenType.Decimal, Integer.parseInt(m.group())));
            } else if (m.group().matches("\"")) {
                tokens.add(new Token(TokenType.Quote));
            } else if (m.group().matches("\\.")) {
                tokens.add(new Token(TokenType.Period));
            }
            inputPos = m.end();
        }
        if (inputPos != input.length()) {
			tokens.add(new Token(TokenType.Error));
		}
        for(int i = 0; i < tokens.size(); i++){
            System.out.println(tokens.get(i).getType());
            System.out.println(tokens.get(i).getData());
        }
        
    }
    // Kika på nästa token i indata, utan att gå vidare
	public Token peekToken() throws SyntaxError {
		// Slut på indataströmmen
		if (!hasMoreTokens()){
            System.out.println("hej");
			throw new SyntaxError();
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

