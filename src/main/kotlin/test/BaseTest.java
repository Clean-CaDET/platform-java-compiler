package test;

import java.util.HashMap;
import java.util.Map;

// Errors in this file are intentional, in order to test some functionalities.
@SuppressWarnings("All")
class BaseTest {
    public BaseTest baseField;

    public void baseEnd() {}
    public BaseTest baseSelf() {return this;}
    public BaseTest baseItself(BaseTest t) {return this;}
}
