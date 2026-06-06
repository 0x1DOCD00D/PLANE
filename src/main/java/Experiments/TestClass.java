package Experiments;

public class TestClass {
    int y = 1;
    public TestClass(){  y = 2;  f();  }
    void f () {System.out.println("Value = " + String.valueOf(y));}
    public static class B extends TestClass {
        static int counter = 0;
        int y = 3;
        public B(){ 
            f(); }
        @Override
        void f () {
            if( counter == 0) {
                y = 5;
                ++ counter;
            }
            System.out.println("Value = " + String.valueOf(y));}
    }
    public static void main(String[] args) {
        TestClass o = new TestClass.B ();
    }}

