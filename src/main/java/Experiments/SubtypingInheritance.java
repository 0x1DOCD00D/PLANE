package Experiments;

class Car {
    void drive(){
        class CarRollsRoyceEngine implements Engine{
            public boolean start() {
                return false;
            }

            @Override
            public String getInternals() {
                return "";
            }
        }

        class CarToyotaEngine implements Engine{
            public boolean start() {
                return false;
            }

            @Override
            public String getInternals() {
                return "";
            }
        }
        new CarRollsRoyceEngine().start();
    }
    interface Engine{
        boolean start();
        String getInternals();
        class CombustionChamber{
            interface SparkPlug {
                boolean createSpark();
            }
        }
    }
}

class Driver{
    void drive(Car car){
        car.drive();
    }
}

class C1{
    void m(){
        System.out.println("C1");
    }
}
class C2 extends C1{
    @Override
    void m(){
        System.out.println("C2");
    }
}
class C3 extends C1{
    @Override
    void m(){
        System.out.println("C3");
    }
}
class C4 extends C2{
    @Override
    void m(){
        System.out.println("C4");
    }
}
class C5 extends C2{
//    @Override
//    void m(){
//        System.out.println("C5");
//    }
}
class C6 extends C3{
    @Override
    void m(){
        System.out.println("C6");
    }
}
class C7 extends C5{
    void f(){
        System.out.println("ouch!");
    }
    @Override
    void m(){
        System.out.println("C7");
    }
}

interface I1{
//    default void dm(){
//        System.out.println("I1");
//    }
    void f();
}
interface I2 extends I1{}

interface I5 extends I1, I2{
    
}

interface I3{
//    default void dm(){
//        System.out.println("I3");
//    }
    void f();
}
class CI implements I1, I3{
    @Override
    public void f() {
    }
}
public class SubtypingInheritance {
    public static void main(String[] args) {
        I1 cio = new CI();
        
        C1 [] storage = {new C1(), new C5(), new C3(), new C4(), new C2()};
        for (int i = 0; i < storage.length; i++) {
            storage[i].m();
        }
        
        I1 i1 = new CI();
        I3 i3 = new CI();
        
        
//        I1 i1 = new I1();
        
//        C5 o1 = new C6();
//        C5 o1 = new C7();
//        C7 o2 = (C7)new C5();
//        o2.f();
//        C1 o3 = new C6();
//        C2 o4 = new C7();
    }
}
