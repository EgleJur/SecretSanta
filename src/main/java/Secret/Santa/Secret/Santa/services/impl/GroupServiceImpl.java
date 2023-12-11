package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.mappers.GroupMapper;
import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.IGroupService;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements IGroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Autowired
    private final IGroupRepo groupRepo;

    @Autowired
    private final IUserRepo userRepo;

    private final UserUtils userUtils;
    private final GroupMapper groupMapper;
    private final GroupUtils groupUtils;
    private final IGiftRepo iGiftRepo;

    @Override
    public List<GroupDTO> getAllGroups() {
        try {
            List<Group> groups = groupRepo.findAll();

            return groups.stream()
                    .map(groupMapper::toGroupDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Failed to retrieve all groups", e);
            throw e;
        }
    }

    @Override
    public GroupDTO getGroupById(int groupId) {
        try {
            Optional<Group> optionalGroup = groupRepo.findById(groupId);

            if (optionalGroup.isPresent()) {
                Group group = optionalGroup.get();
                return groupMapper.toGroupDTO(group);
            }
            throw new EntityNotFoundException("Group not found with id " + groupId);
        } catch (Exception e) {
            logger.error("Failed to retrieve group with ID: {}", groupId, e);
            throw e;
        }
    }

    @Override
    public GroupDTO editByGroupId(GroupDTO groupDTO) {
        if (groupDTO == null) {
            throw new IllegalArgumentException("GroupDTO cannot be null");
        }
        if (groupDTO.getGroupId() == null) {
            throw new IllegalArgumentException("This user does not have ID");
        }
        Optional<Group> existingGroup = groupRepo.findById(groupDTO.getGroupId());
        if (existingGroup.isPresent()) {
            Group group = groupMapper.toGroup(groupDTO);
            Group updatedGroup = groupRepo.save(group);
            return groupMapper.toGroupDTO(updatedGroup);
        }
        throw new EntityNotFoundException("User not found with id " + groupDTO.getGroupId());
    }

    @Override
    public GroupDTO createGroup(GroupDTO groupDTO) {
        if (groupDTO == null) {
            throw new IllegalArgumentException("GroupDTO cannot be null");
        }
        try {
            Group group = groupMapper.toGroup(groupDTO);

            User owner = userRepo.findById(groupDTO.getOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + groupDTO.getOwnerId()));
            group.getUser().add(owner);

            Group savedGroup = groupRepo.save(group);
            return groupMapper.toGroupDTO(savedGroup);

        } catch (Exception e) {
            logger.error("Failed to create group", e);
            throw e;
        }
    }

    @Override
    public List<GroupDTO> getAllGroupsForUser(Integer userId) {
        try {
            User user = userUtils.getUserById(userId);
            List<Group> groups = groupRepo.findByUserContaining(user);

            return groups.stream()
                    .map(groupMapper::toGroupDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all groups for user with ID: {}", userId, e);
            throw e;
        }
    }

    @Override
    public List<GroupDTO> getAllGroupsForOwner(Integer userId) {
        try {
            User user = userUtils.getUserById(userId);
            List<Group> groups = groupRepo.findByOwner(user);

            return groups.stream()
                    .map(groupMapper::toGroupDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all groups for owner with ID: {}", userId, e);
            throw e;
        }
    }


    @Override
    public boolean deleteGroupByGroupId(int groupId) {
        Group group = groupUtils.getGroupById(groupId);
        if (group != null) {

            group.getUser().clear();
            groupRepo.save(group);
            try {
                iGiftRepo.deleteByGroup(group);
            } catch (Exception e) {
                logger.error("Error deleting gifts in group with ID: {}", groupId, e);
                return false;
            }
            try {
                groupRepo.delete(group);
                return true;
            } catch (Exception e) {
                logger.error("Error deleting group with ID: {}", groupId, e);
                return false;
            }
        } else {
            logger.error("Attempted to delete a group that does not exist with ID: {}", groupId);
            throw new EntityNotFoundException("Group not found with id " + groupId);
        }
    }

    public GroupDTO addUserToGroup(int groupId, int userId) {
        var existingGroup = groupRepo.findById(groupId)
                .orElseThrow(() -> new SantaValidationException("Group does not exist",
                        "id", "Group not found", String.valueOf(groupId)));

        if (existingGroup.getUser().stream().anyMatch(user -> user.getUserId() == userId)) {
            throw new IllegalArgumentException("User is already in the group");
        }

        var existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new SantaValidationException("User does not exist",
                        "id", "User not found", String.valueOf(userId)));

        List<User> existingUserList = existingGroup.getUser();
        existingUserList.add(existingUser);
        existingGroup.setUser(existingUserList);
        GroupDTO savedGroupDTO = groupMapper.toGroupDTO(groupRepo.save(existingGroup));
        return savedGroupDTO;
    }


    public List<User> getAllUsersById(int groupId) {
        try {
            return groupRepo.findById(groupId)
                    .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + groupId))
                    .getUser();
        } catch (Exception e) {
            logger.error("Error retrieving users for group with ID: {}", groupId, e);
            throw e;
        }
    }

}
