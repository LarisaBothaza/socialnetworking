package socialnetwork.repository.file;

import socialnetwork.domain.Utilizator;
import socialnetwork.domain.messages.ReplyMessage;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ReplyMessageFile extends AbstractFileRepository<Long, ReplyMessage>{

    public ReplyMessageFile(String fileName, Validator<ReplyMessage> validator,
                            Repository<Long, Utilizator> utilizatorRepository ) {

        super(fileName, validator,utilizatorRepository);

    }

    @Override
    public ReplyMessage extractEntity(List<String> attributes) {
        Long idUserEmitator = Long.parseLong(attributes.get(1));
        Long idUserReceptor = Long.parseLong(attributes.get(2));
        String textMesaj = attributes.get(3);
        LocalDateTime data = LocalDateTime.parse(attributes.get(4),Constants.DATE_TIME_FORMATTER);
        Long idReplyMessage = Long.parseLong(attributes.get(5));
        return new ReplyMessage(userRepository.findOne(idUserEmitator),
                Arrays.asList(userRepository.findOne(idUserReceptor)),textMesaj,data,findOne(idReplyMessage));
    }

    @Override
    protected String createEntityAsString(ReplyMessage entity) {
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
                + entity.getData().format(Constants.DATE_TIME_FORMATTER)+ ";" ;
        if(entity.getMessageReply() == null)
            messageAttributes += "0";
            else
            messageAttributes += entity.getMessageReply().getId();
        return messageAttributes;
    }
}
