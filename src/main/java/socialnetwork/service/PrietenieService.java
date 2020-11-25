package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.ValidatorPrietenieService;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.Event;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class PrietenieService implements Observable<FriendshipChangeEvent> {
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
        if(prietenie == null){
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD,prietenie));
        }

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
        if(prietenie != null){
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE,prietenie));
        }
        return prietenie;
    }

    /**
     *
     * @return an iterable list of all saved friendships
     */
    public Iterable<Prietenie> getAll(){
        return repositoryPrietenie.findAll();
    }

    public Iterable<Prietenie> getAllFriendshipsUser(Long idUser){
        Iterable<Prietenie> allFriendships = this.getAll();
        List<Prietenie> listFirendshipsUser = new ArrayList<>();
        allFriendships.forEach(fr ->{
            if(fr.getId().getLeft().equals(idUser) || fr.getId().getRight().equals(idUser))
                listFirendshipsUser.add(fr);
        });
        return listFirendshipsUser;
    }

    public Iterable<Prietenie> getAllNonFriendshipsUser(Long idUser){
        Iterable<Prietenie> allFriendships = this.getAll();
        List<Prietenie> listNonFirendshipsUser = new ArrayList<>();

        allFriendships.forEach(fr ->{
            if(!fr.getId().getLeft().equals(idUser) && !fr.getId().getRight().equals(idUser))
                listNonFirendshipsUser.add(fr);
        });

        return listNonFirendshipsUser;
    }

    public Prietenie getOne(Long idLeft, Long idRight){
        return repositoryPrietenie.findOne(new Tuple<>(idLeft,idRight));
    }

    private List<Observer<FriendshipChangeEvent>> observers=new ArrayList<>();
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent friendshipChangeEvent) {
        observers.stream().forEach(obs -> obs.update(friendshipChangeEvent));
    }
}
