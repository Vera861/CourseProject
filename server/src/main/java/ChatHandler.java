import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatHandler<in> implements Runnable {

    private final Socket socket;
    private final byte[] buffer;
    private DataInputStream in;
    private DataOutputStream os;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            int read;
            while (true) {
                String strFromClient = in.readUTF();
                if (strFromClient.startsWith("/file")) {
                    String[] parts = strFromClient.split(" ");
                    String fileName = parts[1];
                    String fileSize = parts[2];
                    FileOutputStream os = new FileOutputStream(fileName);
                    while ((read = in.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                        os.flush();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Client connection exeption");
        } finally {
            try {
                in.close();
                os.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}