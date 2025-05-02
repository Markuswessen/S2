// Klass för att representera syntaxfel.  I praktiken vill man nog
// även ha med ett litet felmeddelande om *vad* som var fel, samt på
// vilken rad/position felet uppstod
// Författare: Per Austrin
public class SyntaxError extends Exception {
  private final int line;

  public SyntaxError(int line) {
      this.line = line;
  }

  public int getLine() {
      return line;
  }

  public String getError() {
      return "Syntaxfel på rad " + line;
  }
}
