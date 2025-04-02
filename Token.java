// Författare: Per Austrin

// De olika token-typerna vi har i grammatiken
enum TokenType {
    Root, Forw, Back, Left, Right, Down, Up, Color, Rep, Period, Quote, Decimal, Hex, Error
}

// Klass för att representera en token
// I praktiken vill man nog även spara info om vilken rad/position i
// indata som varje token kommer ifrån, för att kunna ge bättre
// felmeddelanden
class Token {
	public TokenType type;
	private Object data;
	
	public Token(TokenType type) {
		this.type = type;
		this.data = null;
	}

	public Token(TokenType type, Object data) {
		this.type = type;
		this.data = data;
	}

	public TokenType getType() { return type; }
	public Object getData() { return data; }

}
