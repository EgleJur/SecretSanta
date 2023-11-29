package Secret.Santa.Secret.Santa.services;

import Secret.Santa.Secret.Santa.models.Gift;

import java.util.List;

public interface IGiftService {

    List<Gift> getAllGifts();

    Gift getGiftById(int giftId);

    Gift createGift(Gift gift);

    Gift updateGift(int giftId, Gift gift);

    void deleteGift(int giftId);

}
