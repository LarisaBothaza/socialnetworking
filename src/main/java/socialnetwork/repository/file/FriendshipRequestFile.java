package socialnetwork.repository.file;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.FriendshipRequest;
import socialnetwork.domain.messages.ReplyMessage;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FriendshipRequestFile extends AbstractFileRepository<Long, FriendshipRequest> {

    public FriendshipRequestFile(String fileName, Validator<FriendshipRequest> validator, Repository<Long, Utilizator> utilizatorRepository) {
        super(fileName, validator,utilizatorRepository);
    }
    /**
     * extract the attributes of a FriendshipRequest from a file line
     * @param attributes
     * @return FriendshipRequest
     */
    @Override
    public FriendshipRequest extractEntity(List<String> attributes) {
        Long idUserEmitator = Long.parseLong(attributes.get(1));
        Long idUserReceptor = Long.parseLong(attributes.get(2));
        String textMesaj = attributes.get(3);
        LocalDateTime data = LocalDateTime.parse(attributes.get(4), Constants.DATE_TIME_FORMATTER);
        String status = attributes.get(5);
        return new  FriendshipRequest(userRepository.findOne(idUserEmitator),
                Arrays.asList(userRepository.findOne(idUserReceptor)),textMesaj,data,status);
    }

    /**
     * create a line for file from the attributes of a FriendshipRequest
     * @param entity
     * @return string
     */
    @Override
    protected String createEntityAsString(FriendshipRequest entity) {
        String listTo = "";
        List<Utilizator> list = entity.getTo();
        for(Utilizator user : list){
            listTo += user.getId()+",";
        }
        if(listTo.length() >= 1)
            listTo = listTo.substring(0,listTo.length()-1);

        String messageAttributes = "";
        messageAttributes += entity.getId() + ";"
                + entity.getFrom().getId() + ";"
                + listTo + ";"
                + entity.getMessage() + ";"
                + entity.getData().format(Constants.DATE_TIME_FORMATTER)+ ";"
                + entity.getStatus();

        return messageAttributes;
    }
}
