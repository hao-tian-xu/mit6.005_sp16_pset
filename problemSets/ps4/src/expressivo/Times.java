package expressivo;

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
