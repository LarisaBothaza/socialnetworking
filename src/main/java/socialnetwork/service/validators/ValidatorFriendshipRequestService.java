package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public class ValidatorFriendshipRequestService<T> implements Validator<T> {

    /**
     * validate the addition
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validateAdd(T entity) throws ValidationException {
        if(entity != null){
            throw new ValidationException("The friendship request already exist!\n");
        }
    }

    /**
     * validate the deletion
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validateDelete(T entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("The friendship request doesn't exist!\n");
        }
    }
}
