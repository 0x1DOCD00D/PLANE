package Experiments;


public class StaticClassInsanity {
    class A {
        static class B {
            static class C {
                class D {
                    static class E {
                        class F{}
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        A.B.C.D.E.F f = new A.B.C.D.E().new F();
    }
}
