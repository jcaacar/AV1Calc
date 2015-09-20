package estacio.exercises.av1calc.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

import estacio.exercises.av1calc.domain.Token;
import estacio.exercises.av1calc.domain.enums.TokenType;

public class CalculatorExpression {

    private Collection<Token> expressionTokens;

    public CalculatorExpression(String expression) {
        this.expressionTokens = extractTokens(expression);

//        if(!isValid())
//            throw new IllegalArgumentException("Expression invalid, check it!");
    }

    public int execute() {
        Collection<Token> tokensRPN = shuntingYard(expressionTokens);
        return calculateRPN(tokensRPN);
    }

//    private boolean isValid() {
//        boolean fOk = true;
//
//        return fOk == true;
//    }

    private Collection<Token> extractTokens(String expression) {
        Collection<Token> tokens = new ArrayList<>(expression.length());

        for (int i = 0; i < expression.length(); i++) {
            tokens.add(Token.createToken(expression.charAt(i)));
        }
        return tokens;
    }

    // Algorithm that converts [algebraic expression] in [Reverse Polish notation] (RPN)
    private Collection<Token> shuntingYard(Collection<Token> expressionTokens) {
        Stack<Token> stack = new Stack<>();
        Collection<Token> tokens = new LinkedList<>();

        for (Token item : expressionTokens) {
            switch (item.getType()) {
                case NUMBER:
                    tokens.add(item);   // if number adds the output list.
                    continue;
                case OP_SUM:
                case OP_SUB:
                case OP_MULT:
                case OP_DIV:
                case OP_POW:
                    while(!stack.empty() && item.getPrecedence() <= stack.peek().getPrecedence()) { // if the new operator has less/equals precedence, remove top of stack and push in output list
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
        while(!stack.empty()) {
            tokens.add(stack.pop());
        }

        return tokens;
    }

    private int calculateRPN(Collection<Token> rpnTokens) {
        Stack<Integer> stack = new Stack<>();

        for (Token item : rpnTokens) {
            if (item.getType() == TokenType.NUMBER) {
                stack.push(Character.getNumericValue(item.getValue()));
            } else { // Operators
                int result = 0;
                int value1 = stack.pop();
                int value2 = stack.pop();

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
                            throw new IllegalArgumentException("Argument 'divisor' is 0");
                        }
                        result = value1 / value2;
                        break;
                    case OP_POW:
                        result = (int) Math.pow(value1, value2);
                        break;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }
}
