import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChatHandler implements Runnable {

    private final String root = "server/serverFiles";
    private final Socket socket;
    private final byte[] buffer;
    private DataInputStream is;
    private DataOutputStream os;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            while (true) {
                processFileMsg();
            }
        } catch (Exception e) {
            System.err.println("Client connection exeption");
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void processFileMsg() throws IOException {
        String fileName = is.readUTF();
        System.out.println("Received fileName " + fileName);
        Path file = Paths.get(root, fileName);
        long fileSize = is.readLong();
        System.out.println("Received fileSize" + fileSize);
        try (FileOutputStream fos = new FileOutputStream(root + "/" + fileName)) {
            for (int i = 0; i < (fileSize + 255) / 256; i++) {
                int read = is.read(buffer);
                fos.write(buffer, 0, read);
            }
            fos.flush();
        }
        os.writeUTF("File " + fileName + " loaded");
    }
}