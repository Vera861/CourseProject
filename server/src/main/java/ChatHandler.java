import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatHandler implements Runnable {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private byte[] buffer;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            while (true) {
                int read = is.read(buffer);
                System.out.println("Received: " + new String(buffer, 0, read));
                os.write(buffer, 0 , read);
                os.flush();
            }
        }catch (Exception e) {
            System.err.println("Client connection exeption");
        }finally {
            try {
                is.close();
                os.close();
            } catch (IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
}
