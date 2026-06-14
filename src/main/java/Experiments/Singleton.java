package Experiments;

public class Singleton {
    private static  Singleton instance = null;
    public static Singleton getInstance() { 
        if(instance == null) instance = new Singleton();
        return instance; }
     private Singleton() { }

    public static void main(String[] args) {
        var s1 = Singleton.getInstance();
        var s2 = Singleton.getInstance();
        System.out.println(s1 == s2);
    }
}
