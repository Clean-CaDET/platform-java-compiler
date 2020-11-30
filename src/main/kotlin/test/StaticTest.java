package test;

// Errors in this file are intentional, in order to test some functionalities.
public class StaticTest {

    public static StaticTest staticField;

    public StaticTest() {}
    public StaticTest(StaticTest t) {}

    public static StaticTest self() {return new StaticTest();}
    public static StaticTest itself(StaticTest t) {return t;}
}
