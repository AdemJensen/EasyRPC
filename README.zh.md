# EasyRPC
巨简单，巨直接的 RPC 工具，基于 Java Socket 和 Google Gson。

## 简介
提前说明，这不是一个正式项目！我不建议您将此工具用于真实项目，此项目仅用于教学目的！

什么是 RPC？它是 **远程过程调用（Remote Procedure Call）** 的简称。意思是**客户端调用服务端上的方法**，然后**将输出结果返回给客户端**的过程。RPC 能使得网络系统更简洁明了。

了解更多有关 RPC 的知识，点击[这里](https://baike.baidu.com/item/远程过程调用/7854346?fromtitle=RPC&fromid=609861&fr=aladdin)。

使用 RPC Server and RPC Client，可以快速构建你的 Java C-S 程序！

蕴藏在其中的，是 `ServerSocket` 和 `Socket`（当然还有 `Thread`），非常简单明了！

## 安装
你需要为你的项目引入 `Gson` 库。文件本身已经放在本项目的 `lib/gson.jar` 目录下。如果你不知道如何为你的项目引入 jar 包，请参考以下资料：
- 如果你使用 IntelliJ Idea（推荐）：[这里](https://jingyan.baidu.com/article/ff42efa9f8161bc19e220225.html)
- 如果你使用 Eclipse： [这里](https://blog.csdn.net/qq_21808961/article/details/81215590)

项目的 Java 版本是 13，不过没有这么严格，只要你的 Java 版本能够支持 Lambda 表达式特性（8 及以上）即可。

我推荐使用 IntelliJ Idea 作为你的集成开发编辑器，尤其是刚接触 Java 新手朋友们。这个项目就是用 Idea 做的。

## 如何使用
在 `src/top/chorg/easyrpc/demo` 目录下，有两个文件：`ClientMain` 和 `ServerMain`。 首先运行 `ServerMain.main()`，然后运行 `ClientMain.main()`，注意不要搞错了顺序。如果一切顺利的话，你会看到如下的输出：

```
// 服务端:
[RPC Server] Receiving new connection.
[RPC Thread 403097490] Receiving RPC request '{"id":2,"funcName":"helloWorld","params":[1,"233"]}'
[RPC Thread 403097490] Sending RPC return '{"id":2,"funcName":"helloWorld","returnValue":"ThisIsOutputOfHelloWorld(1, 233)"}'
[RPC Thread 403097490] Receiving RPC request '{"id":3,"funcName":"aloha","params":["芜湖"]}'
[RPC Thread 403097490] Sending RPC return '{"id":3,"funcName":"aloha","returnValue":"Aloha! The information is(芜湖)"}'
[RPC Thread 403097490] Receiving null, remote might died, closing connection.

// 客户端:
[RPC] Sending RPC request: '{"id":2,"funcName":"helloWorld","params":[1,"233"]}'
[RPC] Receiving content: '{"id":2,"funcName":"helloWorld","returnValue":"ThisIsOutputOfHelloWorld(1, 233)"}'
ThisIsOutputOfHelloWorld(1, 233)
[RPC] Sending RPC request: '{"id":3,"funcName":"aloha","params":["芜湖"]}'
[RPC] Receiving content: '{"id":3,"funcName":"aloha","returnValue":"Aloha! The information is(芜湖)"}'
Aloha! The information is(芜湖)
```

## 自定义
要为 RPC Server 添加新的函数，只需要打开 `RPCServer.java` 文件，找到 `executeRpcCall(String name, Object[] params)` 方法：

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

可以看到，这是一个 `switch` 分支结构，通过 `name` 参数来判断需要调用的方法。要扩展 RPC server 的功能，你可以添加更多 `case` 上去。

## Q&As
**Q: 为什么不推荐将这个工具用到真项目中？**

A: 因为这项目没有经过严格的测试，也没有使用任何科学的设计模式。整个工具设计之初就不是为真实项目考虑的。

**Q: 在文件 `RPCServer.java`，函数 `executeRpcCall(String name, Object[] params)` 中，使用了 `(int) ((double) params[0])` 来对函数参数进行转换，这是为什么？**

A: 这涉及到 `Gson` 库了。如你所见，`Gson` 库承担了对象的序列化和反序列化操作，也就是数据在 Object 和 Json 字符串之间的转换。（如果你不知道什么是 Json，看[这里](https://www.runoob.com/json/json-tutorial.html) ）。问题在于，当 `Gson` 将数字的字符串转换为对象时，在没有指定具体类型的情况下，它默认使用 `java.lang.Double` 存储数据。在本案例中，我们通过定义在 `RPCRequestObject` 中的 `Object` 类型变量承载输入数据，这意味着我们没有为 `Gson` 的转换操作指定具体类型。所以 `Gson` 自动认为数字就是 `Double` 类型。而要想满足调用 `TestFunctions.helloWorld(int, String)` 方法的参数列表要求，我们必须先将其从 `java.lang.Double` 转换为 Java 基本类型 `double`（开箱过程），然后再将其转换为基本类型 `int`。

**Q: 为什么没有用反射系统来注册函数？**

A: 好问题。添加反射系统会导致整个项目复杂度大大提高，跟我先前的教育目的相悖。只要这个项目能告诉我的学生如何实现一个 RPC，就够了。

**Q: 这个 RPC 框架能否实现双向通信呢？**

A: 不行。这个系统只能实现基础的 RPC，也就是客户端调用，然后服务端处理。如果想让服务端调用客户端的方法（例如实现通知功能），那还是建议你自己用另一组 `ServerSocket` 和 `Socket` 实现。注意：**千万不要**在客户端上开 `RPCServer` ！服务端从来不应该主动连接客户端！
