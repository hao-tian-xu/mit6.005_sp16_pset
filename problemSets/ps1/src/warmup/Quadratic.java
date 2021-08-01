package warmup;

import java.util.HashSet;
import java.util.Set;

public class Quadratic {
    /**
     * Find the integer roots of a quadratic equation, ax^2 + bx + c = 0.
     * @param a coefficient of x^2
     * @param b coefficient of x
     * @param c constant term.  Requires that a, b, and c are not ALL zero.
     * @return all integers x such that ax^2 + bx + c = 0.
     */
    public static Set<Integer> roots(int a, int b, int c) {
        Set<Integer> results = new HashSet<>();
        /* Single Degenerate Root */
        if (a == 0) {
            results.add(-c / b);
            return results;
        }
        double temp1 = b*b - 4.0*a*c;
        /* Complex Roots */
        if (temp1 < 0) return results;
        double temp2 = Math.sqrt(temp1);     // use 4.0 instead of 4 to deal with
        double result1 = (-b - temp2) / (2 * a);
        double result2 = (-b + temp2) / (2 * a);
        /* Integer Roots */
        if (isInteger(result1)) results.add((int) result1);
        if (isInteger(result2)) results.add((int) result2);
        return results;
    }

    private static boolean isInteger(double num) {
        double temp = num - (int) num;
        return temp == 0 || temp == 1;
    }

    /**
     * Main function of program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("For the equation x^2 - 4x + 3 = 0, the possible solutions are:");
        Set<Integer> result = roots(1, -4, 3);
        System.out.println(result);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public GitHub repository.
     */
}
