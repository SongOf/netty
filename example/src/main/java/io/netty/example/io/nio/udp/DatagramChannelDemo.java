package io.netty.example.io.nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author SongOf
 * @ClassName DatagramChannelDemo
 * @Description
 * @Date 2021/5/15 23:47
 * @Version 1.0
 */
public class DatagramChannelDemo {
    public static void reveive() {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(8700));
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.clear();

            channel.receive(buf);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char)buf.get());
            }
            System.out.println();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(channel != null) {
                    channel.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void send(String info) {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.clear();
            buf.put(info.getBytes());
            buf.flip();
            int bytesSend = channel.send(buf, new InetSocketAddress("127.0.0.1", 8700));
            System.out.println(bytesSend);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(channel != null) {
                    channel.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
