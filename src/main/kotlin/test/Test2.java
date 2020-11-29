package test;

public class Test2 {
    public static int staticCounter;
    public int counter;
    private boolean flag;

    Test2(Test2 test) {

    }

    Test2() {
        counter = 0;
        flag = false;

        hello();
        getCounter(new Object());
        incrementCounter();
        setFlag(true);
        printStatusReport();
        Test2.hello();
        itself(selfArg(new Test2(this)));
        Test1.stat();
    }

    public static Test1 getTest1() { return new Test1(); }
    public static void hello() {}
    public int getCounter(Object parameter) {
        return counter;
    }
    public void raiseFlag() {}
    public void incrementCounter() {
        counter++;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public void printStatusReport() {}
    public void itself(Test2 test2) {}
    public Test2 selfArg(Test2 test2) {return this;}
}
