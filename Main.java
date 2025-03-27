import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws java.io.IOException, SyntaxError {
        Lexer lexer = new Lexer(System.in);
		Parser parser = new Parser(lexer);
		ParseTree result = parser.parse();
        result.printTree();
        //System.out.println(result.getChildren().get(0).getChildren().get(0));
        List<ParseTree> tree = result.getChildren();
        while(tree != null){
            for(ParseTree token : tree){
                //System.out.println(token);
            }
        }
		// Parsning klar, gör vad vi nu vill göra med syntaxträdet
        /*try {
            File myObj = new File("input.in");
            Scanner myReader = new Scanner(myObj);
            int line = 1;
            while (myReader.hasNextLine()) 
            {
                String data = myReader.nextLine();
                List<Token> tokens = lexer.readInput(data);
                int x = 0;
                int y = 0;
                String colour = "#0000FF";
                int penstate = 0; //0 = up, 1 = down
                
                System.out.println(data);
                for (Token token : tokens) 
                {
                    System.out.println("Value: " + token.getData() + ", Token: " + token.getType());
                }
                line++;
            }
            myReader.close();
            } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }*/
    }
}
