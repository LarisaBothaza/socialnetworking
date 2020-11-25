package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;
import socialnetwork.service.validators.ValidatorUtilizatorService;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class UtilizatorService  {
    private final Repository<Long, Utilizator> repoUtilizator;
    private final Repository<Tuple<Long,Long>, Prietenie> repoPrietenie;
    //private ValidatorUtilizatorService<Utilizator> validatorUtilizatorService;
    private final Validator<Utilizator> validatorUtilizatorService = new ValidatorUtilizatorService<>();

    public UtilizatorService(Repository<Long, Utilizator> repoUtilizator, Repository<Tuple<Long, Long>, Prietenie> repoPrietenie) {
        this.repoUtilizator = repoUtilizator;
        this.repoPrietenie = repoPrietenie;
    }

    /**
     *saves the user received parameter
     * @param utilizatorParam
     * @return the user created
     */
    public Utilizator addUtilizator(Utilizator utilizatorParam) {
        Utilizator utilizator = repoUtilizator.save(utilizatorParam);
        validatorUtilizatorService.validateAdd(utilizator);
        return utilizator;
    }

    /**
     *
     * @return an iterable list of all saved users
     */
    public Iterable<Utilizator> getAll(){
        return repoUtilizator.findAll();
    }

    ///TO DO: add other methods

    /**
     *delete the user identified by the received id, and also delete that user's friendships
     * @param id long - id's to be deleted
     * @return deleted user
     * @throws ConcurrentModificationException
     */
    public Utilizator deleteUtilizator(Long id) throws ConcurrentModificationException {
        Utilizator utilizator = repoUtilizator.delete(id);
        validatorUtilizatorService.validateDelete(utilizator);
        //sterg priteniile userului
        utilizator.getFriends().forEach((x)->{
            if(repoPrietenie.findOne(new Tuple<>(x.getId(),id)) != null){
                repoPrietenie.delete(new Tuple<>(x.getId(),id));
            }

            if(repoPrietenie.findOne(new Tuple<>(id,x.getId())) != null){
                repoPrietenie.delete(new Tuple<>(id,x.getId()));
            }
        });
        return utilizator;
    }
    public Utilizator findOne(Long id){
        return repoUtilizator.findOne(id);
    }

    public List<UserDTO> getAllUsersDTO(){
        List<UserDTO> userDTOList = new ArrayList<>();
        getAll().forEach(user->{
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName());
            userDTO.setId(user.getId());
            userDTOList.add(userDTO);
        });
        return userDTOList;
    }

    public UserDTO getUserDTO(Long userId){
        Utilizator user = findOne(userId);
        UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName());
        userDTO.setId(userId);
        return userDTO;
    }
}
