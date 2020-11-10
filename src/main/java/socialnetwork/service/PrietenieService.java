package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.ValidatorPrietenieService;

public class PrietenieService {
    private Repository<Tuple<Long,Long>, Prietenie> repositoryPrietenie;
    private Repository<Long, Utilizator> repositoryUtilizator;
    private ValidatorPrietenieService<Prietenie> validatorPrietenieService = new ValidatorPrietenieService<>();

    public PrietenieService(Repository<Tuple<Long, Long>, Prietenie> repositoryPrietenie, Repository<Long, Utilizator> repositoryUtilizator) {
        this.repositoryPrietenie = repositoryPrietenie;
        this.repositoryUtilizator = repositoryUtilizator;
    }

    /**
     * add a new friendship, update users' friend lists
     * @param prietenieParam
     * @return friendship created
     * @throws ValidationException
     */
    public Prietenie addPrietenie(Prietenie prietenieParam) throws ValidationException {
        Prietenie prietenie = repositoryPrietenie.save(prietenieParam);
        validatorPrietenieService.validateAdd(prietenie);
        Utilizator userLeft = repositoryUtilizator.findOne(prietenieParam.getId().getLeft());
        Utilizator userRight = repositoryUtilizator.findOne(prietenieParam.getId().getRight());
        userLeft.getFriends().add(userRight);
        userRight.getFriends().add(userLeft);

        return prietenie;
    }

    /**
     * delete a friendship represented by the 2 received ids
     * @param ids
     * @return erased friendship
     * @throws ValidationException
     */
    public Prietenie deletePrietenie(Tuple<Long, Long> ids) throws ValidationException{
        Prietenie prietenie = repositoryPrietenie.delete(ids);
        validatorPrietenieService.validateDelete(prietenie);
        return prietenie;
    }

    /**
     *
     * @return an iterable list of all saved friendships
     */
    public Iterable<Prietenie> getAll(){
        return repositoryPrietenie.findAll();
    }
}
