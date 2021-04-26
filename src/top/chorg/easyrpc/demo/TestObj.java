package top.chorg.easyrpc.demo;

public class TestObj {

    public String strField;
    public int intField;

    public TestObj(String strField, int intField) {
        this.strField = strField;
        this.intField = intField;
    }

    @Override
    public String toString() {
        return "Printing: TestObj{" +
                "strField='" + strField + '\'' +
                ", intField=" + intField +
                '}';
    }
}
