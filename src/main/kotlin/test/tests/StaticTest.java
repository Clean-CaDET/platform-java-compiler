package test.tests;

// Errors in this file are intentional, in order to test some functionalities.
public class StaticTest extends ExtendTest {

    public static StaticTest staticField;
    private Object objectField;

    public StaticTest() {}

    public StaticTest(StaticTest t) {
        print(this.objectField.toString());

        Test test = new Test();
        ExtendTest et = new ExtendTest();

        test.self().selfArg(test).end();

        // TODO If "et" is not defined in scope, lateinit var will crash.
        et.baseField.baseSelf().baseEnd();

        et.baseField.baseField.baseField.baseField.baseEnd();

        SuperTypeTest(objectField);
        SuperTypeTest(this);

        DITest(new ExtendTest());
        DITest(this);

        FailedDITest(baseField);
        FailedDITest(new Test());
    }

    public void print(String s) {}
    public static StaticTest self() {return new StaticTest(null);}
    public static StaticTest itself(StaticTest t) {return t;}
    public StaticTest string(String s) {return new StaticTest(null);}
    public StaticTest method(StaticTest test) {return new StaticTest(test);}

    public void SuperTypeTest(Object object) {}
    public void DITest(SimpleInterface simpleInterface) {}

    public void FailedDITest(SimpleInterface simpleInterface) {}
}
