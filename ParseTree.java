public class ParseTree {
    private Token token;
    private ParseTree left;
    private ParseTree right;

    public ParseTree(Token token) {
        this.token = token;
        this.left = null;
        this.right = null;
    }

    public Token getToken() {
        return token;
    }

    public ParseTree getLeft() {
        return left;
    }

    public ParseTree getRight() {
        return right;
    }

    public void setLeft(ParseTree left) {
        this.left = left;
    }

    public void setRight(ParseTree right) {
        this.right = right;
    }

    public void printTree(String indent) {
        System.out.println(indent + token.getType() + " : " + token.getData());
        if (left != null)
            left.printTree(indent + "  ");
        if (right != null)
            right.printTree(indent + "  ");
    }

    public void printTree() {
        printTree("");
    }
}
