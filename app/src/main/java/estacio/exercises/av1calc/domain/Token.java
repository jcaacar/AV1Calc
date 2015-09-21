package estacio.exercises.av1calc.domain;

import android.text.TextUtils;

import estacio.exercises.av1calc.App;
import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.domain.enums.TokenType;

public class Token {

    private String value;
    private TokenType type;
    private int precedence;

    // For operators who do not use precedence
    private Token(String value, TokenType type){
        this.value = value;
        this.type = type;
    }

    private Token(String value, TokenType type, int precedence) {
        this(value, type);
        this.precedence = precedence;
    }

    public static Token createToken(String value) throws IllegalArgumentException {
        Token tok;

        if(TextUtils.isDigitsOnly(value)) {
            tok = new Token(value, TokenType.NUMBER);
        } else if(value.equals("+")) {
            tok = new Token(value, TokenType.OP_SUM, 1);
        } else if(value.equals("-")) {
            tok = new Token(value, TokenType.OP_SUB, 1);
        } else if(value.equals("*")) {
            tok = new Token(value, TokenType.OP_MULT, 2);
        } else if(value.equals("/")) {
            tok = new Token(value, TokenType.OP_DIV, 2);
        } else if(value.equals("^")) {
            tok = new Token(value, TokenType.OP_POW, 3);
        } else if(value.hashCode() == 0x221A) { // Square root
            tok = new Token(value, TokenType.OP_SQRT, 3);
        } else if(value.equals("(")) {
            tok = new Token(value, TokenType.OPEN_BRACKET);
        } else if(value.equals(")")) {
            tok = new Token(value, TokenType.END_BRACKET);
        } else {
            throw new IllegalArgumentException(String.format(App.getAppContext().getString(R.string.exception_invalid_token), value));
        }
        return tok;
    }

    public String getValue() {
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
        return getValue();
    }
}
