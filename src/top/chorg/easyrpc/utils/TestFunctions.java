package top.chorg.easyrpc.utils;

import top.chorg.easyrpc.demo.TestObj;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        // Extract StringField from each element.
        return a.stream().map(subList ->
            subList.stream().map(obj -> obj.strField).collect(Collectors.toList())
        ).collect(Collectors.toList());
    }

}
