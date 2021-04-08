package top.chorg.easyrpc;

import top.chorg.easyrpc.utils.RPCServerThread;
import top.chorg.easyrpc.utils.TestFunctions;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class RPCServer {

    protected ServerSocket serverSocket;
    protected Set<RPCServerThread> threads;

    PrintWriter writer;
    BufferedReader reader;

    int connectionLimit = 100;

    public int getConnectionLimit() {
        return connectionLimit;
    }

    public void setConnectionLimit(int connectionLimit) {
        this.connectionLimit = connectionLimit;
    }

    public int getConnectionCount() {
        return threads.size();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    protected int port = -1;

    public RPCServer() {
        status = RPCServerStatus.Stopped;
        threads = new HashSet<>();
    }

    public RPCServer(int port) {
        this();
        this.port = port;
    }

    enum RPCServerStatus {
        Stopped, Preparing, Running
    }
    RPCServerStatus status;

    public boolean isClosed() {
        return status == RPCServerStatus.Stopped;
    }

    public void startServer() throws IOException {
        if (port < 0) throw new IOException();
        serverSocket = new ServerSocket(port);
        status = RPCServerStatus.Preparing;

        new Thread(() -> {
            status = RPCServerStatus.Running;
            while (!isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[RPC Server] Receiving new connection.");
                    if (getConnectionCount() < getConnectionLimit()) {
                        RPCServerThread newThread = new RPCServerThread(this, clientSocket);
                        threads.add(newThread);
                    } else {
                        clientSocket.close();
                        System.out.println("[RPC Server] Connection pool full, connection closed.");
                    }
                } catch (IOException e) {
                    System.out.println("[RPC Server] IOException raised!");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void closeServer() throws IOException {
        status = RPCServerStatus.Stopped;
        for (RPCServerThread thread : threads) {
            try {
                thread.closeConnection();
            } catch (IOException ignored) { }
        }
        serverSocket.close();
    }

    public void closeConnection(RPCServerThread thread) throws IOException {
        threads.remove(thread);
        thread.closeConnection();
    }

    public Object executeRpcCall(String name, Object[] params) {
        switch (name)
        {
            case "helloWorld":
                return TestFunctions.helloWorld((int) ((double) params[0]), (String) params[1]);
            default:
                System.out.printf("[RPC Server] No matching function name '%s'.\n", name);
        }
        return null;
    }

}
