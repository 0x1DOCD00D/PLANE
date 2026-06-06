package Experiments;

public class NormalAbrupt {
    public static int finallyTest() {
        int x = 3;//line 0         
        try {
            try {
                ++x;//line 1                 
                if (true & true | false) throw new RuntimeException("Argh!!"); //line 2 
                return x--; //line 3             
            } catch (Throwable e) {
                return --x;//line 4             
            } finally {
                ++x;//line 5
            }
        } catch (RuntimeException e) {
            return x++;//line 6         
        } finally {
            ++x;//line 7         
        }
    }

    public static void main(String[] args) {
        System.out.println(NormalAbrupt.finallyTest());
    }
}