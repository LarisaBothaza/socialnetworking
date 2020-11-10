package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public class ValidatorUtilizatorService<T> implements Validator<T> {
    /**
     * validates the addition
     * @param entity
     * @throws ValidationException - if the entity is not null
     */
    @Override
    public void validateAdd(T entity) throws ValidationException {
        if(entity != null ){
            throw new ValidationException("The user already exist!\n");
        }
    }

    /**
     *validates the deletion
     * @param entity
     * @throws ValidationException - if the entity is null
     */
    @Override
    public void validateDelete(T entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("The user to be deleted doesn't exist!\n");
        }
    }
}
