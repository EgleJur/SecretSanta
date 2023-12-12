package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.mappers.GroupMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private IGroupRepo groupRepo;

    @Mock
    private IUserRepo userRepo;

    @Mock
    private UserUtils userUtils;

    @Mock
    private GroupUtils groupUtils;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private IGiftRepo giftRepo;

    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    void getAllGroups() {
        List<Group> expectedGroups = Arrays.asList(new Group(), new Group());
        List<GroupDTO> expectedGroupsDTO = Arrays.asList(new GroupDTO(), new GroupDTO());

        when(groupRepo.findAll()).thenReturn(expectedGroups);
        when(groupMapper.toGroupDTO(any(Group.class))).thenAnswer(i -> expectedGroupsDTO.get(expectedGroups.indexOf(i.getArgument(0))));

        List<GroupDTO> actualGroups = groupService.getAllGroups();

        assertEquals(expectedGroupsDTO, actualGroups);
        verify(groupMapper, times(expectedGroups.size())).toGroupDTO(any(Group.class));
    }

    @Test
    void getGroupByIdFound() {
        int groupId = 1;
        Group expectedGroup = new Group();
        GroupDTO expectedGroupDTO = new GroupDTO();

        when(groupRepo.findById(groupId)).thenReturn(Optional.of(expectedGroup));
        when(groupMapper.toGroupDTO(expectedGroup)).thenReturn(expectedGroupDTO);

        GroupDTO actualGroupDTO = groupService.getGroupById(groupId);

        assertEquals(expectedGroupDTO, actualGroupDTO);
        verify(groupMapper).toGroupDTO(expectedGroup);
    }

    @Test
    void getGroupByIdNotFound() {
        int groupId = 1;
        when(groupRepo.findById(groupId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> groupService.getGroupById(groupId));
    }

    @Test
    void createGroup() {
        GroupDTO groupDTO = mock(GroupDTO.class);
        Group group = new Group();
        group.setUser(new ArrayList<>());

        when(groupDTO.getOwnerId()).thenReturn(1);

        User mockUser = new User();
        mockUser.setUserId(1);

        when(userRepo.findById(anyInt())).thenReturn(Optional.of(mockUser));
        when(groupMapper.toGroup(any(GroupDTO.class))).thenReturn(group);
        when(groupRepo.save(any(Group.class))).thenReturn(group);
        when(groupMapper.toGroupDTO(any(Group.class))).thenReturn(groupDTO);

        GroupDTO result = groupService.createGroup(groupDTO);

        assertNotNull(result);
        verify(groupRepo).save(any(Group.class));
    }

    @Test
    void editGroupFound() {
        int groupId = 1;
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setGroupId(groupId);

        Group existingGroup = new Group();
        GroupDTO updatedGroupDTO = new GroupDTO();

        when(groupRepo.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(groupMapper.toGroup(groupDTO)).thenReturn(existingGroup);
        when(groupRepo.save(existingGroup)).thenReturn(existingGroup);
        when(groupMapper.toGroupDTO(existingGroup)).thenReturn(updatedGroupDTO);

        GroupDTO result = groupService.editByGroupId(groupDTO);

        assertEquals(updatedGroupDTO, result);
        verify(groupMapper).toGroup(groupDTO);
        verify(groupMapper).toGroupDTO(existingGroup);
    }

    @Test
    void editGroupNotFound() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setGroupId(1);

        when(groupRepo.findById(groupDTO.getGroupId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> groupService.editByGroupId(groupDTO));
    }

    @Test
    void deleteGroupByGroupId() {
        int groupId = 1;
        Group group = new Group();
        group.setUser(new ArrayList<>());

        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        doNothing().when(giftRepo).deleteByGroup(group);
        doNothing().when(groupRepo).delete(group);

        boolean result = groupService.deleteGroupByGroupId(groupId);

        assertTrue(result);
        verify(groupRepo).delete(group);
    }


    @Test
    void getAllGroupsForUser() {
        Integer userId = 1;
        User mockUser = mock(User.class);
        List<Group> groups = Arrays.asList(new Group(), new Group());
        GroupDTO groupDTO = new GroupDTO();

        when(userUtils.getUserById(userId)).thenReturn(mockUser);
        when(groupRepo.findByUserContaining(mockUser)).thenReturn(groups);
        when(groupMapper.toGroupDTO(any(Group.class))).thenReturn(groupDTO);

        List<GroupDTO> result = groupService.getAllGroupsForUser(userId);

        assertEquals(groups.size(), result.size());
        verify(groupRepo).findByUserContaining(mockUser);
    }


    @Test
    void getAllGroupsForOwner() {
        Integer ownerId = 1;
        User mockOwner = mock(User.class);
        List<Group> groups = Arrays.asList(new Group(), new Group());
        GroupDTO groupDTO = new GroupDTO();

        when(userUtils.getUserById(ownerId)).thenReturn(mockOwner);
        when(groupRepo.findByOwner(mockOwner)).thenReturn(groups);
        when(groupMapper.toGroupDTO(any(Group.class))).thenAnswer(invocation -> new GroupDTO());

        List<GroupDTO> result = groupService.getAllGroupsForOwner(ownerId);

        assertEquals(groups.size(), result.size());
        verify(groupRepo).findByOwner(mockOwner);
    }

}
