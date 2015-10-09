package estacio.exercises.av1calc.domain;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Collection;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;

import estacio.exercises.av1calc.App;
import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.domain.enums.TokenType;

public final class TokenUtil {

    static final int SQUARE_ROOT = 0x221A;

    public static Collection<Token> extractTokens(String expression) throws IllegalArgumentException {
        Collection<Token>  tokens = new LinkedList<>();
        BufferedReader buffer = new BufferedReader(new StringReader(expression));
        try
        {
            int data;
            while((data = buffer.read()) != -1) {
                char c = (char) data;
                Log.i("EXTRACT: ", CharUtils.toString(c));

                Token tok;
                if (Character.isDigit(c)) {
                    buffer.mark(1);
                    StringBuilder sb = new StringBuilder(CharUtils.toString(c));
                    while ((data = buffer.read()) != 1 && Character.isDigit(data)) { // only considering integer
                        sb.append((char) data);
                        buffer.mark(1);
                    }
                    buffer.reset();
                    tok = new Token(TokenType.NUMBER, NumberUtils.toDouble(sb.toString()));
                } else if (c == '+') {
                    tok = new Token(TokenType.OP_SUM, 1);
                } else if (c == '-') {
                    tok = new Token(TokenType.OP_SUB, 1);
                } else if (c == '*') {
                    tok = new Token(TokenType.OP_MULT, 2);
                } else if (c == '/') {
                    tok = new Token(TokenType.OP_DIV, 2);
                } else if (c == '^') {
                    tok = new Token(TokenType.OP_POW, 3);
                } else if (c == SQUARE_ROOT) {
                    tok = new Token(TokenType.OP_SQRT, 3);
                } else if (c == '(') {
                    tok = new Token(TokenType.OPEN_BRACKET);
                } else if (c == ')') {
                    tok = new Token(TokenType.END_BRACKET);
                } else {
                    throw new IllegalArgumentException(String.format(App.getAppContext().getString(R.string.exception_invalid_token), c));
                }
                tokens.add(tok);
            }
            buffer.close();
        } catch (IOException e) {
            Log.e("Stream Closed: ", e.getMessage());
        }
        return tokens;
    }
}
