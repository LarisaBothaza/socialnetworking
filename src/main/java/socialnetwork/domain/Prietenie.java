package socialnetwork.domain;

import socialnetwork.utils.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDate date;

    public Prietenie(LocalDate date) {
        this.date = date; //used when we load the friendship from the friendship file
    }

    public Prietenie(Tuple<Long,Long> ids){
        setId(ids);
        this.date = LocalDate.now(); //used when we add a new friendship at run-time
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {

        return date;
    }

    /**
     *
     * @return a string with all the atributes of a friendship
     */
    @Override
    public String toString() {
        return "Prietenie {" +
                "date=" + date + ", " +
                "idLeft= " + super.getId().getLeft() + ", " +
                "idRight= " + super.getId().getRight() +
                '}';
    }
}
