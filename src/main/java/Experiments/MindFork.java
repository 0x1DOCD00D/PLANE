package Experiments;

public class MindFork {
    boolean rv() {
        try {
            return true;
        } catch (Exception e) {
            return true;
        } finally {
            return false;
        }
    }

    public static void main(String[] args) {
        MindFork r = new MindFork();
        System.out.println(r.rv());
    }

}
