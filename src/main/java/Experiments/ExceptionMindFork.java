package Experiments;

import java.rmi.server.ServerCloneException;

interface Type1 {
    void f() throws CloneNotSupportedException;
}

interface Type2 {
    void f() throws InterruptedException;
//    void f() throws ServerCloneException//InterruptedException;
}

interface Type3 extends Type1, Type2 {
}

public class ExceptionMindFork implements Type3 {
    public void f() {
        System.out.println("Hello world");
    }

    public static void main(String[] args) {
        Type3 t3 = new ExceptionMindFork();
        t3.f();
    }
}
