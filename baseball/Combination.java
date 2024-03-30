/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Combination {
    public static int combination(int n, int m) {
        // source: https://www.jianshu.com/p/acf0c7105b10
        return factorial(n, n - m) / factorial(m, 0);
    }

    public static int factorial(int n, int limit) {
        // source: https://www.jianshu.com/p/acf0c7105b10
        int res = 1;
        for (int i = n; i > limit; i--) {
            res = res * i;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(factorial(4, 2));

    }
}
