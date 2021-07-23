package test.tests;

// Errors in this file are intentional, in order to test some functionalities.
public class Test {
    private Test test;
    private int a;
    private String s;

    Test() {
    }

    Test(Test t) {
        self().selfArg(this).selfArg(new Test()).multArg(1, 2d, false, this).stringArg("S").end();
        stringArg(this.toString());
        selfArg((Test) clone());
        stringArg((String) 1);
        // multArg(hashCode(), (double)"string", this.equals(null), new Test());    // Null argument NOT supported
        self();
        selfArg(this);
        end();
        stringArg("Hello");
        toString();
        hashCode();

        test.end();
    }

    // Test methods
    public void end() {
    }

    public Test self() {
        return this;
    }

    public Test selfArg(Test t) {
        return this;
    }

    public Test stringArg(String s) {
        return this;
    }

    public Test multArg(int x, double y, boolean b, Test t) {
        return this;
    }
}
