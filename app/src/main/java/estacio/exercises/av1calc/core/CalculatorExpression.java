package estacio.exercises.av1calc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

import estacio.exercises.av1calc.App;
import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.domain.Token;
import estacio.exercises.av1calc.domain.enums.TokenType;

public class CalculatorExpression {

    private String expression = "";
    private String expressionRPN = "";
    private Collection<Token> expressionTokens;
    private Collection<Token> expressionTokensRPN;

    public CalculatorExpression(String expression) {
        validateExpression(expression);
        this.expression = expression;
    }

    public double execute() throws IllegalArgumentException {
        this.expressionTokens = extractTokens();
        this.expressionTokensRPN = shuntingYard();

        return calculateRPN();
    }

    public String getRPN() {
        assert(expressionTokensRPN != null);

        if(this.expressionRPN.isEmpty()) {
            this.expressionRPN = Arrays.toString(expressionTokensRPN.toArray());
        }
        return this.expressionRPN;
    }

    private Collection<Token> extractTokens() {
        Collection<Token> tokens = new ArrayList<>(this.expression.length());

        for (int i = 0; i < this.expression.length(); i++) {
            tokens.add(Token.createToken(Character.toString(this.expression.charAt(i))));
        }
        return tokens;
    }

    private void validateExpression(String expression) {

    }

    // Algorithm that converts [algebraic expression] in [Reverse Polish notation] (RPN)
    private Collection<Token> shuntingYard() {
        Stack<Token> stack = new Stack<>();
        Collection<Token> tokens = new LinkedList<>();

        for (Token item : this.expressionTokens) {
            switch (item.getType()) {
                case NUMBER:
                    tokens.add(item);   // if number adds the output list.
                    continue;
                case OP_SUM:
                case OP_SUB:
                case OP_MULT:
                case OP_DIV:
                case OP_POW:
                case OP_SQRT:
                    // if the new operator has less/equals precedence, remove top of stack and push in output list
                    while(!stack.empty() && item.getPrecedence() <= stack.peek().getPrecedence()) {
                        tokens.add(stack.pop());
                    }
                    stack.push(item);
                    continue;
                case OPEN_BRACKET:
                    stack.push(item);
                    continue;
                case END_BRACKET:
                    while(!stack.empty() && stack.peek().getType() != TokenType.OPEN_BRACKET) {
                        tokens.add(stack.pop());
                    }
                    stack.pop(); // remove open bracket
            }
        }

        // puts operators left over in the output list
        tokens.addAll(stack);

        return tokens;
    }

    private double calculateRPN() {
        Stack<Double> stack = new Stack<>();

        for (Token item : this.expressionTokensRPN) {
            if (item.getType() == TokenType.NUMBER) {
                stack.push((double) Integer.parseInt(item.getValue()));
            } else { // Operators
                double result = 0, value1, value2 = 0;

                // square root is using only one operator for now
                if(item.getType() == TokenType.OP_SQRT) {
                    value1 = stack.pop();
                } else {
                    value2 = stack.pop();
                    value1 = stack.pop();
                }

                switch (item.getType()) {
                    case OP_SUM:
                        result = value1 + value2;
                        break;
                    case OP_SUB:
                        result = value1 - value2;
                        break;
                    case OP_MULT:
                        result = value1 * value2;
                        break;
                    case OP_DIV:
                        if (value2 == 0) {
                            throw new IllegalArgumentException(App.getAppContext().getString(R.string.exception_divisor_by_zero));
                        }
                        result = value1 / value2;
                        break;
                    case OP_POW:
                        result = (int) Math.pow(value1, value2);
                        break;
                    case OP_SQRT:
                        result = (int) Math.sqrt(value1);
                        break;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
}
