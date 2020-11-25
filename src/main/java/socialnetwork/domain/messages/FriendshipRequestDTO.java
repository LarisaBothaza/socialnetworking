package socialnetwork.domain.messages;

import socialnetwork.domain.Utilizator;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipRequestDTO extends Message {

    public FriendshipRequestDTO(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, String trash) {
        super(from, to, message, data, trash);
    }
}
