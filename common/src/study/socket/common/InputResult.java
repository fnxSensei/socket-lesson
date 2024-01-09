package study.socket.common;

import java.io.Serializable;

public class InputResult implements Serializable {
    private Message message;
    private CreateFile file;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public CreateFile getFile() {
        return file;
    }

    public void setFile(CreateFile file) {
        this.file = file;
    }
}
