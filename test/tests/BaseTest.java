package test.tests;

import java.util.*;

class BaseTest {
    public BaseTest baseField;

    public BaseTest() {
        baseEnd();
        baseItself(this);

        this.baseEnd();

        print(super.toString());
        //print(((Integer)super.hashCode()).toString()); TODO Throws EnclosedExpr error

        baseSelf().baseItself(this).baseItself(new BaseTest()).baseField.baseEnd();
    }

    public void print(String s) {
    }

    public void baseEnd() {
    }

    public BaseTest baseSelf() {
        return this;
    }

    public BaseTest baseItself(BaseTest t) {
        return this;
    }
}
