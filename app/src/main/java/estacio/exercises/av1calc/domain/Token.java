package estacio.exercises.av1calc.domain;

import estacio.exercises.av1calc.App;
import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.domain.enums.TokenType;

public class Token {

    private double value;
    private TokenType type;
    private int precedence;

    // For operators who do not use precedence
    protected Token(TokenType type){
        this.type = type;
    }

    // For Operator
    protected Token(TokenType type, int precedence){
        this.type = type;
        this.precedence = precedence;
    }

    // For Operand
    protected Token(TokenType type, double value) {
        this.type = type;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public int getPrecedence() {
        return precedence;
    }

    @Override
    public String toString() {
        String result = "";
        switch (this.getType()) {
            case NUMBER:
                result = String.valueOf((int) this.value);
                break;
            case OP_SUM:
                result = "+";
                break;
            case OP_SUB:
                result = "-";
                break;
            case OP_MULT:
                result = "*";
                break;
            case OP_DIV:
                result = "/";
                break;
            case OP_POW:
                result = "^";
                break;
            case OP_SQRT:
                result = App.getAppContext().getString(R.string.square_root);
                break;
            case OPEN_BRACKET:
                break;
            case END_BRACKET:
                break;
        }
        return result;
    }
}
