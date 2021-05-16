package io.netty.example.io.nio.udp;

import io.netty.example.io.nio.udp.DatagramChannelDemo;

/**
 * @author SongOf
 * @ClassName UDPClient
 * @Description
 * @Date 2021/5/15 23:59
 * @Version 1.0
 */
public class UDPClient {
    public static void main(String[] args) {
        String info = "I'm Client!";
        DatagramChannelDemo.send(info);
    }
}
