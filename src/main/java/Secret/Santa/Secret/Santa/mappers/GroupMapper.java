package Secret.Santa.Secret.Santa.mappers;

import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    private static UserUtils userUtils;

    public GroupMapper(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    public Group toGroup(GroupDTO groupDTO) {

        Group group = new Group();
        group.setGroupId(groupDTO.getGroupId());
        group.setName(groupDTO.getName());
        group.setEventDate(groupDTO.getEventDate());
        group.setBudget(groupDTO.getBudget());
        group.setUser(groupDTO.getUser());
        group.setGifts(groupDTO.getGifts());
        group.setGeneratedSanta(groupDTO.getGeneratedSanta());
        User owner = userUtils.getUserById(groupDTO.getOwnerId());
        group.setOwner(owner);

        return group;
    }


    public GroupDTO toGroupDTO(Group group) {

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setGroupId(group.getGroupId());
        groupDTO.setName(group.getName());
        groupDTO.setEventDate(group.getEventDate());
        groupDTO.setBudget(group.getBudget());

        groupDTO.setUser(group.getUser());
        groupDTO.setGifts(group.getGifts());
        groupDTO.setGeneratedSanta(group.getGeneratedSanta());
        groupDTO.setOwnerId(group.getOwner().getUserId());

        return groupDTO;
    }
}