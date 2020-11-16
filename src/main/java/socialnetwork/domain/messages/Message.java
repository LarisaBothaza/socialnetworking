package socialnetwork.domain.messages;

import jdk.jfr.events.CertificateId;
import socialnetwork.domain.Entity;
import socialnetwork.domain.Utilizator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private static long idMaxMesaj=0;
    private static long idMaxReplyMesaj=0;
    private static long idMaxCerereMesaj=0;
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime data;

    public Message(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, String trash) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        idMaxCerereMesaj++;
        setId(idMaxCerereMesaj);
    }

    public Message(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, boolean trash) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        idMaxReplyMesaj++;
        setId(idMaxReplyMesaj);
    }

    public Message(Utilizator from, List<Utilizator> to, String message, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        idMaxMesaj++;
        setId(idMaxMesaj);
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) &&
                Objects.equals(to, message1.to) &&
                Objects.equals(message, message1.message) &&
                Objects.equals(data, message1.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, data);
    }
}
