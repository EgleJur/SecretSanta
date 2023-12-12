package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.mappers.GiftMapper;
import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.Role;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftServiceImplTest {

    @Mock
    private GiftMapper giftMapper;
    @Mock
    private IGiftRepo iGiftRepo;
    @Mock
    private GroupUtils groupUtils;
    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private GiftServiceImpl giftService;

    private User testUser;
    private Group testGroup;
    private Gift testGift;
    private GiftDTO testGiftDTO;

    @BeforeEach
    void setUp() {
        testUser = new User(1, "Test User", "test@example.com", "password", Role.USER);
        testGroup = new Group(
                1,
                "Test Group",
                LocalDate.now(),
                100.0,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                testUser // owner
        );
        testGift = new Gift(
                1,
                "Test Gift",
                "Description",
                "http://link",
                50.0,
                testUser, // createdBy
                testGroup
        );
        testGiftDTO = new GiftDTO(
                1,
                "Test Gift",
                "Description",
                "http://link",
                50.0,
                testUser.getUserId(), // createdBy user ID
                testGroup.getGroupId()
        );
    }

    @Test
    void getAllGifts() {
        when(iGiftRepo.findAll()).thenReturn(Arrays.asList(testGift));
        when(giftMapper.toGiftDTO(any(Gift.class))).thenReturn(testGiftDTO);

        List<GiftDTO> result = giftService.getAllGifts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(testGiftDTO, result.get(0));
    }

    @Test
    void getGiftById() {
        when(iGiftRepo.findById(testGift.getGiftId())).thenReturn(Optional.of(testGift));
        when(giftMapper.toGiftDTO(testGift)).thenReturn(testGiftDTO);

        GiftDTO result = giftService.getGiftById(testGift.getGiftId());

        assertNotNull(result);
        assertEquals(testGiftDTO, result);
    }

    @Test
    void createGift() {
        when(giftMapper.toGift(testGiftDTO)).thenReturn(testGift);
        when(groupUtils.getGroupById(testGiftDTO.getGroupId())).thenReturn(testGroup); // This line is crucial
        when(iGiftRepo.save(testGift)).thenReturn(testGift);
        when(giftMapper.toGiftDTO(testGift)).thenReturn(testGiftDTO);

        GiftDTO result = giftService.createGift(testGiftDTO);

        assertNotNull(result);
        assertEquals(testGiftDTO, result);
    }


    @Test
    void updateGift_Successful() {
        // Setup
        when(iGiftRepo.findById(testGiftDTO.getGiftId())).thenReturn(Optional.of(testGift));
        when(groupUtils.getGroupById(testGiftDTO.getGroupId())).thenReturn(testGroup);
        when(giftMapper.toGift(testGiftDTO)).thenReturn(testGift);
        when(iGiftRepo.save(testGift)).thenReturn(testGift);
        when(giftMapper.toGiftDTO(testGift)).thenReturn(testGiftDTO);

        // Act
        GiftDTO result = giftService.updateGift(testGiftDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testGiftDTO.getName(), result.getName());
    }

    @Test
    void deleteGift() {
        when(iGiftRepo.findById(testGift.getGiftId())).thenReturn(Optional.of(testGift));
        doNothing().when(iGiftRepo).deleteById(testGift.getGiftId());

        assertTrue(giftService.deleteGift(testGift.getGiftId()));
        verify(iGiftRepo).deleteById(testGift.getGiftId());
    }

    @Test
    void deleteGift_NotFound() {
        when(iGiftRepo.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> giftService.deleteGift(99));
    }

    @Test
    void getGiftsCreatedByUser() {
        when(userUtils.getUserById(testUser.getUserId())).thenReturn(testUser);
        when(iGiftRepo.findByCreatedBy(testUser)).thenReturn(Arrays.asList(testGift));

        List<Gift> result = giftService.getGiftsCreatedBy(testUser.getUserId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(testGift, result.get(0));
    }
}
