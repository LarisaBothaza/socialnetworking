package socialnetwork.service;

import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;
import socialnetwork.service.validators.ValidatorFriendshipRequestService;

import java.util.ArrayList;
import java.util.List;

public class FriendshipRequestService {
    private Repository<Long, FriendshipRequest> requestRepository;
    private Validator<FriendshipRequest> friendshipRequestValidator = new ValidatorFriendshipRequestService<>();

    public FriendshipRequestService(Repository<Long, FriendshipRequest> requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * add a friendship request
     * @param friendshipRequest
     * @return
     */
    public FriendshipRequest addRequest(FriendshipRequest friendshipRequest){
        FriendshipRequest friendshipRequest1 = requestRepository.save(friendshipRequest);
        friendshipRequestValidator.validateAdd(friendshipRequest1);
        return friendshipRequest1;
    }

    /**
     * deleteed a friendship request
     * @param id
     * @return
     */
    public FriendshipRequest deleteRequest(Long id){
        FriendshipRequest friendshipRequest1 = requestRepository.delete(id);
        friendshipRequestValidator.validateDelete(friendshipRequest1);
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

    /**
     * searhc for one friendship request by id
     * @param id
     * @return
     */
    public FriendshipRequest getOne(Long id){
        return requestRepository.findOne(id);
    }

}
