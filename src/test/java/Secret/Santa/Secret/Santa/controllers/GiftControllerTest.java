package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.mappers.GiftMapper;
import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.services.IGiftService;
import Secret.Santa.Secret.Santa.services.impl.UserServiceImpl;
import Secret.Santa.Secret.Santa.services.validationUnits.GiftUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GiftControllerTest {
    @Mock
    private IGiftService giftService;

    @InjectMocks
    private GiftController giftController;

    private MockMvc mockMvc;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private UserUtils userUtils;
    @Mock
    private GiftUtils giftUtils;
    @Mock
    private GroupUtils groupUtils;
    @Mock
    private IGiftRepo iGiftRepo;
    @Mock
    private GiftMapper giftMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(giftController).build();
    }

    @Test
    void getAllGifts() throws Exception {
        List<GiftDTO> gifts = Arrays.asList(new GiftDTO(), new GiftDTO());
        when(giftService.getAllGifts()).thenReturn(gifts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/gifts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(gifts.size()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void getGiftById() throws Exception {
        int userId = 1;
        int giftId = 1;

        GiftDTO gift = new GiftDTO();
        gift.setCreatedBy(userId);
        gift.setName("Example Gift");
        when(giftService.getGiftById(anyInt())).thenReturn(gift);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/gifts/users/{userId}/gifts/{giftId}", userId, giftId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(gift.getName()));
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void createGift() throws Exception {
        // Given
        GiftDTO giftDto = new GiftDTO();
        giftDto.setName("gifty");
        giftDto.setDescription("A description");
        giftDto.setLink("https://example.com");
        giftDto.setPrice(10.5);
        giftDto.setCreatedBy(1);
        giftDto.setGroupId(1);

        when(giftService.createGift(any(GiftDTO.class))).thenReturn(giftDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/gifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(giftDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(giftDto.getName()));

        verify(giftService, times(1)).createGift(any(GiftDTO.class));
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void updateGift() throws Exception {
        GiftDTO updatedGiftDTO = new GiftDTO();
        updatedGiftDTO.setGiftId(1);
        updatedGiftDTO.setName("Updated Gift");

        when(giftService.updateGift(any(GiftDTO.class))).thenReturn(updatedGiftDTO);

        mockMvc.perform(put("/api/v1/gifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedGiftDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Gift"));
    }


    @Test
    void deleteGift() throws Exception {
        int giftId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/gifts/{giftId}", giftId))
                .andExpect(status().isNoContent());

        verify(giftService, times(1)).deleteGift(giftId);
    }
}