package expressivo;

import java.util.Map;

/** Variable as Expression which represents a variable */
class Variable implements Expression {
    private final String v;

    // Abstraction function
    //    AF(v) = the variable v
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final

    /** Make a Variable. */
    public Variable(String v) {
        this.v = v;
    }

    @Override
    public Expression differentiate(Variable variable) {
        if (this.equals(variable)) return new Num(1);
        else return new Num(0);
    }

    @Override
    public Expression simplify(Map<String,Double> environment) {
        if (environment.containsKey(this.toString())) return new Num(environment.get(this.toString()));
        else return this;
    }

    @Override
    public String toString() {
        return v;
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Variable && this.sameValue((Variable) that);
    }

    private boolean sameValue(Variable that) {
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return v.hashCode();
    }
}
