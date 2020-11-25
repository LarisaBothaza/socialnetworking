package socialnetwork.utils.events;

import socialnetwork.domain.Prietenie;

public class FriendshipChangeEvent implements Event {
    private ChangeEventType eventType;
    private Prietenie oldFriendship, newFriendship;

    public FriendshipChangeEvent(ChangeEventType eventType, Prietenie oldFriendship, Prietenie newFriendship) {
        this.eventType = eventType;
        this.oldFriendship = oldFriendship;
        this.newFriendship = newFriendship;
    }

    public FriendshipChangeEvent(ChangeEventType eventType, Prietenie oldFriendship) {
        this.eventType = eventType;
        this.oldFriendship = oldFriendship;
    }

    public ChangeEventType getEventType() {
        return eventType;
    }

    public Prietenie getOldFriendship() {
        return oldFriendship;
    }

    public Prietenie getNewFriendship() {
        return newFriendship;
    }
}
