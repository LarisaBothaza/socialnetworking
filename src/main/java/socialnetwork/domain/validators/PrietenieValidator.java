package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

import java.util.concurrent.atomic.AtomicBoolean;

public class PrietenieValidator implements Validator<Prietenie> {
    private Repository<Long,Utilizator> userRepository;

    public PrietenieValidator(Repository<Long, Utilizator> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * throw exception if the ids are the same or an id does not exist
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        String err="";


        if(entity.getId().getLeft().equals(entity.getId().getRight())){
            err+="The ids are the same!\n";
        }

        Iterable<Utilizator> listUtilizatori = this.userRepository.findAll();
        AtomicBoolean leftIdExists = new AtomicBoolean(false);
        AtomicBoolean rightIdExists = new AtomicBoolean(false);
        listUtilizatori.forEach(user ->{
            if(user.getId().equals(entity.getId().getLeft()))
                leftIdExists.set(true);

            if(user.getId().equals(entity.getId().getRight()))
                rightIdExists.set(true);
        });

        if(!leftIdExists.get()){
            err+="The left id is not valid - doesn't exist!\n";
        }

        if(!rightIdExists.get()){
            err+="The right id is not valid - doesn't exist!\n";
        }

        if(err.length() > 0){
            throw new ValidationException(err);
        }

    }
}
