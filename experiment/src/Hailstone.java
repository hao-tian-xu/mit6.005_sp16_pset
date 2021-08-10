import java.util.ArrayList;
import java.util.List;

public class Hailstone {
    public static List<Integer> hailstoneSequence(int n) {
        List<Integer> list = new ArrayList<Integer>();
        while (n != 1) {
            list.add(n);
            if (n % 2 == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }
        }
        list.add(n);
        System.out.println(maxOfList(list));
        return list;
    }

    public static int maxOfList(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        for (int n : list) {
            max = Math.max(n, max);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println("hailstoneSequence(5)=" + hailstoneSequence(5));
    }

}