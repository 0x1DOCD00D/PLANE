package Experiments;

public class NastyCastBooom {
    public static void main(String[] args) {
        Integer [] o = {10, 11};
        Object [] o2 = o;
        o2[0] = "Hello world";
    }
}
