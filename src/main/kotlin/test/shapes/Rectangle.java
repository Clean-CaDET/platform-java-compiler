package test.shapes;

public class Rectangle extends Shape {

    public int x;
    public int y;

    public Rectangle(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public double getCircumference() {
        return 2*x + 2*y;
    }

    @Override
    public double getArea() {
        return x*y;
    }
}
