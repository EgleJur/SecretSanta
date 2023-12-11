package Secret.Santa.Secret.Santa.services.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;

public class ValidationUtilsNotNull {

    public static void isValidById(Long id) {
        if (id == null || id.equals("")) {
            throw new SantaValidationException("Id cannot be empty", "id",
                    "Id is empty", String.valueOf(id));
        }
    }

    public static void isValidByName(String name) {
        if (name == null || name.isEmpty() || name.equals("")) {
            throw new SantaValidationException("Name cannot be empty", "name",
                    "Name is empty", name);
        }
    }


}

