package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.services.IGiftService;
import Secret.Santa.Secret.Santa.services.validationUnits.GiftUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gifts")
@Validated
@RequiredArgsConstructor
public class GiftController {
    private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
    @Autowired
    private final IGiftService giftService;

    private final UserUtils userUtils;
    private final GiftUtils giftUtils;

    @GetMapping
    public ResponseEntity<List<GiftDTO>> getAllGifts() {
        try {
            List<GiftDTO> gifts = giftService.getAllGifts();
            return new ResponseEntity<>(gifts, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving all gifts", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/users/{userId}/gifts/{giftId}")
    public ResponseEntity<GiftDTO> getGiftById(@Valid
                                               @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                               @PathVariable int userId, @PathVariable int giftId, Principal principal) {
        String authenticatedEmail = principal.getName();

        giftUtils.giftBelongsToUser(userId, giftId);
        try {
            if (userUtils.getUserById(userId).getEmail().equals(authenticatedEmail)) {
                GiftDTO giftDTO = giftService.getGiftById(giftId);
                return ResponseEntity.ok(giftDTO);
            } else {
                throw new AccessDeniedException("Authenticated user does not have access to this user's group");
            }
        } catch (Exception e) {
            logger.error("Error retrieving gift with ID: {}", giftId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<GiftDTO> createGift(@RequestBody @Valid GiftDTO giftDTO) {
        try {
            GiftDTO createdGiftDTO = giftService.createGift(giftDTO);
            return ResponseEntity.ok(createdGiftDTO);
        } catch (Exception e) {
            logger.error("Error creating gift", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping
    public ResponseEntity<GiftDTO> updateGift(@RequestBody GiftDTO giftDTO, Principal principal) {
        String authenticatedEmail = principal.getName();
        try {
            if (userUtils.getUserById(giftDTO.getCreatedBy()).getEmail().equals(authenticatedEmail)) {
                GiftDTO updatedGiftDTO = giftService.updateGift(giftDTO);
                return new ResponseEntity<>(updatedGiftDTO, HttpStatus.OK);
            } else {
                throw new AccessDeniedException("Authenticated user does not have access to this user's gifts");
            }
        } catch (Exception e) {
            logger.error("Error updating gift with ID: {}", giftDTO.getGiftId(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{giftId}")
    public ResponseEntity<Void> deleteGift(@PathVariable int giftId) {
        try {
            giftService.deleteGift(giftId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error deleting gift with ID: {}", giftId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/createdBy/{userId}")
    public ResponseEntity<List<Gift>> getGiftsCreatedByUser(@PathVariable int userId, Principal principal) {
        String authenticatedEmail = principal.getName();
        try {
            if (userUtils.getUserById(userId).getEmail().equals(authenticatedEmail)) {
                List<Gift> userGifts = giftService.getGiftsCreatedBy(userId);
                return ResponseEntity.ok(userGifts);
            } else {
                throw new AccessDeniedException("Authenticated user does not have access to this user's groups");
            }
        } catch (Exception e) {
            logger.error("Error retrieving gifts created by user with ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
