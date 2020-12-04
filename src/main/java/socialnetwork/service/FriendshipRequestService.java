package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;
import socialnetwork.service.validators.ValidatorFriendshipRequestService;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.FriendshipRequestChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRequestService implements Observable<FriendshipRequestChangeEvent> {
    private Repository<Long, FriendshipRequest> requestRepository;
    private ValidatorFriendshipRequestService validatorFriendshipRequestService = new ValidatorFriendshipRequestService();
    private Repository<Tuple<Long,Long>, Prietenie> friendshipRepository;
    private List<Observer<FriendshipRequestChangeEvent>> observers = new ArrayList<>();

    public FriendshipRequestService(Repository<Long,
            FriendshipRequest> requestRepository,Repository<Tuple<Long,Long>, Prietenie> friendshipRepository) {
        this.requestRepository = requestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * add a friendship request
     * @param friendshipRequest
     * @return
     */
    public FriendshipRequest addRequest(FriendshipRequest friendshipRequest){
        validatorFriendshipRequestService.validateBeforeAdding(friendshipRequest,getAll(),friendshipRepository.findAll());
        FriendshipRequest friendshipRequest1 = requestRepository.save(friendshipRequest);

        //friendshipRequestValidator.validateAdd(friendshipRequest1);
        return friendshipRequest1;
    }

    /**
     * deleteed a friendship request
     * @param id
     * @return
     */
    public FriendshipRequest deleteRequest(Long id){
        FriendshipRequest friendshipRequest1 = requestRepository.delete(id);
        if(friendshipRequest1 != null){
            notifyObservers(new FriendshipRequestChangeEvent(ChangeEventType.DELETE,friendshipRequest1));
        }
        //validatorFriendshipRequestService.validateDelete(friendshipRequest1);
        return friendshipRequest1;
    }

    /**
     *
     * @param id
     * @return an iterable with all pending requests
     */
    public Iterable<FriendshipRequest> getAllPendingRequest (Long id){
        Iterable<FriendshipRequest> listRequest = requestRepository.findAll();
        List<FriendshipRequest> listPendingRequest = new ArrayList<>();
        listRequest.forEach(request->{
            if(request.getStatus().equals("pending") && request.getTo().get(0).getId().equals(id)){
                listPendingRequest.add(request);
            }
        });
        return listPendingRequest;
    }

    public List<FriendshipRequest> getAllRequest (Long id){
        Iterable<FriendshipRequest> listRequest = requestRepository.findAll();
        List<FriendshipRequest> listAllRequest = new ArrayList<>();
        listRequest.forEach(request->{
            if(request.getTo().get(0).getId().equals(id)){
                listAllRequest.add(request);
            }
        });
        return listAllRequest;
    }

    /**
     * searhc for one friendship request by id
     * @param id
     * @return
     */
    public FriendshipRequest getOne(Long id){
        return requestRepository.findOne(id);
    }

    public Iterable<FriendshipRequest> getAll(){
        return requestRepository.findAll();
    }

    public void updateFriendshipRequest(FriendshipRequest friendshipRequest, String status){
        FriendshipRequest fr = deleteRequest(friendshipRequest.getId());
        fr.setStatus(status);
        fr.setData(LocalDateTime.now());
        fr = addRequest(fr);

        notifyObservers(new FriendshipRequestChangeEvent(ChangeEventType.UPDATE,fr));


    }

    @Override
    public void addObserver(Observer<FriendshipRequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipRequestChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipRequestChangeEvent friendshipRequestChangeEvent) {
        observers.stream().forEach(obs -> obs.update(friendshipRequestChangeEvent));
    }

    public List<FriendshipRequest> getAllRequestTo(Long id) {
        Iterable<FriendshipRequest> listRequest = requestRepository.findAll();
        List<FriendshipRequest> listAllRequest = new ArrayList<>();


        listRequest.forEach(request->{
            if(request.getFrom().getId().equals(id) && request.getStatus().equals("pending")){
                listAllRequest.add(request);
            }
        });
        return listAllRequest;
    }
}
