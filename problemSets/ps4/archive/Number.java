package expressivo.expression;

import expressivo.Expression;

/** Number as Expression which represents an integer */
class Number implements Expression {
    private final int n;

    // Abstraction function
    //    AF(n) = the integer n
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final

    /** Make a Number. */
    public Number(int n) {
        this.n = n;
    }

    @Override public String toString() {
        return "Number(" + n + ")";
    }
}
