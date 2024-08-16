import java.util.Scanner;

public class CLI {
    public boolean getCLI(int allowed) {
        System.out.print("Enter an integer, preferrably " + allowed + ": ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt() == allowed;

    }
    public static void main(String[] args) {
        System.out.println(new CLI().getCLI(1));
    }
}
