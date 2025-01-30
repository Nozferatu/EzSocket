import com.cmj.ez_socket.EzSocket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ClientTest {

    public static void main(String[] args) {
        EzSocket socket = new EzSocket("localhost", 55555, true);

        socket.writeString("Adiós");
        socket.writeInteger(22696922);
        socket.writeFloat(5.25f);
        socket.writeObject(new TestObject("testObject"));

        File testFile = new File("./test_file.txt");
        if(!testFile.exists()) createTestFile(testFile);
        socket.writeFile(testFile);

        /*ArrayList<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");

        socket.writeArrayList(list);
        list = socket.readArrayList();

        for(String str : list){
            System.out.println(str);
        }*/

        socket.close();
    }

    private static void createTestFile(File file){
        try {
            file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);
            output.write("Hi I'm a test file.".getBytes());
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
