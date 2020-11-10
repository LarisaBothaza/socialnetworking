package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public interface Validator <T> {
    void validateAdd(T entity) throws ValidationException;
    void validateDelete(T entity) throws ValidationException;
}
