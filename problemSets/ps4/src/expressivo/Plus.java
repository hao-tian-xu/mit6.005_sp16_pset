package expressivo;

import java.util.Map;

/** Plus as Expression which represents the sum of two Expressions */
class Plus implements Expression {
    private final Expression left, right;

    // Abstraction function
    //    AF(left, right) = the expression left + right
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final

    /** Make a Plus which is the sum of left and right. */
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /** Make an expression which is the simplification of the sum of left and right */
    public static Expression make(Expression left, Expression right) {
        // left or right is 0
        if (left instanceof Num && ((Num) left).value() == 0) return right;
        if (right instanceof Num && ((Num) right).value() == 0) return left;
        // left and right are both numbers
        if (left instanceof Num && right instanceof Num) return new Num(((Num) left).value() + ((Num) right).value());
        // left equals right
        if (left.equals(right)) return Times.make(new Num(2), left);
        return new Plus(left, right);
    }

    @Override
    public Expression differentiate(Variable variable) {
        return Plus.make(left.differentiate(variable), right.differentiate(variable));
    }

    @Override
    public Expression simplify(Map<String,Double> environment) {
        return make(left.simplify(environment), right.simplify(environment));
    }

    @Override
    public String toString() {

        return left.toString() + "+" + right.toString();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Plus && this.sameValue((Plus) that);
    }

    private boolean sameValue(Plus that) {
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
