package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.mappers.GiftMapper;
import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftServiceImplTest {

    @Mock
    private IGiftRepo giftRepo;

    @Mock
    private GiftMapper giftMapper;

    @InjectMocks
    private GiftServiceImpl giftService;

    @Test
    void getAllGifts() {
        // Arrange
        Gift gift1 = new Gift();
        Gift gift2 = new Gift();
        List<Gift> expectedGifts = Arrays.asList(gift1, gift2);

        // Mock the behavior of findAll in the repository
        when(giftRepo.findAll()).thenReturn(expectedGifts);

        // Mock the behavior of the mapper
        GiftDTO giftDto1 = new GiftDTO();
        GiftDTO giftDto2 = new GiftDTO();
        when(giftMapper.toGiftDTO(gift1)).thenReturn(giftDto1);
        when(giftMapper.toGiftDTO(gift2)).thenReturn(giftDto2);

        // Act
        List<GiftDTO> actualGifts = giftService.getAllGifts();

        // Assert
        assertEquals(2, actualGifts.size());
        assertEquals(giftDto1, actualGifts.get(0));
        assertEquals(giftDto2, actualGifts.get(1));
    }

//    @Test
//    void getGiftById() {
//        int giftId = 1;
//        Gift expectedGift = new Gift();
//        when(giftRepo.findById(giftId)).thenReturn(Optional.of(expectedGift));
//
//        Gift actualGift = giftService.getGiftById(giftId);
//
//        assertSame(expectedGift, actualGift);
//    }
//
//    @Test
//    void createGift() {
//        GiftDTO giftDto = new GiftDTO();
//        Gift expectedGift = new Gift();
//        when(giftRepo.save(any(Gift.class))).thenReturn(expectedGift);
//
//        Gift actualGift = giftService.createGift(giftDto);
//        assertSame(expectedGift, actualGift);
//    }

//    @Test
//    void updateGiftNotFound() {
//        int giftId = 1;
//        GiftDTO updatedGiftDTO = new GiftDTO();
//
//        when(giftRepo.findById(giftId)).thenReturn(Optional.empty());
//        assertThrows(EntityNotFoundException.class, () -> giftService.updateGift(giftId, updatedGiftDTO));
//    }

//    @Test
//    void updateGiftNotFound() {
//        int giftId = 1;
//        GiftDTO updatedGiftDTO = new GiftDTO();
//
//        assertThrows(EntityNotFoundException.class, () -> giftService.updateGift(giftId, updatedGiftDTO));
//    }

//    @Test
//    void updateGift_Successful() {
//        // Arrange
//        int giftId = 1;
//        GiftDTO updatedGiftDTO = new GiftDTO();
//        Gift existingGift = new Gift();
//        Gift updatedGift = new Gift();
//        when(giftRepo.existsById(giftId)).thenReturn(true);
//        when(giftRepo.findById(giftId)).thenReturn(Optional.of(existingGift));
//        when(giftRepo.save(any(Gift.class))).thenReturn(updatedGift);
//        when(giftMapper.toGiftDTO(updatedGift)).thenReturn(updatedGiftDTO);
//
//        // Act
//        GiftDTO result = giftService.updateGift(giftId, updatedGiftDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(updatedGiftDTO, result);
//
//        // Verify interactions
//        verify(giftRepo).existsById(giftId);
//        verify(giftRepo).findById(giftId);
//        verify(giftRepo).save(any(Gift.class));
//        verify(giftMapper).toGiftDTO(updatedGift);
//    }

    //    @Test
//    void updateGift_Successful() {
//        int giftId = 1;
//        GiftDTO giftDto = new GiftDTO();
//        giftDto.setName("NewName");
//
//        Gift existingGift = new Gift();
//        existingGift.setGiftId(giftId);
//        existingGift.setName("OldName");
//
//        when(giftRepo.findById(giftId)).thenReturn(Optional.of(existingGift));
//        when(giftRepo.save(existingGift)).thenReturn(existingGift);
//
//        var result = giftService.updateGift(giftId, giftDto);
//
//        assertEquals(giftDto.getName(), result.getName());
//
//        // Verify that the save method is called with the updated gift
//        verify(giftRepo).save(existingGift);
//
//    }
    @Test
    void updateGift_GiftNotFound() {
        // Arrange
        int giftId = 1;
        GiftDTO updatedGiftDTO = new GiftDTO();

        // Mock the behavior of existsById to return false
        when(giftRepo.existsById(giftId)).thenReturn(false);

        // Mock the behavior of findById to return null when called with the specified giftId
        when(giftRepo.findById(giftId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> giftService.updateGift(updatedGiftDTO));

        // Verify interactions
        verify(giftRepo).existsById(giftId);
        verify(giftRepo).findById(giftId); // Ensure findById is called
        verify(giftRepo, never()).save(any(Gift.class));
        verify(giftMapper, never()).toGiftDTO(any(Gift.class));
    }


    @Test
    void deleteGift() {
        int giftId = 1;
        assertThrows(EntityNotFoundException.class, () -> giftService.deleteGift(giftId));
        verify(giftRepo, never()).deleteById(giftId);
    }


}