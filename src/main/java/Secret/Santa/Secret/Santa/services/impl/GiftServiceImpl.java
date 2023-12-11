package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.mappers.GiftMapper;
import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.services.IGiftService;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftServiceImpl implements IGiftService {
    private static final Logger logger = LoggerFactory.getLogger(GiftServiceImpl.class);
    private final GiftMapper giftMapper;
    @Autowired
    private final IGiftRepo iGiftRepo;

    private final GroupUtils groupUtils;
    private final UserUtils userUtils;

    @Override
    public List<GiftDTO> getAllGifts() {
        try {
            List<Gift> users = iGiftRepo.findAll();

            return users.stream()
                    .map(giftMapper::toGiftDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all gifts", e);
            throw e;
        }
    }

    @Override
    public GiftDTO getGiftById(int giftId) {
        try {

            Gift gift = iGiftRepo.findById(giftId)
                    .orElseThrow(() -> new RuntimeException("Gift not found with id: " + giftId));
            return giftMapper.toGiftDTO(gift);
        } catch (Exception e) {
            logger.error("Failed to retrieve gift with ID: {}", giftId, e);
            throw e;
        }
    }

    @Override
    public GiftDTO createGift(GiftDTO giftDTO) {
        try {
            if (giftDTO.getPrice() < 0) {
                throw new SantaValidationException("Price cannot be negative", "price", "NegativeValue", String.valueOf(giftDTO.getPrice()));
            }
            if (giftDTO == null) {
                throw new IllegalArgumentException("GiftDTO cannot be null");
            }
            Group group = groupUtils.getGroupById(giftDTO.getGroupId());
            if (giftDTO.getPrice() > group.getBudget()) {
                throw new SantaValidationException("Price cannot be bigger than group budget", "price", "BiggerThanBudget", String.valueOf(giftDTO.getPrice()));
            }

            Gift gift = giftMapper.toGift(giftDTO);
            Gift savedGift = iGiftRepo.save(gift);
            return giftMapper.toGiftDTO(savedGift);
        } catch (Exception e) {
            logger.error("Error creating gift", e);
            throw e;
        }
    }

    @Override
    public GiftDTO updateGift(GiftDTO giftDTO) {
        if (giftDTO == null) {
            throw new IllegalArgumentException("GiftDTO cannot be null");
        }
        Group group = groupUtils.getGroupById(giftDTO.getGroupId());
        if (giftDTO.getPrice() > group.getBudget()) {
            throw new SantaValidationException("Price cannot be bigger than group budget", "price", "BiggerThanBudget", String.valueOf(giftDTO.getPrice()));
        }

        if (giftDTO == null) {
            throw new IllegalArgumentException("GiftDTO cannot be null");
        }
        if (giftDTO.getGiftId() == null) {
            throw new IllegalArgumentException("This gift does not have ID");
        }
        Optional<Gift> existingGift = iGiftRepo.findById(giftDTO.getGiftId());
        if (existingGift.isPresent()) {
            Gift gift = giftMapper.toGift(giftDTO);
            Gift updatedGift = iGiftRepo.save(gift);
            return giftMapper.toGiftDTO(updatedGift);
        }
        throw new EntityNotFoundException("User not found with id " + giftDTO.getGiftId());
    }


    @Transactional
    @Override
    public boolean deleteGift(int giftId) {
        Optional<Gift> optionalGift = iGiftRepo.findById(giftId);
        if (optionalGift.isPresent()) {
            try {
                iGiftRepo.deleteById(giftId);
                return true;
            } catch (Exception exception) {
                logger.error("Exception occurred while deleting gift with ID: {}", giftId, exception);
                return false;
            }
        } else {
            logger.error("Attempted to delete a gift that does not exist with ID: {}", giftId);
            throw new EntityNotFoundException("Gift not found with id " + giftId);
        }
    }

    @Override
    public List<Gift> getGiftsCreatedBy(int userId) {
        try {
            User user = userUtils.getUserById(userId);
            return iGiftRepo.findByCreatedBy(user);
        } catch (Exception e) {
            logger.error("Error retrieving gifts created by user with ID: {}", userId, e);
            throw e;
        }
    }
}
