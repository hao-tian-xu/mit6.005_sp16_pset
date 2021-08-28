/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

// grammar Expression;

/*
 *
 * You should make sure you have one rule that describes the entire input.
 * This is the "start rule". Below, "root" is the start rule.
 *
 * For more information, see the parsers reading.
 */

expression ::= product | sum;
@skip whitespace{
    product ::= primitive ('*' primitive)*;
    sum ::= primitive_sum ('+' primitive_sum)*;
    primitive ::= number | variable | '(' sum ')' | '(' product ')';
}
primitive_sum ::= primitive | product;
number ::= [0-9]+ | [0-9]* '.' [0-9]+;
variable ::= [a-zA-Z]+;
whitespace ::= [ \t\r\n];
