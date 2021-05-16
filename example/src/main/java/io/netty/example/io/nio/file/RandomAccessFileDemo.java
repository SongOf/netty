package io.netty.example.io.nio.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author SongOf
 * @ClassName RandomAccessFileDemo
 * @Description
 * @Date 2021/5/15 23:11
 * @Version 1.0
 */
public class RandomAccessFileDemo {
    public static void main(String[] args) {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("example\\src\\main\\java\\io\\netty\\example\\io\\bio\\text.txt", "rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);

            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);
            while (bytesRead != -1) {
                buf.flip();
                System.out.println(new String(buf.array()));
                buf.compact(); //buf.clear();
                bytesRead = fileChannel.read(buf);
            }
            buf.clear();
            buf.put("\r\n".getBytes());
            buf.put("文件追加内容".getBytes());
            buf.flip();
            while (buf.hasRemaining()) {
                fileChannel.write(buf);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(aFile != null) {
                    aFile.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void map() {
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        try {
            aFile = new RandomAccessFile("src/1.ppt","rw");
            fc = aFile.getChannel();
            long timeBegin = System.currentTimeMillis();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length());
            // System.out.println((char)mbb.get((int)(aFile.length()/2-1)));
            // System.out.println((char)mbb.get((int)(aFile.length()/2)));
            //System.out.println((char)mbb.get((int)(aFile.length()/2)+1));
            long timeEnd = System.currentTimeMillis();
            System.out.println("Read time: "+(timeEnd-timeBegin)+"ms");
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(aFile!=null) {
                    aFile.close();
                }
                if(fc!=null) {
                    fc.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void transferFrom() {
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;
        try {
            fromFile = new RandomAccessFile("src/fromFile.xml","rw");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile("src/toFile.txt","rw");
            FileChannel toChannel = toFile.getChannel();

            long position = 0;
            long count = fromChannel.size();
            System.out.println(count);
            toChannel.transferFrom(fromChannel, position, count);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fromFile != null) {
                    fromFile.close();
                }
                if(toFile != null) {
                    toFile.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void transferTo() {
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;
        try {
            fromFile = new RandomAccessFile("src/fromFile.txt","rw");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile("src/toFile.txt","rw");
            FileChannel toChannel = toFile.getChannel();

            long position = 0;
            long count = fromChannel.size();
            System.out.println(count);
            fromChannel.transferTo(position, count,toChannel);
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fromFile != null) {
                    fromFile.close();
                }
                if(toFile != null) {
                    toFile.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
