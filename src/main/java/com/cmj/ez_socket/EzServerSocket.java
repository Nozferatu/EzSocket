package com.cmj.ez_socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Carlos Madrid Jiménez
 */

public class EzServerSocket implements AutoCloseable {
    private boolean verbose;
    private InetSocketAddress address;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataOutputStream clientOutput;
    private DataInputStream clientInput;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;

    public EzServerSocket(String address, int port){
        this.address = new InetSocketAddress(address, port);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(this.address);

            System.out.printf("[SERVER] Listening in the address %s\n", serverSocket.getInetAddress());
            verbose = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public EzServerSocket(String address, int port, boolean verbose){
        this(address, port);
        this.verbose = verbose;
    }

    /**
     * This method blocks until a connection is established to this socket.
     */
    public void accept(){
        try {
            clientSocket = serverSocket.accept();
            clientInput = new DataInputStream(clientSocket.getInputStream());
            clientOutput = new DataOutputStream(clientSocket.getOutputStream());
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientSocket.setKeepAlive(true);

            System.out.printf("[SERVER] Connected with client %s\n", clientSocket.getInetAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Blocks and waits for an Integer to be sent to the client input.
     * @return The integer received through the client input.
     */
    public int readInteger(){
        if(clientInput != null){
            int num;

            try {
                if(verbose) System.out.println("[SERVER] Waiting for Integer...");
                num = clientInput.readInt();
                if(verbose) System.out.printf("[SERVER] Integer received: %d\n", num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        }else return -1;
    }

    /**
     * Write an Integer through the client output.
     * @param n Integer to be sent.
     */
    public void writeInt(int n){
        if(clientOutput != null){
            try {
                clientOutput.writeInt(n);
                if(verbose) System.out.printf("[SERVER] Integer sent: %d\n", n);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Blocks and waits for a Float to be sent to the client input.
     * @return The float received through the client input.
     */
    public float readFloat(){
        if(clientInput != null){
            float num;

            try {
                if(verbose) System.out.println("[SERVER] Waiting for Float...");
                num = clientInput.readFloat();
                if(verbose) System.out.printf("[SERVER] Float received: %f\n", num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        }else return -1f;
    }

    /**
     * Write a Float through the client output.
     * @param n Float to be sent.
     */
    public void writeFloat(float n){
        if(clientOutput != null){
            try {
                clientOutput.writeFloat(n);
                if(verbose) System.out.printf("[SERVER] Float sent: %f\n", n);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Blocks and waits for a Double to be sent to the client input.
     * @return The double received through the client input.
     */
    public double readDouble(){
        if(clientInput != null){
            double num;

            try {
                if(verbose) System.out.println("[SERVER] Waiting for Double...");
                num = clientInput.readDouble();
                if(verbose) System.out.printf("[SERVER] Double received: %f\n", num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        }else return -1.0;
    }

    /**
     * Write a Double through the client output.
     * @param n Float to be sent.
     */
    public void writeDouble(double n){
        if(clientOutput != null){
            try {
                clientOutput.writeDouble(n);
                if(verbose) System.out.printf("[SERVER] Double sent: %f\n", n);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Blocks and waits for a String to be sent to the client input.
     * @return The string received through the client input.
     */
    public String readString(){
        if(clientInput != null){
            String data;

            try {
                if(verbose) System.out.println("[SERVER] Waiting for String...");
                data = clientInput.readUTF();
                if(verbose) System.out.printf("[SERVER] String received: %s\n", data);
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
        if(clientOutput != null){
            try {
                clientOutput.writeUTF(text);
                if(verbose) System.out.printf("[SERVER] String sent: %s\n", text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                if(verbose) System.out.printf("[SERVER] Object received: %s\n", o.toString());
            }else{
                if(verbose) System.out.println("[SERVER] The object sent by the client was not Serializable, returned null");
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
                if(verbose) System.out.printf("[SERVER] Object sent: %s\n", o);
            } else {
                System.out.println("[SERVER] The object does not implement the Serializable interface, thus cannot be sent.");
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
        if(!clientSocket.isClosed()){
            try {
                ArrayList<E> list = new ArrayList<>();
                E item;

                //Get list size
                int listLength = clientInput.readInt();
                if (verbose) System.out.printf("[SERVER] ArrayList size: %d\n", listLength);

                int count = 0;
                while(count != listLength){
                    item = (E) objectInput.readObject();
                    if (verbose) System.out.printf("[SERVER] Object received: %s\n", item.toString());
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
        if(!clientSocket.isOutputShutdown()){
            try {
                clientOutput.writeInt(list.size());

                for(E item: list){
                    objectOutput.writeObject(item);
                    if (verbose) System.out.printf("[SERVER] Object sent: %s\n", item.toString());
                }

                objectOutput.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readFile(){
        if(clientInput != null){
            try {
                //Receive the file name
                String fileName = clientInput.readUTF();
                File file = new File("./received_files/" + fileName);

                //Create directory if it doesn't exist
                if(!file.exists()){
                    if (verbose) System.out.println("[SERVER] Output directory for received files not found. Creating...\n");
                    Files.createDirectory(Paths.get("./received_files/"));
                }
                FileOutputStream outputFile = new FileOutputStream(file);

                byte[] buffer = new byte[1024];

                int count;
                while (clientInput.available() > 0) {
                    count = clientInput.read(buffer);
                    outputFile.write(buffer, 0, count);
                }

                outputFile.close();
                if (verbose) System.out.printf("[SERVER] File received: %s\n", file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        System.out.println("[SERVER] Closing...");
        try {
            if(clientSocket != null) {
                clientInput.close();
                clientOutput.close();
                clientSocket.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
