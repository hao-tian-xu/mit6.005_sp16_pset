/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    /** assertion enabled */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Testing strategy
    //   TODO

    @Test
    public void testExpression() {
        Times times = new Times(new Plus(new Num(1), new Variable("x")), new Num(2.2));
        assertEquals("(1+x)*2.2", times.toString());
    }
}
