package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public class ValidatorFriendshipRequestService<T> implements Validator<T> {

    @Override
    public void validateAdd(T entity) throws ValidationException {
        if(entity != null){
            throw new ValidationException("The friendship request already exist!\n");
        }
    }

    @Override
    public void validateDelete(T entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("The friendship request doesn't exist!\n");
        }
    }
}
