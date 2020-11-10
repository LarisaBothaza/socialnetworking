package socialnetwork.domain.validators;

import socialnetwork.domain.messages.ReplyMessage;

public class ReplyMessageValidator implements Validator<ReplyMessage> {
    @Override
    public void validate(ReplyMessage entity) throws ValidationException {
        String err="";
        if(entity.getMessage().matches("[ ]*")){
            err += "The message can't contain only blank spaces!\n";
        }

        if(err.length() > 0){
            throw new ValidationException(err);
        }
    }
}
