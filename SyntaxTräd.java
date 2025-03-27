import java.util.List;

class SyntaxTree {
    // Implementation av syntaxträdet för Leona-språket
    // Klassen kan ha olika metoder och egenskaper för att representera syntaxträdet.
    // I det här fallet har vi inte definierat detaljerna för syntaxträdet, utan det är bara en stub.
    // Du kan anpassa och utöka klassen för att passa dina behov.
}

public class SyntaxTräd {
    private List<Token> tokens;
    private int currentPosition;

    public SyntaxTräd(List<Token> tokens) {
        this.tokens = tokens;
        currentPosition = 0;
    }

    public SyntaxTree parse() {
        return program();
    }

    private SyntaxTree program() {
        // Implementera parsning av programmet här
        // Anropa andra hjälpmetoder för att bearbeta instruktioner, repetitioner etc.
        // Returnera syntaxträdet för hela programmet
        return null; // Tillfälligt returnera null
    }

    // Implementera metoder för att bearbeta och parsna olika instruktioner, repetitioner etc.
    // Använd currentPosition och tokens för att navigera genom token-sekvensen och producera syntaxträdet.
    // Varje hjälpmetod bör returnera det motsvarande syntaxträdet för den specifika instruktionen.

    // Exempel på hjälpmetoder:
    private SyntaxTree movement() {
        // Implementera parsning av rörelseinstruktioner (FORW och BACK)
        // Returnera syntaxträdet för rörelseinstruktionen
        return null; // Tillfälligt returnera null
    }

    private SyntaxTree turn() {
        // Implementera parsning av svänginstruktioner (LEFT och RIGHT)
        // Returnera syntaxträdet för svänginstruktionen
        return null; // Tillfälligt returnera null
    }

    private SyntaxTree pen() {
        // Implementera parsning av penninstruktioner (DOWN och UP)
        // Returnera syntaxträdet för penninstruktionen
        return null; // Tillfälligt returnera null
    }

    private SyntaxTree color() {
        // Implementera parsning av färginstruktioner (COLOR)
        // Returnera syntaxträdet för färginstruktionen
        return null; // Tillfälligt returnera null
    }

    private SyntaxTree repeat() {
        // Implementera parsning av upprepningar (REP)
        // Anropa andra hjälpmetoder för att bearbeta den inre sekvensen av instruktioner
        // Returnera syntaxträdet för upprepningen
        return null; // Tillfälligt returnera null
    }
}
