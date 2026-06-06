package Experiments;
//If a field is declared static, there exists exactly one incarnation of the field, no
//matter how many instances (possibly zero) of the class may eventually be created.
//A static field, sometimes called a class variable, is incarnated when the class is
//initialized (§12.4)

class A1 {
    static int i = 10;
    static class B {
        static int i = 50;
    }
}

public class StaticInsanity {
    public static void main(String[] args) {
        A1 o1 = new A1();
        o1.i = 100;
        A1 o2 = new A1();
        A1 o3 = new A1();
        A1.B o4 = new A1.B();
        System.out.println(o1.i);
        System.out.println(o2.i);
        System.out.println(o3.i);
        System.out.println(o4.i);
    }
}
