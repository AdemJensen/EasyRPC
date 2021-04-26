package top.chorg.easyrpc.utils;

import com.google.gson.Gson;

public class RPCRequestObject {
    int id;
    String funcName;
    String[] params;

    public RPCRequestObject(int id, String funcName, Object...params) {
        this.id = id;
        this.funcName = funcName;
        this.params = new String[params.length];
        Gson gson = new Gson();
        for (int i = 0; i < params.length; i++) {
            this.params[i] = gson.toJson(params[i]);
        }
    }

    public int getId() {
        return id;
    }

    public String getFuncName() {
        return funcName;
    }

    public String[] getParams() {
        return params;
    }
}
