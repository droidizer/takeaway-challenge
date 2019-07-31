
public class Test {

    @org.junit.Test
    public void main() {

        int[] array = new int[]{-3, -14, -5, 7, 8, 42, 8, 3};

        System.out.println("Hello World! " + solution(array));
        // System.out.println("" + solution(array));
    }

    public String solution(int[] T) {
        int diff = 0;
        int max = 0;
        int n = T.length / 4;
        int[] arr = new int[4];
        for (int i = 0; i < T.length - 1; ++i) {
            diff = (T[i] - T[i + 1]);
            arr[i] = diff;
            if (max < diff) {
                max = diff;
            }
        }
                return "";
    }
//
//    private int isFreezing(int f){
//        return (f < 0)
//    }
//    private int sumOfNegatives(int a, int b){
//        int sum = a-b;
//        return sum ?
//    }
}
