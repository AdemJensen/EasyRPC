package top.chorg.easyrpc.demo;

import top.chorg.easyrpc.RPCClient;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        RPCClient client = new RPCClient("127.0.0.1", 10001);
        client.start();
        System.out.println(client.executeRpcCall("helloWorld", 1, "233"));
        System.out.println(client.executeRpcCall("aloha", "芜湖"));
    }

}
