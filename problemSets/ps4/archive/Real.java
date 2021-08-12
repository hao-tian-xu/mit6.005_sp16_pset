package expressivo.expression;

import expressivo.Expression;

/** Real as Expression which represents a real number */
class Real implements Expression {
    private final double l;

    // Abstraction function
    //    AF(l) = the real number l
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final

    /** Make a Real number. */
    public Real(double l) {
        this.l = l;
    }

    @Override public String toString() {
        return "Number(" + l + ")";
    }
}
