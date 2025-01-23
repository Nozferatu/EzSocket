import com.cmj.ez_socket.EzServerSocket;
import com.cmj.ez_socket.EzSocket;

public class Prueba {
    public static void main(String[] args) {
        EzServerSocket serverSocket = new EzServerSocket("localhost", 55555);
        ClienteThread clienteThread = new ClienteThread();

        serverSocket.accept();
        clienteThread.start();

        String cadena = serverSocket.readString(256);
        System.out.println(cadena);

        serverSocket.writeString("nada");
    }

    static class ClienteThread extends Thread{
        private EzSocket socket;
        public ClienteThread(){
            socket = new EzSocket("localhost", 55555);
        }

        @Override
        public void run(){
            socket.writeString("ola ke ase");
            System.out.println(socket.readString(256));
        }
    }
}

