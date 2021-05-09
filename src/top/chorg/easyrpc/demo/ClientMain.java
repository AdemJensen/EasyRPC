package top.chorg.easyrpc.demo;

import com.google.gson.reflect.TypeToken;
import top.chorg.easyrpc.RPCClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        RPCClient client = new RPCClient("127.0.0.1", 10001);
        client.start();
        System.out.println(client.executeRpcCall("helloWorld", String.class, 1, "233"));
        System.out.println(client.executeRpcCall("aloha", TestObj.class, "芜湖"));
        System.out.println(client.executeRpcCall("objTest", String.class, new TestObj("111", 233)));
        // Test data for "complicate"
        List<List<TestObj>> reqData = List.of(
                List.of(
                        new TestObj("1.1", 0),
                        new TestObj("1.2", 0),
                        new TestObj("1.3", 0),
                        new TestObj("1.4", 0),
                        new TestObj("1.5", 0)
                ),
                List.of(
                        new TestObj("2.1", 0),
                        new TestObj("2.2", 0),
                        new TestObj("2.3", 0),
                        new TestObj("2.4", 0),
                        new TestObj("2.5", 0)
                )
        );
        List<List<String>> result =
                client.executeRpcCall("complicate", new TypeToken<List<List<String>>>(){}.getType(), reqData);
        result.forEach(subRes -> {
            subRes.forEach(str -> System.out.print(str + " | "));
            System.out.println();
        });
        client.close();
    }

}
