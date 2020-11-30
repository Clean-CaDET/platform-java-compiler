package test;

public class ExtendTest extends BaseTest {

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
