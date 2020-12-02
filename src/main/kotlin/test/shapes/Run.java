package test.shapes;

public class Run {

    public static void main(String[] args) {
        Rectangle r = new Rectangle(1, 2);
        Square s = new Square(1);

        r.getArea();
        r.getCircumference();

        s.getArea();
        s.getCircumference();
    }
}
