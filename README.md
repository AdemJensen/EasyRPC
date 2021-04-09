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

The Java version of this project is 13. But any version with Lambda support(11 or newer) is ok.

I recommend using IntelliJ Idea as your Java IDE, especially if you are new to Java. This project is built with Idea.

## How to use
Under folder `src/top/chorg/easyrpc/demo`, there are 2 files: `ClientMain` and `ServerMain`. You need to run `ServerMain.main()` first and run `ClientMain.main()`. You will see the following output if everything works:

```
// Server:
[RPC Server] Receiving new connection.
[RPC Thread 403097490] Receiving RPC request '{"id":2,"funcName":"helloWorld","params":[1,"233"]}'
[RPC Thread 403097490] Sending RPC return '{"id":2,"funcName":"helloWorld","returnValue":"ThisIsOutputOfHelloWorld(1, 233)"}'
[RPC Thread 403097490] Receiving RPC request '{"id":3,"funcName":"aloha","params":["芜湖"]}'
[RPC Thread 403097490] Sending RPC return '{"id":3,"funcName":"aloha","returnValue":"Aloha! The information is(芜湖)"}'
[RPC Thread 403097490] Receiving null, remote might died, closing connection.

// Client:
[RPC] Sending RPC request: '{"id":2,"funcName":"helloWorld","params":[1,"233"]}'
[RPC] Receiving content: '{"id":2,"funcName":"helloWorld","returnValue":"ThisIsOutputOfHelloWorld(1, 233)"}'
ThisIsOutputOfHelloWorld(1, 233)
[RPC] Sending RPC request: '{"id":3,"funcName":"aloha","params":["芜湖"]}'
[RPC] Receiving content: '{"id":3,"funcName":"aloha","returnValue":"Aloha! The information is(芜湖)"}'
Aloha! The information is(芜湖)
```

## Customize
To add new functions, just open the `RPCServer.java` file and goto the `executeRpcCall(String name, Object[] params)` method:

```java
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
```

As you can see, it's a `switch`, and it uses `name` to identify different functions. You can extend the capability of this RPC server by adding more `case` on it.

## Q&As
**Q: Why can't use this on a real project?**

A: Because this project was not tested throughly, and it really doesn't match any Design Patterns. The purpose of this project was never for real projects.

**Q: In file `RPCServer.java`, function `executeRpcCall(String name, Object[] params)`, it uses `(int) ((double) params[0])` to parse a received int value, why?**

A: This envolves `Gson` library. As you can see, the `Gson` library handles the objects' serialization and deserialization, which means transforming data between Objects and Json Strings(If you don't know what is Json, see [Here](https://www.runoob.com/json/json-tutorial.html)). The problem is, when handling digits from String, the `Gson` library consider them `java.lang.Double` if the type is not specified. In this case, we uses `Object` to transfer all the data(defined in class `RPCRequestObject`), which did not specify a concrete type for `Gson`. So `Gson` automatically consider the digit value as a `Double`. So we need to unbox it from `java.lang.Double` to primitive `double`, and transform it again to `int` to match the parameter list of function `TestFunctions.helloWorld(int, String)`.

**Q: Why not use Reflection systems to register functions?**

A: This is a good idea, but it's too complicated, adding reflection systems does no good for my students, since they basically don't need it.

**Q: Can this RPC system do Two-way Communication?**

A: It can't. The system only considers the basic Client-Calls, which means Client calls, Server answers. If you want to let Server to call a Client's method(like notification systems), you might want to just establish another `ServerSocket` and `Socket`. Heads up: DO NOT creat any `RPCServer` on your client side! The server should never take initiatives on establishing a connection!
