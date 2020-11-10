package socialnetwork.domain.messages;

import socialnetwork.domain.Utilizator;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Message messageReply;


    public ReplyMessage(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Message messageReply) {
        super(from, to, message, data,true);
        this.messageReply = messageReply;
    }

    public Message getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(Message messageReply) {
        this.messageReply = messageReply;
    }

    @Override
    public String toString() {
        String messageString = "ReplyMessage{"
                + "From: " + super.getFrom().getFirstName()+" "+ super.getFrom().getLastName() + " "
                + "To: " + super.getTo().get(0).getFirstName() + " " + super.getTo().get(0).getLastName() + " "
                + "Text message: " + super.getMessage() + " "
                + "Data: " + super.getData().format(Constants.DATE_TIME_FORMATTER);
        if(getMessageReply() != null)
            messageString += " Reply to: " + getMessageReply().getMessage() + '}';

        return messageString;
    }
}
