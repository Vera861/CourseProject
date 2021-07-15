import lombok.Data;

import java.util.Date;

@Data

public class Message {

    Date sendAt;
    String content;

    public Message(String content) {
        this.content = content;
        sendAt = new Date();
    }
}
