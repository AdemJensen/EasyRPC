# EasyRPC
A simple, straight-forward RPC utility based on Java Socket and Google Gson.

## Introduction
For the record, this is NOT an official project! I strongly suggest that you don't use it on a real project, this utility is only written for educational purpose!

What is RPC? It means **Remote Procedure Call**, stands for the procedure which **the client calls a method on server**, and the **sever returns its output back to the client**. This makes network systems easier and straight forward.

If you want to learn more about RPC, click [here](https://baike.baidu.com/item/远程过程调用/7854346?fromtitle=RPC&fromid=609861&fr=aladdin).

Using RPC Server and RPC Client, your Java C-S program can easily be constructed! RPC method is widly used.

Underneath them, it's `ServerSocket` and `Socket` working(and `Thread`), really simple!

## Installtion
You need to have `Gson` library included for your project(This file is included in this project under `lib/gson.jar`). If you don't know how to import a jar library for your project, here are references:
- For IntelliJ Idea users(Recommended): [Here](https://jingyan.baidu.com/article/ff42efa9f8161bc19e220225.html)
- For Eclipse users: [Here](https://blog.csdn.net/qq_21808961/article/details/81215590)

The Java version of this project is 13. But any version with Lambda support(8 or newer) is ok.

I recommend using IntelliJ Idea as your Java IDE, especially if you are new to Java. This project is built with Idea.

## How to use
Under folder `src/top/chorg/easyrpc/demo`, there are 2 files: `ClientMain` and `ServerMain`. You need to run `ServerMain.main()` first and run `ClientMain.main()`. You will see the following output if everything works:

```
// Server:
[RPC Server] Receiving new connection.
[RPC Thread 1342691038] Receiving RPC request '{"id":2,"funcName":"helloWorld","params":["1","\"233\""]}'
[RPC Thread 1342691038] Sending RPC return '{"id":2,"funcName":"helloWorld","returnValue":"\"ThisIsOutputOfHelloWorld(1, 233)\""}'
[RPC Thread 1342691038] Receiving RPC request '{"id":3,"funcName":"aloha","params":["\"芜湖\""]}'
[RPC Thread 1342691038] Sending RPC return '{"id":3,"funcName":"aloha","returnValue":"{\"strField\":\"ThisIsStrFieldOfReturn\",\"intField\":66666666}"}'
[RPC Thread 1342691038] Receiving RPC request '{"id":4,"funcName":"objTest","params":["{\"strField\":\"111\",\"intField\":233}"]}'
[RPC Thread 1342691038] Sending RPC return '{"id":4,"funcName":"objTest","returnValue":"\"Aloha! The information is(233, 111)\""}'

// Client:
[RPC] Sending RPC request: '{"id":2,"funcName":"helloWorld","params":["1","\"233\""]}'
[RPC] Receiving content: '{"id":2,"funcName":"helloWorld","returnValue":"\"ThisIsOutputOfHelloWorld(1, 233)\""}'
ThisIsOutputOfHelloWorld(1, 233)
[RPC] Sending RPC request: '{"id":3,"funcName":"aloha","params":["\"芜湖\""]}'
[RPC] Receiving content: '{"id":3,"funcName":"aloha","returnValue":"{\"strField\":\"ThisIsStrFieldOfReturn\",\"intField\":66666666}"}'
Printing: TestObj{strField='ThisIsStrFieldOfReturn', intField=66666666}
[RPC] Sending RPC request: '{"id":4,"funcName":"objTest","params":["{\"strField\":\"111\",\"intField\":233}"]}'
[RPC] Receiving content: '{"id":4,"funcName":"objTest","returnValue":"\"Aloha! The information is(233, 111)\""}'
Aloha! The information is(233, 111)

```

## Customize
To add new functions, just open the `RPCServer.java` file and goto the `executeRpcCall(String name, String[] rawParams)` method:

```java
public class RPCServer {
    
    //...
    
    public Object executeRpcCall(String name, String[] rawParams) {
        switch (name)
        {
            case "helloWorld":
                return TestFunctions.helloWorld(
                        getParam(rawParams[0], int.class), getParam(rawParams[1], String.class));
            case "aloha":
                return TestFunctions.aloha(getParam(rawParams[0], String.class));
            case "objTest":
                return TestFunctions.objTest(getParam(rawParams[0], TestObj.class));
            default:
                System.out.printf("[RPC Server] No matching function name '%s'.\n", name);
        }
        return null;
    }
    
}
```

As you can see, it's a `switch`, and it uses `name` to identify different functions. You can extend the capability of this RPC server by adding more `case` on it.

The `rawParams[]` array passed in all the parameter's JSON strings. To get the original object, use `getPara()` like I did.

Also, don't forget the client side:

```java
TestObj obj = client.executeRpcCall("aloha", TestObj.class, "芜湖")
```

See that second parameter here? That's the type of return value.

## Q&As
**Q: Why can't use this on a real project?**

A: Because this project was not tested throughly, and it really doesn't match any Design Patterns. The purpose of this project was never for real projects.

**Q: Why is `public <T> T getParam(String rawParam, Class<T> classOfParam)` correct?**

A: I copied the signatures from `com.google.gson.Gson.fromJson()`, which is relevant to template functions.

**Q: Why not use Reflection systems to register functions?**

A: This is a good idea, but it's too complicated, adding reflection systems does no good for my students, since they basically don't need it.

**Q: Can this RPC system do Two-way Communication?**

A: It can't. The system only considers the basic Client-Calls, which means Client calls, Server answers. If you want to let Server to call a Client's method(like notification systems), you might want to just establish another `ServerSocket` and `Socket`. Heads up: DO NOT creat any `RPCServer` on your client side! The server should never take initiatives on establishing a connection!
