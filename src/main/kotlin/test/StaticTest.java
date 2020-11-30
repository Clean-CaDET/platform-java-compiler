package test;

public class StaticTest {

    public StaticTest() {}
    public StaticTest(StaticTest t) {}

    public static StaticTest self() {return new StaticTest();}
    public static StaticTest itself(StaticTest t) {return t;}
}
