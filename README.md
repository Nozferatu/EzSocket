# EzSocket, the easy way to use sockets in Java

EzSocket is a Java library made with JDK 11 version that simplifies the use of the Socket library, making it easier to use for newbies. <br>
Includes methods for sending primitive types like int, float, double, and even for more complex types such as ArrayList and files.

Here is an example of how much it changes from using the Socket classes to the EzSocket ones.

<h2> Client example </h2>
With tradicional Socket:

```
Socket socket = new Socket();
InetSocketAddress address = new InetSocketAddress("localhost", 55555);
socket.connect(address);
        
OutputStream output = socket.getOutputStream();
String text = "Hello World!";
output.write(text.getBytes());
```

With EzSocket:

```
EzSocket socket = new EzSocket("localhost", 55555);
socket.writeString("Hello World!");
```

<h2> Server example </h2>
With tradicional Socket:

```
ServerSocket socket = new ServerSocket();
InetSocketAddress address = new InetSocketAddress("localhost", 55555);
socket.bind(address);

Socket client = socket.accept();
InputStream input = client.getInputStream();

byte[] buffer = new byte[64];
input.read(buffer);
String text = new String(buffer).trim();
```

With EzSocket:

```
EzServerSocket serverSocket = new EzServerSocket("localhost", 55555);
serverSocket.accept();

String text = serverSocket.readString();
```

[![](https://jitpack.io/v/Nozferatu/EzSocket.svg)](https://jitpack.io/#Nozferatu/EzSocket)
