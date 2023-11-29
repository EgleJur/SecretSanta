package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.services.IGiftService;

import java.util.List;
import java.util.Optional;

public class GiftServiceImpl implements IGiftService {

    private final IGiftRepo giftRepo;

    public GiftServiceImpl(IGiftRepo giftRepo) {
        this.giftRepo = giftRepo;
    }

    @Override
    public List<Gift> getAllGifts() {
        return giftRepo.findAll();
    }

    @Override
    public Gift getGiftById(int giftId) {
        Optional<Gift> optionalGift = giftRepo.findById(giftId);
        return optionalGift.orElse(null);    }

    @Override
    public Gift createGift(Gift gift) {
        return giftRepo.save(gift);
    }

    @Override
    public Gift updateGift(int giftId, Gift updatedGift) {
        Gift existingGift = getGiftById(giftId);

        if (existingGift != null) {
            existingGift.setName(updatedGift.getName());
            existingGift.setDescription(updatedGift.getDescription());
            existingGift.setLink(updatedGift.getLink());
            existingGift.setPrice(updatedGift.getPrice());
            existingGift.setGroup(updatedGift.getGroup());

            return giftRepo.save(existingGift);
        }
        return null;
    }

    @Override
    public void deleteGift(int giftId) {
        giftRepo.deleteById(giftId);
    }
}
