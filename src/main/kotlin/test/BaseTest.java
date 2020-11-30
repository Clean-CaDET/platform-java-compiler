package test;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("All")
class BaseTest {

    public void baseEnd() {}
    public BaseTest baseSelf() {return this;}
    public BaseTest baseItself(BaseTest t) {return this;}

}
