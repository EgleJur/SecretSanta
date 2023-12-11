package Secret.Santa.Secret.Santa.services.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftUtils {

    @Autowired
    private final IGiftRepo giftRepository;
    private final UserUtils userUtils;


    public Gift getGiftById(Integer id) {
        return giftRepository.findById(id)
                .orElseThrow(() -> new SantaValidationException("Gift does not exist", "id",
                        "Gift not found", String.valueOf(id)));
    }

    public boolean giftBelongsToUser(int userId, int giftId) {
        Gift gift = getGiftById(giftId);
        User user = userUtils.getUserById(userId);
        return gift.getCreatedBy().equals(user);
    }
}
