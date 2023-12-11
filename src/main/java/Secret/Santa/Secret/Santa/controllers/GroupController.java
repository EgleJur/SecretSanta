package Secret.Santa.Secret.Santa.controllers;


import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.services.IGroupService;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    @Autowired
    private final IGroupService iGroupService;
    private final UserUtils userUtils;
    private final GroupUtils groupUtils;

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        try {
            List<GroupDTO> groupsDTOs = iGroupService.getAllGroups();
            return new ResponseEntity<>(groupsDTOs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to get all groups", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDTO> getGroupById(@Valid
                                                 @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                                 @PathVariable int groupId) {

        try {
            GroupDTO groupDTO = iGroupService.getGroupById(groupId);
            return ResponseEntity.ok(groupDTO);

        } catch (Exception e) {
            logger.error("Failed to get group with ID: {}", groupId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@Valid @RequestBody GroupDTO groupDTO) {
        try {
            GroupDTO createdGroupDTO = iGroupService.createGroup(groupDTO);
            return new ResponseEntity<>(createdGroupDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create group", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<GroupDTO> updateGroup(@Valid @RequestBody GroupDTO groupDTO) {
        try {
            GroupDTO editedGroupDTO = iGroupService.editByGroupId(groupDTO);
            return ResponseEntity.ok(editedGroupDTO);
        } catch (Exception e) {
            logger.error("Failed to update group with ID: {}", groupDTO.getGroupId(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}/groups")
    public List<GroupDTO> getAllGroupsForUser(@PathVariable("userId") Integer userId, Principal principal) {

        String authenticatedEmail = principal.getName();
        try {
            if (userUtils.getUserById(userId).getEmail().equals(authenticatedEmail)) {
                return iGroupService.getAllGroupsForUser(userId);
            } else {
                throw new AccessDeniedException("Authenticated user does not have access to this user's groups");
            }
        } catch (Exception e) {
            logger.error("Error retrieving groups for user with ID: {}", userId, e);
            throw e;
        }
    }

    @GetMapping("/owner/{userId}/groups")
    public List<GroupDTO> getAllGroupsForOwner(@PathVariable("userId") Integer userId, Principal principal) {
        String authenticatedEmail = principal.getName();

        try {
            if (userUtils.getUserById(userId).getEmail().equals(authenticatedEmail)) {
                return iGroupService.getAllGroupsForOwner(userId);
            } else {
                throw new AccessDeniedException("Authenticated user does not have access to this user's groups");
            }

        } catch (Exception e) {
            logger.error("Error retrieving groups for owner with ID: {}", userId, e);
            throw e;
        }
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable int groupId) {
        try {
            boolean deleted = iGroupService.deleteGroupByGroupId(groupId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Failed to delete group with ID: {}", groupId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{groupId}/users/{userId}/newUsers")
    public ResponseEntity<GroupDTO> addUserToGroup(@PathVariable int groupId, @Valid @PathVariable int userId) {

        var updatedGroup = iGroupService.addUserToGroup(groupId, userId);

        return ok(updatedGroup);

    }

    @GetMapping(value = "/{groupId}/users")
    @ResponseBody
    public List<User> getAllUsersById(@PathVariable int groupId) {
        try {
            return iGroupService.getAllUsersById(groupId);
        } catch (Exception e) {
            logger.error("Error retrieving users for group with ID: {}", groupId, e);
            throw new RuntimeException("Failed to retrieve users for group", e);
        }
    }

    @GetMapping("/users/{userId}/groups/{groupId}")
    public ResponseEntity<GroupDTO> getGroupForUser(
            @PathVariable int userId,
            @PathVariable int groupId,
            Principal principal) {

        String authenticatedEmail = principal.getName();

        if (!userUtils.getUserById(userId).getEmail().equals(authenticatedEmail)) {
            throw new AccessDeniedException("Authenticated user does not have access to this user's groups");
        }

        boolean isUserInGroup = groupUtils.isUserInGroup(userId, groupId);
        if (!isUserInGroup) {
            throw new AccessDeniedException("Authenticated user does not have access to this user's groups");
        }

        GroupDTO groupDTO = iGroupService.getGroupById(groupId);
        return ResponseEntity.ok(groupDTO);
    }
}
