import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Experiment {

    public static void main(String[] args) {
        test7();
    }

    public static void test1() throws UnableToParseException, IOException {
        String file = "/Users/tian/Desktop/mit6.005_sp16/experiment/src/Exp.g";
        Parser<Exp> parser = Parser.compile(new File(file), Exp.ROOT);
        ParseTree<Exp> tree = parser.parse("1[23");
        System.out.println(tree.children().get(1));
    }

    public static void test2() {
        List<Integer> list = Arrays.asList(1);
        System.out.println(list.stream().reduce(Integer::sum).get());
    }

    public static void test4() {
        final String regex = "(?<accidental>[\\^_]{1,2}|=)(?<note>[a-gA-G][,']*)(?<rest>[^\\r\\n]*)\\|";
        final String string = "^c' =b, c D |";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));

            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }
    }

    public static void test5() {
        final String regex = "(?<numerator>\\d+)?(?<slash>/(?<denominator>\\d+)?)?";
        final String string = "1/2";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));

            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }
    }

    public static void test6() {
        final String regex = "[^\\^_=]c'";
        final String string = "c d ^c' =b, c D |";
        final String subst = " \\^c'";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        // The substituted value will be contained in the result variable
        final String result = matcher.replaceAll(subst);

        System.out.println("Substitution result: " + result);
    }

    public static void test7() {
        String note = "C D E F";
        final String regex = "(?<accidental>([\\^_]{1,2}|=)(?<note>[a-gA-G][,']*))(?<rest>[^\\r\\n]*)";
        final Matcher matcher = Pattern.compile(regex).matcher(note);
        if (matcher.find()) {
            final String regex_subst = "[^\\^_=]" + matcher.group("note");
            final String replace = matcher.group("accidental")
                    + matcher.group("rest").replaceAll(regex_subst, matcher.group("accidental"));
            note = matcher.replaceAll(replace);
        }
        System.out.println(note);
    }
}
