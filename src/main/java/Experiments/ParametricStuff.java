package Experiments;

public class ParametricStuff {
    static class BoxStorage <WhateverYouWant2Store>{
        WhateverYouWant2Store storeHere;
        public void set(WhateverYouWant2Store parm){
            storeHere = parm;
        }
        public WhateverYouWant2Store get() {
            return storeHere;
        }
    }
    public static void main(String[] args) {
        BoxStorage <Exception>o = new  BoxStorage<Exception>();
        o.storeHere = new Exception("some message");
        System.out.println(o.storeHere.getMessage());
    }
    
}
