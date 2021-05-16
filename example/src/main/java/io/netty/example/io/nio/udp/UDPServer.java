package io.netty.example.io.nio.udp;

import io.netty.example.io.nio.udp.DatagramChannelDemo;

/**
 * @author SongOf
 * @ClassName UDPServer
 * @Description
 * @Date 2021/5/16 0:02
 * @Version 1.0
 */
public class UDPServer {
    public static void main(String[] args) {
        while (true) {
            DatagramChannelDemo.reveive();
        }
    }
}
