import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    private Token token;
    private List<ParseTree> children;

    public ParseTree(Token token) {
        this.token = token;
        this.children = new ArrayList<>();
    }

    public Token getToken() {
        return token;
    }

    public void addChild(ParseTree child) {
        children.add(child);
    }

    public List<ParseTree> getChildren() {
        return children;
    }

    public void printTree() {

        printTreeHelper(this, 0);
    }

    private void printTreeHelper(ParseTree node, int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("  ");
        }
        Token token = node.getToken();
        token = node.getToken();
        System.out.println(indent.toString() + token.getType() + " : " + token.getData());

        for (ParseTree child : node.getChildren()) {
            printTreeHelper(child, level + 1);
        }
    }
}
