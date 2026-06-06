package Experiments;

import java.sql.SQLException;

public class GenericsExceptions<T extends Exception> {
    private void throwIt( final Exception t ) throws T {
        throw (T)t;
    }

    public static void main(String[] args) {
        try {
            new GenericsExceptions<RuntimeException>().throwIt(new SQLException());
        } catch(final RuntimeException ex){
            ex.printStackTrace();
        }
    }
}