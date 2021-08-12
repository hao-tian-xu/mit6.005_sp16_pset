package expressivo;

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
