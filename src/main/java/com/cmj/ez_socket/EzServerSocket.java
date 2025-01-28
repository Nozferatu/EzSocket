package com.cmj.ez_socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private DataOutputStream clientOutput;
    private DataInputStream clientInput;

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
            clientInput = new DataInputStream(clientSocket.getInputStream());
            clientOutput = new DataOutputStream(clientSocket.getOutputStream());

            System.out.printf("Connected with client %s\n", clientSocket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readInteger(){
        if(clientInput != null){
            int num;

            try {
                num = clientInput.readInt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        }else return -1;
    }

    public void writeInt(int n){
        if(clientOutput != null){
            try {
                clientOutput.writeInt(n);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public float readFloat(){
        if(clientInput != null){
            float num;

            try {
                num = clientInput.readFloat();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        }else return -1f;
    }

    public void writeFloat(float n){
        if(clientOutput != null){
            try {
                clientOutput.writeFloat(n);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String readString(){
        if(clientInput != null){
            String data;

            try {
                data = clientInput.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return data;
        } else return "";
    }

    public void writeString(String text){
        if(clientOutput != null){
            try {
                clientOutput.writeUTF(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
