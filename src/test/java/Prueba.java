import com.cmj.ez_socket.EzServerSocket;
import com.cmj.ez_socket.EzSocket;

public class Prueba {
    public static void main(String[] args) {
        EzServerSocket serverSocket = new EzServerSocket("localhost", 55555);
        ClienteThread clienteThread = new ClienteThread();

        serverSocket.accept();
        clienteThread.start();

        String cadena = serverSocket.readString();
        System.out.println(cadena);

        int numero = serverSocket.readInteger();
        System.out.println(numero);
    }

    static class ClienteThread extends Thread{
        private EzSocket socket;
        public ClienteThread(){
            socket = new EzSocket("localhost", 55555);
        }

        @Override
        public void run(){
            socket.writeString("Adiós");
            socket.writeInteger(22696922);
        }
    }
}
