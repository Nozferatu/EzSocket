package com.cmj.ez_socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Carlos Madrid Jiménez
 */

public class EzSocket{
    private boolean verbose;
    private InetSocketAddress address;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;

    public EzSocket(String address, int port){
        this.address = new InetSocketAddress(address, port);
        socket = new Socket();
        try {
            socket.connect(this.address);
            socket.setKeepAlive(true);

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());

            System.out.printf("[CLIENT] Connected in the address %s\n", socket.getInetAddress());
            verbose = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EzSocket(String address, int port, boolean verbose){
        this(address, port);
        this.verbose = verbose;
    }

    public int readInteger(){
        if(input != null){
            int num;

            try {
                if(verbose) System.out.println("[CLIENT] Waiting for Integer...");
                num = input.readInt();
                if(verbose) System.out.printf("[CLIENT] Integer received: %d\n", num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        } else return -1;
    }

    public void writeInteger(int n){
        try{
            output.writeInt(n);
            if(verbose) System.out.printf("[CLIENT] Integer sent: %d\n", n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float readFloat(){
        if(input != null){
            float num;

            try {
                if(verbose) System.out.println("[CLIENT] Waiting for Float...");
                num = input.readFloat();
                if(verbose) System.out.printf("[CLIENT] Float received: %f\n", num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        } else return 1f;
    }

    public void writeFloat(float n){
        try {
            output.writeFloat(n);
            if(verbose) System.out.printf("[CLIENT] Float sent: %f\n", n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double readDouble(){
        if(input != null){
            double num;

            try {
                if(verbose) System.out.println("[CLIENT] Waiting for Double...");
                num = input.readDouble();
                if(verbose) System.out.printf("[CLIENT] Double received: %f\n", num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        } else return 1.0;
    }

    public void writeDouble(double n){
        try {
            output.writeDouble(n);
            if(verbose) System.out.printf("[CLIENT] Double sent: %f\n", n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readString(){
        if(input != null){
            String data;

            try {
                if(verbose) System.out.println("[CLIENT] Waiting for String...");
                data = input.readUTF();
                if(verbose) System.out.printf("[CLIENT] String received: %s\n", data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return data;
        } else return "";
    }

    public void writeString(String text){
        try {
            output.writeUTF(text);
            if(verbose) System.out.printf("[CLIENT] String sent: %s\n", text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object readObject(){
        Object o;

        try {
            o = objectInput.readObject();
            if(verbose) System.out.printf("[CLIENT] Object received: %s\n", o.toString());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return o;
    }

    public void writeObject(Object o){
        try {
            objectOutput.writeObject(o);
            if(verbose) System.out.printf("[CLIENT] Object sent: %s\n", o.toString());
            objectOutput.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <E> ArrayList<E> readArrayList(){
        if(!socket.isClosed()){
            try {
                ArrayList<E> list = new ArrayList<>();
                E item;
                int listLength = input.readInt();
                if (verbose) System.out.printf("[CLIENT] ArrayList size: %d\n", listLength);

                int count = 0;
                while(count != listLength){
                    item = (E) objectInput.readObject();
                    if (verbose) System.out.printf("[CLIENT] Object received: %s\n", item.toString());
                    list.add(item);
                    count++;
                }

                return list;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else return null;
    }

    public <E> void writeArrayList(ArrayList<E> list){
        try {
            output.writeInt(list.size());

            for(E item: list){
                objectOutput.writeObject(item);
                if (verbose) System.out.printf("[CLIENT] Object sent: %s\n", item.toString());
            }

            objectOutput.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile(File file){
        try {
            FileInputStream fileInput = new FileInputStream(file);

            //Send the file name
            output.writeUTF(file.getName());

            byte[] buffer = new byte[1024];
            int count;
            while ((count = fileInput.read(buffer)) > 0) {
                output.write(buffer, 0, count);
            }

            fileInput.close();
            if (verbose) System.out.printf("[CLIENT] File sent: %s\n", file.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}