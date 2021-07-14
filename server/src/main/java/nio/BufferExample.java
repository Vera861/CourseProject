package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferExample {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        buffer.putInt(12425);
        buffer.flip();

        RandomAccessFile raf = new RandomAccessFile("server/serverFiles/test.txt", "rw");
        FileChannel channel = raf.getChannel();
        channel.write(buffer);

        buffer.clear();
        channel.read(buffer);
        buffer.flip();
    }
}
