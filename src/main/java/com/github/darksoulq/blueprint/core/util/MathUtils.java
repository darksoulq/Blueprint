package com.github.darksoulq.blueprint.core.util;

import java.util.Map;

public class MathUtils {

    public interface MathExpression {
        double evaluate(ExpressionContext context);
    }

    public interface ExpressionContext {
        double getVariable(String name);

        static ExpressionContext empty() {
            return name -> 0.0;
        }

        static ExpressionContext of(Map<String, Double> vars) {
            return name -> vars.getOrDefault(name, 0.0);
        }
    }

    public static class ExpressionParser {
        private final String str;
        private int pos = -1;
        private int ch;

        public ExpressionParser(String str) {
            this.str = str;
        }

        public static MathExpression parse(String input) {
            return new ExpressionParser(input).parse();
        }

        private void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        private boolean consume(int charToEat) {
            while (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        public MathExpression parse() {
            nextChar();
            MathExpression x = parseExpression();
            if (pos < str.length()) {
                throw new RuntimeException("Unexpected token at position " + pos + ": " + (char) ch);
            }
            return x;
        }

        private MathExpression parseExpression() {
            MathExpression x = parseTerm();
            for (;;) {
                if (consume('+')) {
                    MathExpression a = x, b = parseTerm();
                    x = ctx -> a.evaluate(ctx) + b.evaluate(ctx);
                } else if (consume('-')) {
                    MathExpression a = x, b = parseTerm();
                    x = ctx -> a.evaluate(ctx) - b.evaluate(ctx);
                } else {
                    return x;
                }
            }
        }

        private MathExpression parseTerm() {
            MathExpression x = parseFactor();
            for (;;) {
                if (consume('*')) {
                    MathExpression a = x, b = parseFactor();
                    x = ctx -> a.evaluate(ctx) * b.evaluate(ctx);
                } else if (consume('/')) {
                    MathExpression a = x, b = parseFactor();
                    x = ctx -> a.evaluate(ctx) / b.evaluate(ctx);
                } else {
                    return x;
                }
            }
        }

        private MathExpression parseFactor() {
            if (consume('+')) return parseFactor();
            if (consume('-')) {
                MathExpression a = parseFactor();
                return ctx -> -a.evaluate(ctx);
            }

            MathExpression x;
            int startPos = this.pos;

            if (consume('(')) {
                x = parseExpression();
                consume(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                double val = Double.parseDouble(str.substring(startPos, this.pos));
                x = ctx -> val;
            } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') {
                while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || (ch >= '0' && ch <= '9')) nextChar();
                String name = str.substring(startPos, this.pos);
                x = ctx -> ctx.getVariable(name);
            } else {
                throw new RuntimeException("Unexpected character at position " + pos + ": " + (char) ch);
            }

            if (consume('^')) {
                MathExpression a = x, b = parseFactor();
                x = ctx -> Math.pow(a.evaluate(ctx), b.evaluate(ctx));
            }

            return x;
        }
    }
}