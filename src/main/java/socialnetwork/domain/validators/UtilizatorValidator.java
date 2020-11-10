package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    /**
     * throw an exception if the first or last name is invalid
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        String err = "";
        if(entity.getFirstName().matches(".*\\d.*") || entity.getFirstName().length()<3 || entity.getFirstName().equals("")){
            err+="First Name invalid!\n";
        }

        if(entity.getLastName().equals("") || entity.getLastName().length()<3 || entity.getLastName().matches(".*\\d.*")){
            err+="Last Name invalid!\n";
        }

        if(err.length() > 0)
            throw new ValidationException(err);
    }
}
