package Secret.Santa.Secret.Santa.mappers;

import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import org.springframework.stereotype.Component;

@Component
public class GiftMapper {
    private static GroupUtils groupUtils;
    private static UserUtils userUtils;

    public GiftMapper(GroupUtils groupUtils, UserUtils userUtils) {
        this.groupUtils = groupUtils;
        this.userUtils = userUtils;
    }

    public Gift toGift(GiftDTO giftDTO) {

        Gift gift = new Gift();
        if (giftDTO.getGiftId() != null) {
            gift.setGiftId(giftDTO.getGiftId());
        }
        gift.setName(giftDTO.getName());
        gift.setDescription(giftDTO.getDescription());
        gift.setLink(giftDTO.getLink());
        gift.setPrice(giftDTO.getPrice());
        User user = userUtils.getUserById(giftDTO.getCreatedBy());
        gift.setCreatedBy(user);
        Group group = groupUtils.getGroupById(giftDTO.getGroupId());
        gift.setGroup(group);

        return gift;
    }


    public GiftDTO toGiftDTO(Gift gift) {

        GiftDTO giftDTO = new GiftDTO();
        giftDTO.setGiftId(gift.getGiftId());
        giftDTO.setName(gift.getName());
        giftDTO.setDescription(gift.getDescription());
        giftDTO.setLink(gift.getLink());
        giftDTO.setPrice(gift.getPrice());
        giftDTO.setCreatedBy(gift.getCreatedBy().getUserId());
        giftDTO.setGroupId(gift.getGroup().getGroupId());

        return giftDTO;
    }


}
