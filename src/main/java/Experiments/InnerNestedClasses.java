package Experiments;

public class InnerNestedClasses {
    class Car {
        int i = 10;
        class Engine {
            int i = 20;
            class SparkPlugs {
                int i = 30;
                public void f(){
                    System.out.println(Engine.this.i);
                }
            }
        }
    }
    
    
    public static void main(String[] args) {
        InnerNestedClasses outer = new InnerNestedClasses();
        System.out.println(outer.new Car().i);
        System.out.println(outer.new Car().new Engine().i);
        System.out.println(outer.new Car().new Engine().new SparkPlugs().i);
        outer.new Car().new Engine().new SparkPlugs().f();
        
    }
}
