import com.cmj.ez_socket.EzServerSocket;

import java.util.ArrayList;

public class ServerTest {
    public static void main(String[] args) {
        EzServerSocket serverSocket = new EzServerSocket("localhost", 55555);
        serverSocket.accept();

        String cadena = serverSocket.readString();
        System.out.println(cadena);

        int numero = serverSocket.readInteger();
        System.out.println(numero);

        float numeroFlotante = serverSocket.readFloat();
        System.out.println(numeroFlotante);

        //serverSocket.readFile();
        ArrayList<String> list = serverSocket.readArrayList();
        for (String str : list) {
            System.out.println(str);
        }
        list.add("bear");

        serverSocket.writeArrayList(list);
    }
}
