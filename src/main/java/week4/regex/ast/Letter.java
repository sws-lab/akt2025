package week4.regex.ast;

/**
 * Tähistab regulaaravaldist, mis sobitub näidatud sümboliga
 * (see sümbol ei pea olema tingimata täht, nagu klassi nimest võiks arvata).
 */
public class Letter extends RegexNode {

    public Letter(char symbol) {
        super(symbol);
        if (symbol == 'ε') {
            throw new IllegalArgumentException("ε-nodes should be created from class Epsilon");
        }
    }

    public char getSymbol() {
        return getType();
    }

    @Override
    public <R> R accept(RegexVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
