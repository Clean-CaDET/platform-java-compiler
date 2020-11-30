package test;

// Errors in this file are intentional, in order to test some functionalities.
public class ExtendTest extends BaseTest {

    public BaseTest baseField;
    public ExtendTest extField;
    public Test testField;

    ExtendTest() {
        // Inherited methods
        super.baseEnd();
        baseEnd();
        super.baseItself(new BaseTest());
        baseItself(new BaseTest());
        this.baseItself(new BaseTest());

        // Static methods
        StaticTest.self();
        StaticTest.itself(new StaticTest());
        StaticTest.self().itself(new StaticTest(new StaticTest()));

        // Local variables
        Test test = new Test();
        BaseTest base = new BaseTest();
        ExtendTest ext = new ExtendTest();

        // Basic method calls
        foreignArg(test);
        extSelf(); this.extEnd();
        extItself(new ExtendTest()); extItself(ext); extItself(this);
        extItself(extItself(ext));

        // Multiple arguments
        multipleArgs(1, 2d, false, new ExtendTest());
        multipleArgs(1, 2d, false, this);

        // Method chaining
        this.extItself(new ExtendTest()).foreignArg(test).extSelf().multipleArgs(1, 2d, false, this).extEnd();
        super.baseItself(base).baseSelf().baseEnd();
        test.selfArg(test).stringArg((String)22).self().end();
        ext.extItself(ext).extEnd();

        // Casting variables
        this.extItself((ExtendTest)new Object());
        this.multipleArgs((int)"s", (double)2, false, this);

        // Variable single access
        new Test().hashCode();
        new Test().end();
        test.hashCode();
        test.end();
        testField.end();
        testField.hashCode();

        new ExtendTest().extEnd();
        new BaseTest().baseEnd();
        base.baseEnd();
        ext.extEnd();
        baseField.baseEnd();
        extField.extEnd();

        // This should fail to resolve!
        this.extItself(new ExtendTest()).foreignArg(test).baseSelf().extEnd();
    }

    // TODO These tests will fail
    // multipleArgs(1, 2, false, this);    Automatic conversion '2'->'2d' allows this, but resolver won't recognize it
    // multipleArgs(1, 2d, false, null);   Nullable arguments

    public void extEnd() {}
    public ExtendTest extSelf() {return this;}
    public ExtendTest extItself(ExtendTest t) {return this;}
    public ExtendTest foreignArg(Test t) {return this;}
    public ExtendTest multipleArgs(int a, double b, boolean c, ExtendTest t) {return this;}
}
