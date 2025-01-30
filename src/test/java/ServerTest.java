import com.cmj.ez_socket.EzServerSocket;

import java.util.ArrayList;

public class ServerTest {
    public static void main(String[] args) {
        EzServerSocket serverSocket = new EzServerSocket("localhost", 55555, true);
        serverSocket.accept();

        String text = serverSocket.readString();

        int number = serverSocket.readInteger();

        float decimalNumber = serverSocket.readFloat();

        serverSocket.readFile();

        //ArrayList<String> list = serverSocket.readArrayList();
        //list.add("bear");

        //serverSocket.writeArrayList(list);
    }
}
