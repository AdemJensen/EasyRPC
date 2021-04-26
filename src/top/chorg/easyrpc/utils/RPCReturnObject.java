package top.chorg.easyrpc.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class RPCReturnObject {
    int id;
    String funcName;
    String returnValue;

    public RPCReturnObject(int id, String funcName, Object returnValue) {
        this.id = id;
        this.funcName = funcName;
        this.returnValue = new Gson().toJson(returnValue);
    }

    public int getId() {
        return id;
    }

    public String getFuncName() {
        return funcName;
    }

    public <T> T getReturnValue(Class<T> classOfT) throws JsonSyntaxException {
        return new Gson().fromJson(returnValue, classOfT);
    }

}
