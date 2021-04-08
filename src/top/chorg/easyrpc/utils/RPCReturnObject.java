package top.chorg.easyrpc.utils;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class RPCReturnObject {
    int id;
    String funcName;
    Object returnValue;

    public RPCReturnObject(int id, String funcName, Object returnValue) {
        this.id = id;
        this.funcName = funcName;
        this.returnValue = returnValue;
    }

    public int getId() {
        return id;
    }

    public String getFuncName() {
        return funcName;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
