package socialnetwork.repository.file;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.Message;
import socialnetwork.domain.validators.Validator;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.spi.LocaleNameProvider;

public class MessageFile extends AbstractFileRepository<Long, Message> {

    public MessageFile(String fileName, Validator<Message> validator) {
        super(fileName, validator);
    }

    @Override
    public Message extractEntity(List<String> attributes) {
        Message message = new Message(new Utilizator("Dummy","Dummy"), new ArrayList<>(),"Dummy", LocalDateTime.now());
        //message.setId(Long.parseLong(attributes.get(0)));
        return message;
    }

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
