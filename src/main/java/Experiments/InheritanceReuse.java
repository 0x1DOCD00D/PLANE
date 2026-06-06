package Experiments;

class Humann{
    int maxAge = 500;
    void eats(int calories){}
}

class Devon extends Humann{
    int maxAge = 100;
    public Devon(int maxAge){
        super.maxAge = maxAge;
    }
}

class Mark extends Humann {
    int maxAge = 10;
    void eats(String food) {
        System.out.println(food);
    }
    @Override
    void eats(int calories) {
        System.out.println("Mark");
    }
    
}

public class InheritanceReuse {
    public static void main(String[] args) {
        Humann d = new Devon(1000);
        System.out.println(d.maxAge);
    }
}
