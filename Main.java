import java.util.Scanner;

public class Main {
    public static void traverseParseTree(ParseTree node, Leona leona) {
        if (node == null)
            return;

        Token token = node.getToken();

        if (node.getToken().getType() == TokenType.Root) {
            traverseParseTree(node.getLeft(), leona);
            traverseParseTree(node.getRight(), leona);
            return;
        }

        switch (token.getType()) {
            case Rep:
                if (node.getLeft() == null || node.getRight() == null)
                    return;
                Token repToken = node.getLeft().getToken();
                if (repToken.getType() != TokenType.Decimal) {
                    System.out.println("Error: Expected Decimal token, got " + repToken.getType());
                    return;
                }
                int repetitions = (Integer) repToken.getData();
                for (int i = 0; i < repetitions - 1; i++) {
                    traverseParseTree(node.getRight(), leona);
                }
                break;
            case Forw:
            case Back:
                if (node.getLeft() == null)
                    return;
                Token data = node.getLeft().getToken();
                if (data.getType() != TokenType.Decimal) {
                    System.out.println(
                            "Error: Expected Decimal token, got " + data.getType() + " token: " + token.getType());
                    return;
                }

                int distance = (Integer) data.getData();
                double angleRad = Math.toRadians(leona.getAngle());
                double cosValue = Math.cos(angleRad);
                double sinValue = Math.sin(angleRad);
                if (Math.abs(cosValue) < 1e-10)
                    cosValue = 0;
                if (Math.abs(sinValue) < 1e-10)
                    sinValue = 0;

                double newX = leona.getX() + cosValue * distance * (token.getType() == TokenType.Forw ? 1 : -1);
                double newY = leona.getY() + sinValue * distance * (token.getType() == TokenType.Forw ? 1 : -1);

                if (leona.isPenDown()) {
                    System.out.printf("%s %.4f %.4f %.4f %.4f%n", leona.getColor(), leona.getX(), leona.getY(), newX,
                            newY);
                }

                leona.setX(newX);
                leona.setY(newY);
                break;

            case Left:
            case Right:
                if (node.getLeft() == null)
                    return;
                Token angleToken = node.getLeft().getToken();
                if (angleToken.getType() != TokenType.Decimal) {
                    System.out.println("Error: Expected Decimal token, got " + angleToken.getType() + " token: "
                            + token.getType());
                    return;
                }
                int angle = (Integer) angleToken.getData();
                if (token.getType() == TokenType.Left) {
                    leona.setAngle((leona.getAngle() + angle) % 360);
                } else {
                    leona.setAngle((leona.getAngle() - angle + 360) % 360);
                }
                break;

            case Down:
                leona.setPenDown(true);
                break;

            case Up:
                leona.setPenDown(false);
                break;

            case Color:
                if (node.getLeft() == null)
                    return;
                Token colorToken = node.getLeft().getToken();
                if (colorToken.getType() != TokenType.Hex) {
                    System.out.println("Error: Expected Hex token, got " + colorToken.getType());
                    return;
                }
                leona.setColor((String) colorToken.getData());
                break;

            default:
                break;
        }

        // Recursively traverse left and right
        traverseParseTree(node.getLeft(), leona);
        traverseParseTree(node.getRight(), leona);
    }

    public static void main(String[] args) throws java.io.IOException, SyntaxError {
        // System.out.println("ja");
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            inputBuilder.append(scanner.nextLine()).append("\n");
            // System.out.println("row");
        }
        String result = inputBuilder.toString().replaceAll("\r\n", "\n"); // Standardisera radbrytningar;
        scanner.close();
        try {
            Lexer lexer = new Lexer(result);
            // System.out.println("ja2");
            Parser parser = new Parser(lexer);
            // System.out.println("ja3");
            ParseTree Tree = parser.parse();
            //Print the parse tree for debugging
            //Tree.printTree();

            Leona leona = new Leona(0, 0, 0, false, "#0000FF");
            traverseParseTree(Tree, leona);
        } catch (SyntaxError e) {
            System.out.println(e.getError());
        }

    }
}
