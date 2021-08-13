package expressivo;

import java.util.Map;

/** Times as Expression which represents the product of two Expressions */
class Times implements Expression {
    private final Expression left, right;

    // Abstraction function
    //    AF(left, right) = the expression left * right
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final

    /** Make a Times which is the product of left and right. */
    public Times(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /** Make an expression which is the simplification of the product of left and right */
    public static Expression make(Expression left, Expression right) {
        // left or right is 0
        if ((left instanceof Num && ((Num) left).value() == 0)
          || right instanceof Num && ((Num) right).value() == 0) return new Num(0);
        // left or right is 1
        if (left instanceof Num && ((Num) left).value() == 1) return right;
        if (right instanceof Num && ((Num) right).value() == 1) return left;
        // left and right are both numbers
        if (left instanceof Num && right instanceof Num)
            return new Num(((Num) left).value() * ((Num) right).value());
        return new Times(left, right);
    }

    @Override
    public Expression differentiate(Variable variable) {
        return Plus.make(Times.make(left, right.differentiate(variable)),
                         Times.make(left.differentiate(variable), right));
    }

    @Override
    public Expression simplify(Map<String,Double> environment) {
        return make(left.simplify(environment), right.simplify(environment));
    }

    @Override
    public String toString() {
        String Left, Right;
        if (left instanceof Plus) Left = "(" + left.toString() + ")";
        else Left = left.toString();
        if (right instanceof Plus) Right = "(" + right.toString() + ")";
        else Right = right.toString();
        return Left + "*" + Right;
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Times && this.sameValue((Times) that);
    }

    private boolean sameValue(Times that) {
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
