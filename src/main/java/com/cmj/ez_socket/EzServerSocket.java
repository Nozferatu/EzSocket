package com.cmj.ez_socket;

import java.io.*;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public double readDouble(){
        if(clientInput != null){
            double num;

            try {
                num = clientInput.readDouble();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return num;
        }else return -1.0;
    }

    public void writeDouble(double n){
        if(clientOutput != null){
            try {
                clientOutput.writeDouble(n);
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

    public <E> ArrayList<E> readArrayList(){
        if(!clientSocket.isClosed()){
            try {
                ArrayList<E> list = new ArrayList<>();
                E item;
                int listLength = readInteger();
                int count = 0;

                while(count != listLength){
                    item = (E) objectInput.readObject();
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
        if(!clientSocket.isOutputShutdown()){
            try {
                clientOutput.writeInt(list.size());

                for(E item: list){
                    objectOutput.writeObject(item);
                    objectOutput.flush();
                }
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {
        System.out.println("Closing...");
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
