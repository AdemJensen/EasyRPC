package top.chorg.easyrpc.utils;

import top.chorg.easyrpc.demo.TestObj;

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

}
