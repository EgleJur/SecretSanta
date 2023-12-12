package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.mappers.GiftMapper;
import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    private GroupUtils groupUtils;
    @Mock
    private IGroupRepo groupRepo;
    @InjectMocks
    private GiftServiceImpl giftService;

    @Test
    void getAllGifts() {
        Gift gift1 = new Gift();
        Gift gift2 = new Gift();
        List<Gift> expectedGifts = Arrays.asList(gift1, gift2);

        when(giftRepo.findAll()).thenReturn(expectedGifts);

        when(giftMapper.toGiftDTO(any(Gift.class))).thenReturn(new GiftDTO());

        List<GiftDTO> actualGifts = giftService.getAllGifts();

        assertEquals(2, actualGifts.size());
        verify(giftRepo, times(1)).findAll();
        verify(giftMapper, times(2)).toGiftDTO(any(Gift.class));
    }


    @Test
    void getGiftById() {
        int giftId = 1;
        GiftDTO expectedGiftDTO = new GiftDTO();

        Gift gift = new Gift();
        when(giftRepo.findById(giftId)).thenReturn(Optional.of(gift));
        when(giftMapper.toGiftDTO(gift)).thenReturn(expectedGiftDTO);

        GiftDTO actualGiftDTO = giftService.getGiftById(giftId);

        assertEquals(expectedGiftDTO, actualGiftDTO);
    }

//    @Test
//    void createGift() {
//        GiftDTO giftDTO = new GiftDTO();
//        giftDTO.setName("GiftName");
//
//        Gift expectedGift = new Gift();
//
//        when(groupUtils.getGroupById(isNull())).thenReturn(new Group());
//        when(giftRepo.save(any(Gift.class))).thenReturn(expectedGift);
//        when(giftMapper.toGift(any(GiftDTO.class))).thenReturn(expectedGift);
//
//        GiftDTO actualGiftDTO = giftService.createGift(giftDTO);
//
//        assertNotNull(actualGiftDTO, "Returned GiftDTO should not be null");
//        assertNotNull(actualGiftDTO.getGiftId(), "GiftDTO ID should not be null");
//
//        verify(giftRepo, times(1)).save(any(Gift.class));
//        verify(giftMapper, times(1)).toGift(any(GiftDTO.class));
//
//        assertEquals(expectedGift, actualGiftDTO, "Returned GiftDTO should match the expected Gift");
//    }


//    @Test
//    void updateGiftNotFound() {
//        int giftId = 1;
//        GiftDTO updatedGiftDTO = new GiftDTO();
//
//        when(giftRepo.findById(giftId)).thenReturn(Optional.empty());
//
//        Group group = mock(Group.class); // Mock the Group object
//        when(group.getBudget()).thenReturn(100.0); // Set the budget for the group
//
//        Gift gift = mock(Gift.class);
//
//        when(gift.getGroup()).thenReturn(group);
//
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> giftService.updateGift(updatedGiftDTO));
//        assertEquals("Group not found for the gift with id " + giftId, exception.getMessage());
//    }

    @Test
    void updateGift_Successful() {
        GiftDTO inputGiftDTO = new GiftDTO();
        inputGiftDTO.setGiftId(1);
        inputGiftDTO.setGroupId(2);
        inputGiftDTO.setPrice(50);

        Group group = new Group();
        group.setBudget(100);


        Gift inputGift = new Gift();
        inputGift.setGiftId(1);
        inputGift.setGroup(group);

        when(groupUtils.getGroupById(2)).thenReturn(group);
        when(giftRepo.findById(1)).thenReturn(Optional.of(inputGift));
        when(giftRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Gift updatedGift = new Gift();
        updatedGift.setGiftId(1);
        updatedGift.setGroup(group);

        when(giftMapper.toGift(any())).thenReturn(updatedGift);
        when(giftMapper.toGiftDTO(updatedGift)).thenReturn(inputGiftDTO);

        GiftDTO result = giftService.updateGift(inputGiftDTO);


        assertNotNull(result);
        assertEquals(inputGiftDTO.getGiftId(), result.getGiftId());
        assertEquals(inputGiftDTO.getName(), result.getName());
        assertEquals(inputGiftDTO.getDescription(), result.getDescription());
        assertEquals(inputGiftDTO.getLink(), result.getLink());
        assertEquals(inputGiftDTO.getPrice(), result.getPrice());
        assertEquals(inputGiftDTO.getCreatedBy(), result.getCreatedBy());
        assertEquals(inputGiftDTO.getGroupId(), result.getGroupId());
    }

    @Test
    void deleteGift() {
        int giftId = 1;
        assertThrows(EntityNotFoundException.class, () -> giftService.deleteGift(giftId));
        verify(giftRepo, never()).deleteById(giftId);
    }


}