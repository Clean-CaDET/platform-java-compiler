package test;

public class Test2 {
    public static int staticCounter;
    public int counter;
    private boolean flag;

    Test2() {
        counter = 0;
        flag = false;

        hello();
        getCounter(new Object());
        incrementCounter();
    }

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
}
