package expressivo;

import java.lang.Number;
import java.util.Map;

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
    public Expression differentiate(Variable variable) {
        return new Num(0);
    }

    @Override
    public Expression simplify(Map<String,Double> environment) {
        return this;
    }

    @Override
    public String toString() {
        if (n.longValue() == n.doubleValue()) return Long.toString(n.longValue());
        return String.format("%f", n.doubleValue());
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Num && this.sameValue((Num) that);
    }

    private boolean sameValue(Num that) {
        return this.value() == that.value();
    }

    public double value() {
        return n.doubleValue();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value());
    }
}
