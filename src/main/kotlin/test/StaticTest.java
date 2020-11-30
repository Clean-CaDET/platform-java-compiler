package test;

// Errors in this file are intentional, in order to test some functionalities.
public class StaticTest {

    public static StaticTest staticField;

    public StaticTest(StaticTest t) {
        t.toString();

        Test test = new Test();
        test.end();

        new ExtendTest().extSelf().baseEnd();
        staticField.toString();
    }

    public static StaticTest self() {return new StaticTest();}
    public static StaticTest itself(StaticTest t) {return t;}
}
