import com.cmj.ez_socket.EzSocket;

import java.util.ArrayList;

public class ClientTest {
    public static void main(String[] args) {
        EzSocket socket = new EzSocket("localhost", 55555);

        socket.writeString("Adiós");
        socket.writeInteger(22696922);
        socket.writeFloat(5.25f);
        //socket.writeFile(new File("./sent_file.txt"));

        ArrayList<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");

        socket.writeArrayList(list);
        list = socket.readArrayList();

        for(String str : list){
            System.out.println(str);
        }

        socket.close();
    }
}
