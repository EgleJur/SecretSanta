package Secret.Santa.Secret.Santa.services;

import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;

import java.util.ArrayList;
import java.util.List;

public interface IGroupService {
    List<Group> getAllGroups();
    Group editByGroupId(GroupDTO groupDTO, int groupId);
    Group createGroup(GroupDTO groupDTO);
    boolean deleteGroupByGroupId(int groupId);
    Group getGroupById(int groupId);
}
