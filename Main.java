import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void traverseParseTree(ParseTree node, Leona leona, String color) {
        if (node == null)
            return;

        Token token = node.getToken();
        List<ParseTree> children = node.getChildren();
        // System.out.println(" token: " + token.getType());
        switch (token.getType()) {
            case Forw:
            case Back:
                Token data = children.get(0).getToken(); // Första barnnoden är alltid en token med data
                if (data.getType() != TokenType.Decimal) {
                    System.out.println(
                            "Error: Expected Decimal token, got " + data.getType() + " token: " + token.getType());
                    return;
                }
                int distance = (Integer) data.getData(); // Läs värdet från token
                double numerator = (leona.getAngle() * Math.PI) / 180;
                double cosValue = Math.cos(numerator);
                double sinValue = Math.sin(numerator);
                if (Math.abs(cosValue) < 1e-10) { // Tolerans för små värden
                    cosValue = 0;
                }
                if (Math.abs(sinValue) < 1e-10) { // Tolerans för PI
                    sinValue = 0;
                }
                double newX = leona.getX() + cosValue * distance * (token.getType() == TokenType.Forw ? 1 : -1);
                double newY = leona.getY() + sinValue * distance * (token.getType() == TokenType.Forw ? 1 : -1);
                // System.out.println("cos: " + numerator);
                // System.out.println("x: " + leona.getX() + " newX: " + newX);
                // System.out.println("y: " + leona.getY() + " newY: " + newY);
                if (leona.isPenDown()) {
                    System.out.printf("%s %.4f %.4f %.4f %.4f%n", color, leona.getX(), leona.getY(), newX, newY);
                }

                leona.setX(newX);
                leona.setY(newY);
                break;

            case Left:
                Token angletoken1 = children.get(0).getToken(); // Första barnnoden är alltid en token med data
                if (angletoken1.getType() != TokenType.Decimal) {
                    System.out.println("Error: Expected Decimal token, got " + angletoken1.getType() + " token: "
                            + token.getType());
                    return;
                }
                leona.setAngle((leona.getAngle() + (Integer) angletoken1.getData()) % 360);
                break;

            case Right:
                Token angletoken2 = children.get(0).getToken(); // Första barnnoden är alltid en token med data
                if (angletoken2.getType() != TokenType.Decimal) {
                    System.out.println("Error: Expected Decimal token, got " + angletoken2.getType() + " token: "
                            + token.getType());
                    return;
                }
                leona.setAngle((leona.getAngle() - (Integer) angletoken2.getData() + 360) % 360);
                break;

            case Down:
                leona.setPenDown(true);
                break;

            case Up:
                leona.setPenDown(false);
                break;

            case Color:
                Token colorToken = children.get(0).getToken(); // Första barnnoden är alltid en token med data
                if (colorToken.getType() != TokenType.Hex) {
                    System.out.println(
                            "Error: Expected Hex token, got " + colorToken.getType() + " token: " + token.getType());
                    return;
                }
                color = (String) colorToken.getData();
                break;

            default:
                break;
        }
        // Fortsätt traversera barnnoder
        for (ParseTree child : children) {
            traverseParseTree(child, leona, color);
            // System.out.println("node: " + node.getToken().getType() + " " +
            // node.getToken().getData());
        }
    }

    public static void main(String[] args) throws java.io.IOException, SyntaxError {
        System.out.println("ja");
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            inputBuilder.append(scanner.nextLine()).append("\n");
            System.out.println("row");
        }
        String result = inputBuilder.toString().replaceAll("\r\n", "\n"); // Standardisera radbrytningar;
        scanner.close();
        
        Lexer lexer = new Lexer(result);
        System.out.println("ja2");
        Parser parser = new Parser(lexer);
        System.out.println("ja3");
        ParseTree Tree = parser.parse();
        System.out.println("ja4");
        // result.printTree();
        Leona leona = new Leona(0, 0, 0, false);
        traverseParseTree(Tree, leona, "#0000FF");
        // System.out.println(result.getChildren().get(0).getChildren().get(0));
        // Parsning klar, gör vad vi nu vill göra med syntaxträdet
        /*
         * try {
         * int line = 1;
         * int x = 0, y = 0; // Startposition
         * int angle = 0; // 0 = höger, 90 = upp, 180 = vänster, 270 = ner
         * String colour = "#0000FF";
         * int penstate = 0; // 0 = up, 1 = down
         * while (myReader.hasNextLine()) {
         * String data = myReader.nextLine();
         * List<Token> tokens = lexer.readInput(data);
         * 
         * System.out.println(data); // Debug: printar rad
         * 
         * for (int i = 0; i < tokens.size(); i++) {
         * Token token = tokens.get(i);
         * TokenType type = token.getType();
         * 
         * if (type == TokenType.Down) {
         * penstate = 1;
         * } else if (type == TokenType.Up) {
         * penstate = 0;
         * } else if (type == TokenType.Color) {
         * if (i + 1 < tokens.size() && tokens.get(i + 1).getType() == TokenType.Hex) {
         * colour = tokens.get(i + 1).getData();
         * i++; // Hoppa över HEX-tokenet
         * }
         * } else if (type == TokenType.Left) {
         * if (i + 1 < tokens.size() && tokens.get(i + 1).getType() ==
         * TokenType.Decimal) {
         * angle = (angle + tokens.get(i + 1).getData()) % 360;
         * i++; // Hoppa över siffertokenet
         * }
         * } else if (type == TokenType.Right) {
         * if (i + 1 < tokens.size() && tokens.get(i + 1).getType() ==
         * TokenType.Decimal) {
         * angle = (angle - tokens.get(i + 1).getData() + 360) % 360;
         * i++;
         * }
         * } else if (type == TokenType.Forw || type == TokenType.Back) {
         * if (i + 1 < tokens.size() && tokens.get(i + 1).getType() ==
         * TokenType.Decimal) {
         * int distance = tokens.get(i + 1).getData();
         * i++;
         * 
         * // Räkna ut ny position
         * double rad = Math.toRadians(angle);
         * int newX = x + (int) Math.round(Math.cos(rad) * distance);
         * int newY = y + (int) Math.round(Math.sin(rad) * distance);
         * 
         * // Rita bara om pennan är nere
         * if (penstate == 1) {
         * System.out.printf("%s %.4f %.4f %.4f %.4f%n", colour, x / 1.0, y / 1.0, newX
         * / 1.0, newY / 1.0);
         * }
         * 
         * // Uppdatera position
         * x = newX;
         * y = newY;
         * }
         * }
         * }
         * }
         * myReader.close();
         * } catch (FileNotFoundException e) {
         * System.out.println("An error occurred.");
         * e.printStackTrace();
         * }
         */
    }
}
