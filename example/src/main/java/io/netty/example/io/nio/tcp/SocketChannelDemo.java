package io.netty.example.io.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author SongOf
 * @ClassName SocketChannelDemo
 * @Description
 * @Date 2021/5/16 0:12
 * @Version 1.0
 */
public class SocketChannelDemo {
    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8700));

            if(socketChannel.finishConnect()) {
                int i = 0;
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    String info = "I'm " + i++ +"-th information from client";
                    buf.clear();
                    buf.put(info.getBytes());
                    buf.flip();
                    while (buf.hasRemaining()) {
                        socketChannel.write(buf);
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                if(socketChannel != null) {
                    socketChannel.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
