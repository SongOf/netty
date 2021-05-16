package io.netty.example.io.bio.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SongOf
 * @ClassName ServerSocketDemo
 * @Description
 * @Date 2021/5/15 23:29
 * @Version 1.0
 */
public class ServerSocketDemo {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    public static class Task implements Runnable {
        public Socket clientSocket;
        public Task(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            InputStream in = null;
            int recvMsgSize = 0;
            byte[] recvBuf = new byte[1024];
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Handling client at "+clientAddress);
            try {
                in = clientSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                while((recvMsgSize=in.read(recvBuf))!=-1){
                    byte[] temp = new byte[recvMsgSize];
                    System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);
                    System.out.println(new String(temp));
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8700);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new Task(clientSocket));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(serverSocket != null) {
                    serverSocket.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
