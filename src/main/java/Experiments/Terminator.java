package Experiments;

import kotlin.reflect.jvm.internal.impl.util.collectionUtils.ScopeUtilsKt;

interface Loving {
    void luv();
}

interface DamagedBody {
    void bleed();
}

interface Human extends Loving, DamagedBody {
}

//The forms using the keyword super are valid only in an instance method, 
// instance initializer, or constructor of a class, or in the initializer 
// of an instance variable of a class. If they appear anywhere else, 
// a compile-time error occurs
class Person implements Human {
    public void luv() {
        
    }
    public void bleed() {

    }
}

interface Robot {
    default void metalSkeleton(){
        System.out.println("screeches");
    }
    boolean charge();
    void kill();
}

class Hollywood{
    void fiction(){
        System.out.println("never happened");
    }
}

class T100 extends Hollywood implements Robot, Human {
    @Override
    public void luv() {
        System.out.println("Summer of love");
        super.fiction();
    }

    @Override
    public void bleed() {
        System.out.println("Ouch!");
    }

    @Override
    public boolean charge() {
        return true;
    }

    @Override
    public void kill() {
        System.out.println("The object copied is terminated");
    }
}

class Processor {
    public <T extends Human & Robot> void service( T i){
        System.out.println(i.toString());
    }
}

public class Terminator {
    public static void main(String[] args) {
        Human h = new T100();
        h.bleed();
        Robot r = (Robot) h;
        r.kill();
        new Processor().service(new T100());
    }
}
