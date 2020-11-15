package test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("All")
class Test1 {
    private Map<String, Test1> names = new HashMap<>();
    public static Test2 test2 = new Test2();
    protected double doubleNum = 6;
    private int counter = 5;
    private Test1 testField = new Test1();

    Test1(int intParameter, Object objectParameter) { }

    Test1() { }

    private void test() {
        // Local variables test
        int a;
        double b;
        Test1 test1 = new Test1();

        // Direct field access + function call test
        test1.emptyFunction();

        // Caller-ommited (or 'this') function call test
        self();
        test1.toString();
        this.itself(selfArg(this));
        multipleArgs(1, 2d, this, false);

        // Arbitrary depth field access + function call test (not impl.)
        // test1.self().test2.hello();

        // Static functions through classname test
        Test1.stat();

        // Method chaining test (not impl.)
        // test1.self().self().selfArg(this).itself(self());
    }

    private static void stat(){}
    private void emptyFunction() {}
    private void itself(Test1 test1) {}
    private Test1 self() {return this;}
    private Test1 selfArg(Test1 arg) {return this;}
    // Automatic number conversion when calling functions could be an issue (passing '3' when double is required will be
    // "Ok-d" by the compiler, but JavaParser will read it as an integer)
    private Test1 multipleArgs(int a, double b, Test1 test, boolean bool) {return this;}
}
