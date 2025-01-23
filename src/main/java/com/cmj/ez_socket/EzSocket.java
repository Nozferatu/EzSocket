package com.cmj.ez_socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Carlos Madrid Jiménez
 */

public class EzSocket{
    private InetSocketAddress address;
    private Socket socket;
    private OutputStream output;
    private InputStream input;

    public EzSocket(String address, int port){
        this.address = new InetSocketAddress(address, port);
        socket = new Socket();
        try {
            socket.connect(this.address);

            input = socket.getInputStream();
            output = socket.getOutputStream();

            System.out.printf("Socket connected in the address %s\n", socket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readString(int bufferSize){
        if(input != null){
            byte[] buffer = new byte[bufferSize];
            String data;

            try {
                input.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            data = new String(buffer).trim();

            return data;
        } else return "";
    }

    public void writeString(String text){
        try {
            output.write(text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}