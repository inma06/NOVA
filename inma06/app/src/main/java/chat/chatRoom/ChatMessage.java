package chat.chatRoom;

public class ChatMessage {
    private boolean isImage, isMine;
    private String contentMessage;
    private String contentImage;

    public ChatMessage(String message, String image, boolean isMine, boolean isImage) {
        this.contentMessage = message;
        this.contentImage = image;
        this.isMine = isMine;
        this.isImage = isImage;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String image) {
        this.contentImage = image;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String message) {
        this.contentMessage = message;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }
}
