package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<Tuple<Long,Long>, Prietenie> {
    private Repository<Long, Utilizator> utilizatorRepository;

    public PrietenieFile(String fileName, Validator<Prietenie> validator, Repository<Long, Utilizator> utilizatorRepository) {
        super(fileName, validator);
        this.utilizatorRepository = utilizatorRepository;
        createListPrieteni();
    }

    /**
     * extract the attributes of a friendship from a file line
     * @param attributes
     * @return a friendship
     */
    @Override
    public Prietenie extractEntity(List<String> attributes) {
       Prietenie prietenie = new Prietenie(LocalDate.parse(attributes.get(0)));
       Long leftId = Long.parseLong(attributes.get(1));
       Long rightId = Long.parseLong(attributes.get(2));
       prietenie.setId(new Tuple<Long, Long>(leftId,rightId));
       return prietenie;

    }

    /**
     * creates a string from the attributes of a friendship
     * @param entity
     * @return a string
     */
    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getDate()+";"+entity.getId().getLeft() + ";" + entity.getId().getRight();
    }

    /**
     * updates friend lists for users
     */
    private void createListPrieteni(){
        entities.forEach((ids,element)->{
            Utilizator userLeft = utilizatorRepository.findOne(ids.getLeft());
            Utilizator userRight = utilizatorRepository.findOne(ids.getRight());
            userLeft.getFriends().add(userRight);
            userRight.getFriends().add(userLeft);
        });
    }
}
