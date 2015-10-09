package estacio.exercises.av1calc.core;

import java.util.Stack;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collection;

import estacio.exercises.av1calc.App;
import estacio.exercises.av1calc.R;
import estacio.exercises.av1calc.domain.Token;
import estacio.exercises.av1calc.domain.TokenUtil;
import estacio.exercises.av1calc.domain.enums.TokenType;

public class CalculatorExpression {

    private String expression = "";
    private String expressionRPN = "";
    private Collection<Token> expressionTokens;
    private Collection<Token> expressionTokensRPN;

    public CalculatorExpression(String expression) {
        if(!validateExpression(expression))
            throw new IllegalArgumentException(App.getAppContext().getString(R.string.exception_expr_invalid));

        this.expression = expression;
    }

    public double execute() throws IllegalArgumentException {
        this.expressionTokens = TokenUtil.extractTokens(this.expression);
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

    private boolean validateExpression(String expression) {
        return true;
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
        while(!stack.empty()) {
            tokens.add(stack.pop());
        }

        return tokens;
    }

    private double calculateRPN() {
        Stack<Double> operands = new Stack<>();

        for (Token item : this.expressionTokensRPN) {
            if (item.getType() == TokenType.NUMBER) {
                operands.push(item.getValue());
            } else {
                double result = 0, value1, value2 = 0;

                // square root only use one operand
                if(item.getType() == TokenType.OP_SQRT) {
                    value1 = operands.pop();
                } else {
                    value2 = operands.pop();
                    value1 = operands.pop();
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
                operands.push(result);
            }
        }
        return operands.pop();
    }
}
