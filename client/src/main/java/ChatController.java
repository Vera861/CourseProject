import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Message;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class ChatController implements Initializable {
    private final String root = "client/clientFiles";
    public TextField statusBar;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;
    private byte[] buffer;
    private NettyNetwork network;
    public ListView<String> listView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buffer = new byte[256];
        try {
            File dir = new File(root);
            listView.getItems().clear();
            listView.getItems().addAll(dir.list());

            Socket socket = new Socket("localhost", 8189);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());

            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String status = is.readUTF();
                        Platform.runLater(() -> statusBar.setText(status)
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(javafx.event.ActionEvent actionEvent) throws IOException {
        String content = statusBar.getText();
        network.writeMessage(new Message(content));
    }
}
