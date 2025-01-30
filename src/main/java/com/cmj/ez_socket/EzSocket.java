package com.cmj.ez_socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    /**
     * Blocks and waits for an Integer to be sent to the client input.
     * @return The integer received through the client input.
     */
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

    /**
     * Write an Integer through the client output.
     * @param n Integer to be sent.
     */
    public void writeInteger(int n){
        try{
            output.writeInt(n);
            if(verbose) System.out.printf("[CLIENT] Integer sent: %d\n", n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks and waits for a Float to be sent to the client input.
     * @return The float received through the client input.
     */
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

    /**
     * Write a Float through the client output.
     * @param n Float to be sent.
     */
    public void writeFloat(float n){
        try {
            output.writeFloat(n);
            if(verbose) System.out.printf("[CLIENT] Float sent: %f\n", n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks and waits for a Double to be sent to the client input.
     * @return The double received through the client input.
     */
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

    /**
     * Write a Double through the client output.
     * @param n Float to be sent.
     */
    public void writeDouble(double n){
        try {
            output.writeDouble(n);
            if(verbose) System.out.printf("[CLIENT] Double sent: %f\n", n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks and waits for a String to be sent to the client input.
     * @return The string received through the client input.
     */
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

    /**
     * Write a String through the client output.
     * @param text String to be sent.
     */
    public void writeString(String text){
        try {
            output.writeUTF(text);
            if(verbose) System.out.printf("[CLIENT] String sent: %s\n", text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks and waits for an Object to be sent to the client input.
     * @return The object received through the client input.
     */
    public Object readObject(){
        Object o = null;

        try {
            o = objectInput.readObject();
            if(!(o instanceof EzNotSerializable)){
                if(verbose) System.out.printf("[CLIENT] Object received: %s\n", o.toString());
            }else{
                if(verbose) System.out.println("[CLIENT] The object sent by the client was not Serializable, returned null");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }

    /**
     * Write an Object through the client output. The object must implement the Serializable interface in order to be sent correctly.
     * @param o Object to be sent.
     */
    public void writeObject(Object o){
        try {
            if(o instanceof Serializable){
                objectOutput.writeObject(o);
                if(verbose) System.out.printf("[CLIENT] Object sent: %s\n", o);
            } else {
                System.out.println("[CLIENT] The object does not implement the Serializable interface, thus cannot be sent.");
                objectOutput.writeObject(new EzNotSerializable());
            }
            objectOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Blocks and waits for an ArrayList list to be sent to the client input.
     * @return The arraylist received through the client input.
     */
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

    /**
     * Write an ArrayList list through the client output. The objects contained in the list
     * must implement the Serializable interface in order to be sent correctly.
     * @param list List to be sent.
     */
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