package com.cmj.ez_socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Carlos Madrid Jiménez
 */

public class EzServerSocket {
    private InetSocketAddress address;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private OutputStream clientOutput;
    private InputStream clientInput;

    public EzServerSocket(String address, int port){
        this.address = new InetSocketAddress(address, port);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(this.address);

            System.out.printf("Server listening in the address %s\n", serverSocket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void accept(){
        try {
            clientSocket = serverSocket.accept();
            clientInput = clientSocket.getInputStream();
            clientOutput = clientSocket.getOutputStream();

            System.out.printf("Connected with client %s\n", clientSocket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readString(int bufferSize){
        if(clientInput != null){
            byte[] buffer = new byte[bufferSize];
            String data;

            try {
                clientInput.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            data = new String(buffer).trim();

            return data;
        } else return "";
    }

    public void writeString(String text){
        if(clientOutput != null){
            try {
                clientOutput.write(text.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
