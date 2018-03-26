package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public abstract class ExprCode extends CompoundCode<Code, ExprCode> {
    final String op;

    private ExprCode(String op) {
        this.op = op;
    }

    private static void addValue(Row row, Code code) {
        if (code instanceof CompoundExprCode)
            row.add("(").add(code).add(")");
        else
            row.add(code);
    }

    private static class UnaryExprCode extends ExprCode {
        private final Code code;

        private UnaryExprCode(String op, Code code) {
            super(op);
            this.code = code;
        }

        @Override
        public Element getContent() {
            return new Row()
                    .add(op)
                    .process(row -> addValue(row, code));
        }
    }

    private static class BinaryExprCode extends ExprCode {
        private final Code left;
        private final Code right;

        private BinaryExprCode(String op, Code left, Code right) {
            super(op);
            this.left = left;
            this.right = right;
        }

        @Override
        public Element getContent() {
            return new Row()
                    .process(row -> addValue(row, left))
                    .add(" ")
                    .add(op)
                    .add(" ")
                    .process(row -> addValue(row, right));
        }
    }

    private static class IfExprCode extends CompoundExprCode {
        private final Code testValue;

        public IfExprCode(Code testValue, Code trueValue, Code falseValue) {
            super(":", trueValue, falseValue);
            this.testValue = testValue;
        }

        @Override
        public Element getContent() {
            return new Row()
                    .process(row -> addValue(row, testValue))
                    .add(" ? ")
                    .add(super.getContent());
        }
    }

    private static class CompoundExprCode extends BinaryExprCode {

        private CompoundExprCode(String op, Code left, Code right) {
            super(op, left, right);
        }
    }

    private static Code convert(Object v) {
        if (v instanceof Code)
            return (Code) v;
        return new VariableCode(v.toString());
    }

    public static ExprCode and(ExprCode left, ExprCode right) {
        return new CompoundExprCode("&&", left, right);
    }

    public static ExprCode or(ExprCode left, ExprCode right) {
        return new CompoundExprCode("||", left, right);
    }

    public static ExprCode not(Object code) {
        return new UnaryExprCode("!", convert(code));
    }

    public static ExprCode isNull(Object left) {
        return eq(left, "null");
    }

    public static ExprCode isNotNull(Object left) {
        return neq(left, "null");
    }

    public static ExprCode eq(Object left, Object right) {
        return new BinaryExprCode("==", convert(left), convert(right));
    }

    public static ExprCode neq(Object left, Object right) {
        return new BinaryExprCode("!=", convert(left), convert(right));
    }

    public static ExprCode gt(Object left, Object right) {
        return new BinaryExprCode(">", convert(left), convert(right));
    }

    public static ExprCode gte(Object left, Object right) {
        return new BinaryExprCode(">=", convert(left), convert(right));
    }

    public static ExprCode lt(Object left, Object right) {
        return new BinaryExprCode("<", convert(left), convert(right));
    }

    public static ExprCode lte(Object left, Object right) {
        return new BinaryExprCode("<=", convert(left), convert(right));
    }

    public static ExprCode instanceOf(Object left, Object right) {
        return new BinaryExprCode("instanceof", convert(left), convert(right));
    }

    public static ExprCode ifExpr(Object testValue, Object trueValue, Object falseValue) {
        return new IfExprCode(convert(testValue), convert(trueValue), convert(falseValue));
    }
}
