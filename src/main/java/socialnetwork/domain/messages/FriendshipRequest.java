package socialnetwork.domain.messages;

import socialnetwork.domain.Utilizator;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipRequest extends Message{
    private String status;

    public FriendshipRequest(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, String status) {
        super(from, to, message, data, "trash");
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        String messageString = "FriendshipRequest{"
                + "Id Friendship Request: " + super.getId() + " "
                + "From: " + super.getFrom().getFirstName()+" "+ super.getFrom().getLastName() + " "
                + "To: " + super.getTo().get(0).getFirstName() + " " + super.getTo().get(0).getLastName() + " "
                + "Text message: " + super.getMessage() + " "
                + "Data: " + super.getData().format(Constants.DATE_TIME_FORMATTER) + " "
                + "Status: " + status + '}';

        return messageString;
    }
}
