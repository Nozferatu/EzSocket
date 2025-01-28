package com.cmj.ez_socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Carlos Madrid Jiménez
 */

public class EzSocket{
    private InetSocketAddress address;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    public EzSocket(String address, int port){
        this.address = new InetSocketAddress(address, port);
        socket = new Socket();
        try {
            socket.connect(this.address);

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            System.out.printf("[CLIENT] Connected in the address %s\n", socket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readInteger(){
        if(input != null){
            int num;

            try {
                num = input.readInt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        } else return -1;
    }

    public void writeInteger(int n){
        try{
            output.writeInt(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float readFloat(){
        if(input != null){
            float num;

            try {
                num = input.readFloat();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        } else return 1f;
    }

    public void writeDouble(double n){
        try {
            output.writeDouble(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double readDouble(){
        if(input != null){
            double num;

            try {
                num = input.readDouble();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        } else return 1.0;
    }

    public void writeFloat(float n){
        try {
            output.writeFloat(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readString(){
        if(input != null){
            String data;

            try {
                data = input.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return data;
        } else return "";
    }

    public void writeString(String text){
        try {
            output.writeUTF(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}