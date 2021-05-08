package top.chorg.easyrpc.utils;

import top.chorg.easyrpc.demo.TestObj;

import java.util.ArrayList;
import java.util.List;

public class TestFunctions {

    public static String helloWorld(int a, String b) {
        return String.format("ThisIsOutputOfHelloWorld(%d, %s)", a, b);
    }

    public static TestObj aloha(String b) {
        return new TestObj("ThisIsStrFieldOfReturn", 66666666);
    }

    public static String objTest(TestObj a) {
        return String.format("Aloha! The information is(%d, %s)", a.intField, a.strField);
    }

    public static List<List<String>> complicate(List<List<TestObj>> a) {
        List<List<String>> res = new ArrayList<>();
        for (List<TestObj> testObjs : a) {
            List<String> sub = new ArrayList<>();
            for (TestObj testObj : testObjs) {
                sub.add(testObj.strField);
            }
            res.add(sub);
        }
        return res;
    }

}
