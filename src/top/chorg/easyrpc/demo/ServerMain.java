package top.chorg.easyrpc.demo;

import top.chorg.easyrpc.RPCServer;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {
        RPCServer server = new RPCServer(10001);
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
