package Secret.Santa.Secret.Santa.services.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGenerateSantaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateSantaUtils {

    @Autowired
    private final IGenerateSantaRepo generateSantaRepo;

    public GenerateSantaUtils(IGenerateSantaRepo generateSantaRepo) {

        this.generateSantaRepo = generateSantaRepo;
    }

    public GenerateSanta getGenerateSantaById(Integer id) {
        return generateSantaRepo.findById(id)
                .orElseThrow(() -> new SantaValidationException("Generate_Santa does not exist", "id",
                        "Generate_Santa not found", String.valueOf(id)));
    }

    public GenerateSanta getBySantaAndGroup(User user, Group group) {
        return generateSantaRepo.findBySantaAndGroup(user, group)
                .orElseThrow(() -> new SantaValidationException("Generate_Santa does not exist", "id",
                        "Generate_Santa not found", String.valueOf(user.getUserId())));
    }

    public boolean existsByGroup(Group group) {
        if (generateSantaRepo.existsByGroup(group)) {
            return true;
        }
        return false;
    }

    public GenerateSanta getByUserAndGroup(User user, Group group) {
        return generateSantaRepo.findByRecipientAndGroup(user, group)
                .orElseThrow(() -> new SantaValidationException("Generate_Santa does not exist", "id",
                        "Generate_Santa not found", String.valueOf(user.getUserId())));
    }

    public boolean alreadyPaired(User user1, User user2, Group group) {
        return generateSantaRepo.existsBySantaAndRecipientAndGroup(user1, user2, group) ||
                generateSantaRepo.existsBySantaAndRecipientAndGroup(user2, user1, group);
    }
}
