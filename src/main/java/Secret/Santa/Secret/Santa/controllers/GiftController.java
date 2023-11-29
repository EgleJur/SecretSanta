package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.services.IGiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gifts")
public class GiftController {
    private final IGiftService giftService;

    public GiftController(IGiftService giftService) {
        this.giftService = giftService;
    }

    @GetMapping
    public ResponseEntity<List<Gift>> getAllGifts() {
        List<Gift> gifts = giftService.getAllGifts();
        return new ResponseEntity<>(gifts, HttpStatus.OK);
    }

    @GetMapping("/{giftId}")
    public ResponseEntity<Gift> getGiftById(@PathVariable int giftId) {
        Gift gift = giftService.getGiftById(giftId);
        return gift != null ?
                new ResponseEntity<>(gift, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Gift> createGift(@RequestBody Gift gift) {
        Gift createdGift = giftService.createGift(gift);
        return new ResponseEntity<>(createdGift, HttpStatus.CREATED);
    }

    @PutMapping("/{giftId}")
    public ResponseEntity<Gift> updateGift(@PathVariable int giftId, @RequestBody Gift updatedGift) {
        Gift updated = giftService.updateGift(giftId, updatedGift);
        return updated != null ?
                new ResponseEntity<>(updated, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{giftId}")
    public ResponseEntity<Void> deleteGift(@PathVariable int giftId) {
        giftService.deleteGift(giftId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
