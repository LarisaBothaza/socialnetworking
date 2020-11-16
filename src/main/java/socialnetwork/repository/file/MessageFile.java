package socialnetwork.repository.file;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.Message;
import socialnetwork.domain.messages.ReplyMessage;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.spi.LocaleNameProvider;

public class MessageFile extends AbstractFileRepository<Long, Message> {

    public MessageFile(String fileName, Validator<Message> validator,  Repository<Long, Utilizator> utilizatorRepository) {

        super(fileName, validator,utilizatorRepository);
    }

    /**
     * extract the attributes of a  message from a file line
     * @param attributes
     * @return message
     */
    @Override
    public Message extractEntity(List<String> attributes) {
//        Message message = new Message(new Utilizator("Dummy","Dummy"), new ArrayList<>(),"Dummy", LocalDateTime.now());
//        message.setId(Long.parseLong(attributes.get(0)));
//        return message;

        //14;3;4,5,6;FInal;2020-11-11 18:52:03

        Long idUserEmitator = Long.parseLong(attributes.get(1));

        String list = attributes.get(2);
        String[] parts = list.split(",");
        List<Utilizator> idsUsersReceptors = new ArrayList<>();
        for(String p : parts){
            Long id = Long.parseLong(p);
            Utilizator user = userRepository.findOne(id);
            idsUsersReceptors.add(user);
        }

        String textMesaj = attributes.get(3);
        LocalDateTime data = LocalDateTime.parse(attributes.get(4),Constants.DATE_TIME_FORMATTER);

        return new Message(userRepository.findOne(idUserEmitator),idsUsersReceptors,textMesaj, data);
    }
    /**
     * create a line for file from the attributes of a  message
     * @param entity
     * @return string
     */
    @Override
    protected String createEntityAsString(Message entity) {
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
                            + entity.getData().format(Constants.DATE_TIME_FORMATTER);
        return messageAttributes;
    }
}
