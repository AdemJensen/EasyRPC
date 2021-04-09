package top.chorg.easyrpc.utils;

import com.google.gson.Gson;
import top.chorg.easyrpc.RPCServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class RPCServerThread extends Thread {

    RPCServer serverObject;
    Socket clientSocket;
    PrintWriter writer;
    BufferedReader reader;

    boolean closed;

    public RPCServerThread(RPCServer serverObject, Socket clientSocket) throws IOException {
        closed = false;
        this.serverObject = serverObject;
        this.clientSocket = clientSocket;
        writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    @Override
    public void run() {
        super.run();
        while (!closed) {
            String content;
            try {
                content = reader.readLine();
            } catch (IOException e) {
                content = null;
            }
            if (content == null) {
                System.out.printf("[RPC Thread %s] Receiving null, remote might died, closing connection.\n", this.hashCode());
                try {
                    serverObject.closeConnection(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            RPCRequestObject requestObject = new Gson().fromJson(content, RPCRequestObject.class);
            System.out.printf("[RPC Thread %s] Receiving RPC request '%s'\n", this.hashCode(), content);
            Object returnValue = serverObject.executeRpcCall(requestObject.getFuncName(), requestObject.getParams());
            RPCReturnObject returnObject = new RPCReturnObject(
                    requestObject.getId(), requestObject.getFuncName(), returnValue);
            writer.println(new Gson().toJson(returnObject));
            writer.flush();
            System.out.printf("[RPC Thread %s] Sending RPC return '%s'\n", this.hashCode(), new Gson().toJson(returnObject));
        }
    }

    // This function should be called by RPCServer.closeConnection().
    public void closeConnection() throws IOException {
        closed = true;
        clientSocket.close();
        this.interrupt();
    }

}
