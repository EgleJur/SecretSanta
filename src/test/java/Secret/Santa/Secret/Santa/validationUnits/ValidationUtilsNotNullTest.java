package Secret.Santa.Secret.Santa.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.services.validationUnits.ValidationUtilsNotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


class ValidationUtilsNotNullTest {

    @Test
    void isValidById_ValidId() {
        Long id = 123L;
        ValidationUtilsNotNull.isValidById(id);
    }

    @Test
    void isValidById_NullId() {
        Long id = null;
        assertThrows(SantaValidationException.class, () -> ValidationUtilsNotNull.isValidById(id));
    }

    @Test
    void isValidById_EmptyId() {
        Long id = null; // Or any value representing an empty ID
        assertThrows(SantaValidationException.class, () -> ValidationUtilsNotNull.isValidById(id));
    }

    @Test
    void isValidByName_ValidName() {
        String name = "John Doe";
        ValidationUtilsNotNull.isValidByName(name);
    }

    @Test
    void isValidByName_NullName() {
        String name = null;
        assertThrows(SantaValidationException.class, () -> ValidationUtilsNotNull.isValidByName(name));
    }

    @Test
    void isValidByName_EmptyName() {
        String name = "";
        assertThrows(SantaValidationException.class, () -> ValidationUtilsNotNull.isValidByName(name));
    }

}
