package test.shapes;

public class Run {

    private Run runField;

    public static void main(String[] args) {
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

        run.SuperTypeTest(square);

        run.DITest(cube);
        run.SuperTypeTest(cube);

        run.FailedDITest(run);

        run.FailedSuperTypeTest(rect);
        run.FailedSuperTypeTest(square);

        run.FailedDITest(not);
    }

    public void DITest(Shape shape) {
        // Test whether implementing classes can be passed in as arguments instead of an interface
    }

    public void FailedDITest(Shape shape) {

    }

    public void SuperTypeTest(Rectangle rect) {

    }

    public void FailedSuperTypeTest(Cube cube) {}
}
