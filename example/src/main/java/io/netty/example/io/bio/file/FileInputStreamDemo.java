package io.netty.example.io.bio.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author SongOf
 * @ClassName FileInputStreamDemo
 * @Description
 * @Date 2021/5/15 19:19
 * @Version 1.0
 */
public class FileInputStreamDemo {
    public static void main(String[] args) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream("example\\src\\main\\java\\io\\netty\\example\\io\\bio\\text.txt"));
            byte[] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while (bytesRead != -1) {
                System.out.println(new String(buf));
                bytesRead = in.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}