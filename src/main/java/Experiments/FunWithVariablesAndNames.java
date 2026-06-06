package Experiments;

public class FunWithVariablesAndNames {
    {
        int i = 3;
        {
            int j = 5;
            {
                int finalI = i;
                {
                    class Inner {
                        int i = 100;

                        void f() {
                            {
                                int i = 10;
                                System.out.println(i);
                            }
                        }
                    }

                    new Inner().f();
                }
            }
            System.out.println(j);
        }
    }
    public static void main(String[] args) {
        new FunWithVariablesAndNames();
    }
}
