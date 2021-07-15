package model;

import lombok.Data;

import java.util.Date;

@Data

public class Message {

    private Date sendAt;
    private String content;

    public Message(String content) {
        this.content = content;
        sendAt = new Date();
    }
}
