package expressivo;

//import lib6005.parser.ParseTree;
import edu.mit.eecs.parserlib.*;

import java.util.List;

class AbstractSyntaxTree {
    static Expression makeAST(ParseTree<Expressivo> tree) {
        switch (tree.name()) {
            case EXPRESSION:
            case PRIMITIVE:
            case PRIMITIVE_SUM:
                return makeAST(tree.children().get(0));
            case SUM:
                final List<ParseTree<Expressivo>> children = tree.children();
                Expression expression = makeAST(children.get(0));
                for (int i=1; i < children.size(); i++) expression = new Plus(expression, makeAST(children.get(i)));
                return expression;
            case PRODUCT:
                final List<ParseTree<Expressivo>> children_p = tree.children();
                Expression expression_p = makeAST(children_p.get(0));
                for (int i=1; i < children_p.size(); i++) expression_p = new Times(expression_p, makeAST(children_p.get(i)));
                return expression_p;
            case NUMBER:
                String num = tree.text();
                if (num.contains(".")) return new Num(Double.parseDouble(num));
                else return new Num(Long.parseLong(num));
            case VARIABLE:
                return new Variable(tree.text());
            case WHITESPACE:
                throw new AssertionError("whitespace");
            default:
                throw new AssertionError("should never get here");
        }
    }
}
