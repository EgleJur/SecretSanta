package Secret.Santa.Secret.Santa.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGenerateSantaRepo;
import Secret.Santa.Secret.Santa.services.validationUnits.GenerateSantaUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateSantaUtilsTest {

    @Mock
    private IGenerateSantaRepo generateSantaRepo;

    @InjectMocks
    private GenerateSantaUtils generateSantaUtils;

    @Test
    void getGenerateSantaById_ValidId_ReturnsGenerateSanta() {
        int generateSantaId = 1;
        GenerateSanta expectedGenerateSanta = new GenerateSanta();
        when(generateSantaRepo.findById(generateSantaId)).thenReturn(Optional.of(expectedGenerateSanta));

        GenerateSanta result = generateSantaUtils.getGenerateSantaById(generateSantaId);

        assertSame(expectedGenerateSanta, result);
    }

    @Test
    void getGenerateSantaById_InvalidId_ThrowsException() {
        int generateSantaId = 1;
        when(generateSantaRepo.findById(generateSantaId)).thenReturn(Optional.empty());

        assertThrows(SantaValidationException.class, () -> generateSantaUtils.getGenerateSantaById(generateSantaId));
    }

    @Test
    void getBySantaAndGroup_ValidData_ReturnsGenerateSanta() {
        User user = new User();
        Group group = new Group();
        GenerateSanta expectedGenerateSanta = new GenerateSanta();
        when(generateSantaRepo.findBySantaAndGroup(user, group)).thenReturn(Optional.of(expectedGenerateSanta));

        GenerateSanta result = generateSantaUtils.getBySantaAndGroup(user, group);

        assertSame(expectedGenerateSanta, result);
    }

    @Test
    void getBySantaAndGroup_InvalidId_ThrowsException() {
        User user = new User();
        Group group = new Group();
        when(generateSantaRepo.findBySantaAndGroup(user, group)).thenReturn(Optional.empty());

        assertThrows(SantaValidationException.class, () -> generateSantaUtils.getBySantaAndGroup(user, group));
    }

    @Test
    void getByUserAndGroup_ValidData_ReturnsGenerateSanta() {
        User user = new User();
        Group group = new Group();
        GenerateSanta expectedGenerateSanta = new GenerateSanta();
        when(generateSantaRepo.findByRecipientAndGroup(user, group)).thenReturn(Optional.of(expectedGenerateSanta));

        GenerateSanta result = generateSantaUtils.getByUserAndGroup(user, group);

        assertSame(expectedGenerateSanta, result);
    }

    @Test
    void getByUserAndGroup_InvalidId_ThrowsException() {
        User user = new User();
        Group group = new Group();
        when(generateSantaRepo.findByRecipientAndGroup(user, group)).thenReturn(Optional.empty());

        assertThrows(SantaValidationException.class, () -> generateSantaUtils.getByUserAndGroup(user, group));
    }

    @Test
    void alreadyPaired_PairedUsers_ReturnsTrue() {
        User user1 = new User();
        User user2 = new User();
        when(generateSantaRepo.existsBySantaAndRecipient(user1, user2)).thenReturn(true);

        boolean result = generateSantaUtils.alreadyPaired(user1, user2);

        assert result;
    }

    @Test
    void alreadyPaired_NotPairedUsers_ReturnsFalse() {
        User user1 = new User();
        User user2 = new User();
        when(generateSantaRepo.existsBySantaAndRecipient(user1, user2)).thenReturn(false);

        boolean result = generateSantaUtils.alreadyPaired(user1, user2);

        assertFalse(result);
    }
}