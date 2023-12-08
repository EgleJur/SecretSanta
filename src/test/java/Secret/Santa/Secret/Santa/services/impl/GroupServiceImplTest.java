package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private IGroupRepo groupRepo;

    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    void getAllGroups() {
        List<Group> expectedGroups = Arrays.asList(new Group(), new Group());
        when(groupRepo.findAll()).thenReturn(expectedGroups);

        List<Group> actualGroups = groupService.getAllGroups();
        assertSame(expectedGroups, actualGroups);
    }

    @Test
    void getGroupByIdFound() {
        int groupId = 1;
        Group expectedGroup = new Group();
        when(groupRepo.findById(groupId)).thenReturn(Optional.of(expectedGroup));

        Group actualGroup = groupService.getGroupById(groupId);
        assertSame(expectedGroup, actualGroup);
    }

    @Test
    void getGroupByIdNotFound() {
        int groupId = 1;
        when(groupRepo.findById(groupId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> groupService.getGroupById(groupId));
    }

    @Test
    void createGroup() {
        GroupDTO groupDto = new GroupDTO();
        Group expectedGroup = new Group();
        when(groupRepo.save(any(Group.class))).thenReturn(expectedGroup);

        Group actualGroup = groupService.createGroup(groupDto);
        assertSame(expectedGroup, actualGroup);
    }

    /*
    @Test
    void editGroupFound() {
        int groupId = 1;
        GroupDTO groupDTO = new GroupDTO();
        Group existingGroup = new Group();
        when(groupRepo.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(groupRepo.save(any(Group.class))).thenReturn(existingGroup);

        Group updatedGroup = groupService.editByGroupId(groupDTO, groupId);
        assertSame(existingGroup, updatedGroup);
    }
    */

    @Test
    void editGroupNotFound() {
        int groupId = 1;
        GroupDTO groupDTO = new GroupDTO();
        when(groupRepo.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> groupService.editByGroupId(groupDTO, groupId));
    }

    @Test
    void deleteGroup() {
        int groupId = 1;
        when(groupRepo.existsById(groupId)).thenReturn(true);

        assertTrue(groupService.deleteGroupByGroupId(groupId));
        verify(groupRepo, times(1)).deleteById(groupId);
    }
}
