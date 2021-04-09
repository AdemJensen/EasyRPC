package top.chorg.easyrpc.utils;

public class TestFunctions {

    public static String helloWorld(int a, String b) {
        return String.format("ThisIsOutputOfHelloWorld(%d, %s)", a, b);
    }

    public static String aloha(String b) {
        return String.format("Aloha! The information is(%s)", b);
    }

}
