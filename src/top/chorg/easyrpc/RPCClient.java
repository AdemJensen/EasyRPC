package top.chorg.easyrpc;

import com.google.gson.Gson;
import top.chorg.easyrpc.utils.RPCRequestObject;
import top.chorg.easyrpc.utils.RPCReturnObject;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class RPCClient {

    Socket socket;
    String host = "";
    int port = -1;

    int lastId = 1;

    int getNextUsableId() {
        do {
            lastId++;
            lastId %= 10000;
        } while (sendMap.containsKey(lastId));
        return lastId;
    }

    PrintWriter writer;
    BufferedReader reader;

    enum RPCClientStatus {
        Stopped, Preparing, Running
    }
    RPCClientStatus status;

    public boolean isClosed() {
        return status == RPCClientStatus.Stopped;
    }

    public void close() throws IOException {
        status = RPCClientStatus.Stopped;
        socket.close();
    }

    public RPCClient() {
        status = RPCClientStatus.Stopped;
    }

    public RPCClient(String host, int port){
        this();
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    Map<Integer, RPCRequestObject> sendMap;
    Map<Integer, RPCReturnObject> receiveMap;

    public void start() throws IOException {
        if (port < 0 || host.length() == 0) throw new IOException();
        socket = new Socket(host, port);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        receiveMap = new HashMap<>();
        sendMap = new HashMap<>();
        status = RPCClientStatus.Preparing;
        new Thread(() -> {
            status = RPCClientStatus.Running;
            while (!isClosed()) {
                String content;
                try {
                    content = reader.readLine();        // Blocking...
                } catch (IOException e) {
                    try {
                        close();
                    } catch (IOException ignored) { }
                    break;
                }
                System.out.printf("[RPC] Receiving content: '%s'\n", content);
                RPCReturnObject returnObject = new Gson().fromJson(content, RPCReturnObject.class);
                if (sendMap.containsKey(returnObject.getId())) receiveMap.put(returnObject.getId(), returnObject);
            }
        }).start();
    }

    // Use this function to call remote procedure.
    public <T> T  executeRpcCall(String name, Class<T> classOfReturnValue, Object...params) {
        while (status == RPCClientStatus.Preparing) Thread.onSpinWait();
        if (isClosed()) {
            System.out.println("[RPC] Error: RPC Client connection not established.");
            return null;
        }
        int id = getNextUsableId();
        RPCRequestObject requestObject = new RPCRequestObject(id, name, params);
        sendMap.put(id, requestObject);
        writer.println(new Gson().toJson(requestObject));
        writer.flush();
        System.out.printf("[RPC] Sending RPC request: '%s'\n", new Gson().toJson(requestObject));
        Object returnValue;
        while (!receiveMap.containsKey(id)) {
            Thread.onSpinWait();
        }
        RPCReturnObject returnObject = receiveMap.get(id);
        if (returnObject.getFuncName().equals(name)) {
            return returnObject.getReturnValue(classOfReturnValue);
        } else {
            System.out.println("[RPC] Error: Return packet ID matched but func name not matched.");
            return null;
        }
    }

}
