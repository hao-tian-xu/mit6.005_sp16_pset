package expressivo;

import java.lang.Number;

/** Num as Expression which represents a non-negative number */
class Num implements Expression {
    private final Number n;

    // Abstraction function
    //    AF(n) = the non-negative number n
    // Rep invariant
    //    n is non-negative
    // Safety from rep exposure
    //    all fields are immutable and final

    /** Make a Number. */
    public Num(Number n) {
        this.n = n;
    }

    // check RI
    private void checkRep() {
        assert n.doubleValue() >= 0;
    }

    @Override
    public String toString() {
        return n.toString();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Num && this.sameValue((Num) that);
    }

    private boolean sameValue(Num that) {
        return this.value().doubleValue() == that.value().doubleValue();
    }

    private Number value() {
        return n;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value().doubleValue());
    }
}
