import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] a1 = {10, 20, 30, 40, 50, 60};
        int[] a2 = {11, 42, -5, 27, 0, 89};
        swapAll(a1, a2);

        System.out.println(Arrays.toString(a1));
        System.out.println(Arrays.toString(a2));
    }

    public static void swapAll (int[] arr1, int[] arr2) {
        int[] tempArr = new int[arr1.length];
        for (int i = 0; i < arr2.length; i++) {
            tempArr[i] = arr1[i];
            arr1[i] = arr2[i];
            arr2[i] = tempArr[i];
        }
    }
}