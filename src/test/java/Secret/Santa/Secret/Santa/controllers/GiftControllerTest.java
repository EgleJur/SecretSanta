//package Secret.Santa.Secret.Santa.controllers;
//
//import Secret.Santa.Secret.Santa.models.DTO.GiftDTO;
//import Secret.Santa.Secret.Santa.models.Gift;
//import Secret.Santa.Secret.Santa.services.IGiftService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//class GiftControllerTest {
//    @Mock
//    private IGiftService giftService;
//
//    @InjectMocks
//    private GiftController giftController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(giftController).build();
//    }
//
//    @Test
//    void getAllGifts() throws Exception {
//        List<Gift> gifts = Arrays.asList(new Gift(), new Gift());
//        when(giftService.getAllGifts()).thenReturn(gifts);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/gifts"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(gifts.size()));
//    }
//
//    @Test
//    void getGiftById() throws Exception {
//        int giftId = 1;
//        Gift gift = new Gift();
//        when(giftService.getGiftById(giftId)).thenReturn(gift);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/gifts/{giftId}", giftId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(gift.getName()));
//    }
//
//    @Test
//    void createGift() throws Exception {
//        GiftDTO giftDto = new GiftDTO();
//        giftDto.setName("gifty");
//        Gift createdGift = new Gift();
//
//        doReturn(createdGift).when(giftService).createGift(any(GiftDTO.class));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/gifts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(giftDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(createdGift.getName()));
//
//        verify(giftService, times(1)).createGift(any(GiftDTO.class));
//
//    }
//
////    @Test
////    void updateGift() throws Exception {
////        GiftDTO updatedGiftDTO = new GiftDTO();
////        updatedGiftDTO.setName("Updated Gift");
////
////        Gift updatedGift = new Gift();
////        updatedGift.setGiftId(1);
////        updatedGift.setName(updatedGiftDTO.getName());
////        when(giftService.updateGift(eq(1), any(GiftDTO.class))).thenReturn(updatedGift);
////
////        mockMvc.perform(put("/api/v1/gifts/1")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(new ObjectMapper().writeValueAsString(updatedGiftDTO)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.name").value("Updated Gift"));
////    }
//
//    @Test
//    void deleteGift() throws Exception {
//        int giftId = 1;
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/gifts/{giftId}", giftId))
//                .andExpect(status().isNoContent());
//
//        verify(giftService, times(1)).deleteGift(giftId);
//    }
//}