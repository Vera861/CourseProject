import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Message;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ChatController implements Initializable {

    private final String root = "client/clientFiles";
    public TextField statusBar;
    @FXML
    private TableView<FileInfo> tableFile;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream is;
    private byte[] buffer;
    private NettyNetwork network;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfo, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        nameCol.setPrefWidth(240);

        TableColumn<FileInfo, Long> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        sizeCol.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        sizeCol.setPrefWidth(120);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        dateCol.setPrefWidth(120);

        tableFile.getColumns().addAll(nameCol, sizeCol, dateCol);
        tableFile.getSortOrder().add(nameCol);
        updateList(Paths.get(root));

        buffer = new byte[256];
        try {
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

    public void updateList(Path path) {
        try {
            tableFile.getItems().clear();
            tableFile.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            tableFile.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to load file list", ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void send(javafx.event.ActionEvent actionEvent) {
        String content = statusBar.getText();
        network.writeMessage(new Message(content));
    }

}