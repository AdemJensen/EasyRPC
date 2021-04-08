package top.chorg.easyrpc.utils;

public class RPCRequestObject {
    int id;
    String funcName;
    Object[] params;

    public RPCRequestObject(int id, String funcName, Object...params) {
        this.id = id;
        this.funcName = funcName;
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public String getFuncName() {
        return funcName;
    }

    public Object[] getParams() {
        return params;
    }
}
