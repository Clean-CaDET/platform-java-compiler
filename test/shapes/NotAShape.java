package test.shapes;

public class NotAShape {
    private NotAShape obj;

    public NotAShape() {

    }

    private void method() {
        this.obj(new NotAShape());
        new NotAShape();
        this.obj = null;
    }

    private void obj(NotAShape obj) {}
}
