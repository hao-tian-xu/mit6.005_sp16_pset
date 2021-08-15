package expressivo;

//import lib6005.parser.*;
import edu.mit.eecs.parserlib.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {

    // Datatype definition
    //   Expression = Num(n:Number)
    //              + Variable(v:String)
    //              + Plus(left:Expression, right:Expression)
    //              + Times(left:Expression, right:Expression)

    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) throws IllegalArgumentException {
        /*String file = new File("").getAbsolutePath();
        file = file.concat("Expression.g");*/
        String file = "/Users/tian/Desktop/mit6.005_sp16/problemSets/ps4/src/expressivo/Expression.g";
        try {
            Parser<Expressivo> parser = Parser.compile(new File(file), Expressivo.EXPRESSION);
            ParseTree<Expressivo> tree = parser.parse(input);
            /*System.out.println(tree.toString());*/

            return AbstractSyntaxTree.makeAST(tree);
        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("invalid expression");
        } catch (IOException e) {
            throw new IllegalArgumentException("grammar file not found");
        }
    }

    /**
     * @param variable the variable to differentiate by
     * @return expression's derivative with respect to variable.  Must be a valid expression equal
     *         to the derivative
     */
    public Expression differentiate(Variable variable);

    /**
     * @param environment maps variables to values.  Variables are required to be case-sensitive nonempty
     *         strings of letters.  The set of variables in environment is allowed to be different than the
     *         set of variables actually found in expression.  Values must be nonnegative numbers.
     * @return an expression equal to the input, but after substituting every variable v that appears in both
     *         the expression and the environment with its value, environment.get(v).  If there are no
     *         variables left in this expression after substitution, it must be evaluated to a single number.
     *         Additional simplifications to the expression may be done at the implementor's discretion.
     */
    public Expression simplify(Map<String,Double> environment);

    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     *
     * additional rules: no whitespace;
     *                   parentheses only for additions within a production
     *                      e.g. x*(1+2) is valid but x*(1*2) is not
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     *
     * additional rules: Integers that can be represented exactly as a double should be considered equal.
     *                      e.g. x + 1 and x + 1.000 are equal
     *                   different groupings with the same mathematical meaning are equal
     *                      e.g. (3 + 4) + 5 and 3 + (4 + 5) are equal
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
