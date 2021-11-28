package test.shapes;

public class Run {

    private Run runField;

    public Run() { }

    public static void main(String[] args) {
//        ((Rectangle)new Object).nothing();
        Rectangle rect = new Rectangle(1, 2);
        Square square = new Square(1);
        Cube cube = new Cube(2);
        NotAShape not = new NotAShape();

        rect.getArea();
        rect.getCircumference();

        square.getArea();
        square.getCircumference();

        Run run = new Run();

        run.DITest(rect);
        run.DITest(square);
        run.DITest(cube);

        run.SuperTypeTest(square);
        run.SuperTypeTest(cube);

        run.NullTest(null);
        run.NullTestMultipleArgs(null, null, 2d);   // This fails if '2' is passed to be auto-converted

        // These tests should fail
//        run.FailedNullTest(null, null);
//        run.FailedDITest(run);
//        run.FailedSuperTypeTest(rect);
//        run.FailedSuperTypeTest(square);
//        run.FailedDITest(not);
    }

    public void DITest(Shape shape) {
    }

    public void FailedDITest(Shape shape) {
    }

    public void SuperTypeTest(Rectangle rect) {
    }

    public void FailedSuperTypeTest(Cube cube) {
    }

    public void NullTest(Object object) {
    }

    public void NullTestMultipleArgs(Object object, Run run, double number) {
    }

    public void FailedNullTest(Object object) {
    }
    public void nothing() {}
}
