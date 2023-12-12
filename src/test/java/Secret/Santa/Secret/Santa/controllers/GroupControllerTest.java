package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.services.IGroupService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IGroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void getAllGroups() throws Exception {
        List<GroupDTO> groups = Arrays.asList(new GroupDTO(), new GroupDTO());
        when(groupService.getAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/api/v1/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(groups.size()));
    }

    @Test
    void getGroupById() throws Exception {
        int groupId = 1;
        GroupDTO group = new GroupDTO();
        when(groupService.getGroupById(groupId)).thenReturn(group);

        mockMvc.perform(get("/api/v1/groups/{groupId}", groupId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void createGroup() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("Christmas Party");
        groupDTO.setEventDate(LocalDate.of(2023, 12, 25));
        groupDTO.setBudget(100.0);
        groupDTO.setOwnerId(1); // Set an owner ID as it's a required field
        // Set other required fields if needed to satisfy the validation

        GroupDTO group = new GroupDTO();
        when(groupService.createGroup(any(GroupDTO.class))).thenReturn(group);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(groupDTO);

        mockMvc.perform(post("/api/v1/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }


    @Test
    void updateGroup() throws Exception {
        int groupId = 1;
        GroupDTO groupDTO = new GroupDTO();
        GroupDTO updatedGroup = new GroupDTO();
        when(groupService.editByGroupId(any(GroupDTO.class))).thenReturn(updatedGroup);

        mockMvc.perform(put("/api/v1/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) //TODO Replace {} with aJSON content for GroupDTO
                .andExpect(status().isOk());
    }

    @Test
    void deleteGroup() throws Exception {
        int groupId = 1;
        when(groupService.deleteGroupByGroupId(groupId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/groups/{groupId}", groupId))
                .andExpect(status().isNoContent());
    }
}
