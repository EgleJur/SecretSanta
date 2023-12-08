package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.mappers.UserMapper;
import Secret.Santa.Secret.Santa.models.DTO.GroupDTO;
import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.Role;
import Secret.Santa.Secret.Santa.models.Gift;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.Role;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.IUserService;
import Secret.Santa.Secret.Santa.validationUnits.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final IUserRepo iUserRepo;
    private final IGiftRepo iGiftRepo;
    private final UserUtils userUtils;
    private final IGroupRepo iGroupRepo;
    private final IGroupRepo groupRepo;

//    public UserServiceImpl(UserMapper userMapper, IUserRepo iUserRepo, IGiftRepo iGiftRepo, UserUtils userUtils,
//                           IGroupRepo iGroupRepo, GroupServiceImpl groupService, IGroupRepo groupRepo) {
//        this.userMapper = userMapper;
//        this.iUserRepo = iUserRepo;
//        this.iGiftRepo = iGiftRepo;
//        this.userUtils = userUtils;
//        this.iGroupRepo = iGroupRepo;
//        this.groupRepo = groupRepo;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = iUserRepo.findByEmail(username) //TODO check if this is correct
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        try {
            List<User> users = iUserRepo.findAll();

            return users.stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all users", e);
            throw e;
        }
    }

    @Override
    public UserDTO findByUserid(int userid) {
        try {
            Optional<User> optionalUser = iUserRepo.findById(userid);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return userMapper.toUserDTO(user);
            }
            throw new EntityNotFoundException("User not found with id " + userid);

        } catch (RuntimeException e) {
            logger.error("Error occurred while retrieving user with ID: {}", userid, e);
            throw e;
        }
    }

    @Override
    public UserDTO editByUserId(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }
        if (userDTO.getUserId() == null) {
            throw new IllegalArgumentException("This user does not have ID");
        }
        Optional<User> existingUser = iUserRepo.findById(userDTO.getUserId());
        if (existingUser.isPresent()) {
            User user = userMapper.toUser(userDTO);
            User updatedUser = iUserRepo.save(user);
            return userMapper.toUserDTO(updatedUser);
        }
        throw new EntityNotFoundException("User not found with id " + userDTO.getUserId());
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }
        if (iUserRepo.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("User with this email already registered");
        }
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(Role.USER);
            User savedUser = iUserRepo.save(user);
            return userMapper.toUserDTO(savedUser);

        } catch (Exception e) {
            logger.error("Error creating user", e);
            throw e;
        }
    }

    @Override
    public boolean deleteUserByUserid(int userId) {
        User user = userUtils.getUserById(userId);

        if (user != null) {

            List<Group> groups = groupRepo.findByUserContaining(user);
            if (!groups.isEmpty())
                logger.info("list of groups " + groups);

            for (Group group : groups) {
                List<User> updatedUsers = group.getUser();
                updatedUsers.remove(user);
                group.setUser(updatedUsers);
            }
            iGroupRepo.saveAll(groups);

            List<Gift> gifts = iGiftRepo.findByCreatedBy(user);
            if (!gifts.isEmpty()) {
                try {
                    iGiftRepo.deleteByCreatedBy(user);
                } catch (Exception e) {
                    logger.error("Error deleting gifts owned by user with ID: {}", userId, e);
                    return false;
                }
            }
            try {
                iGroupRepo.deleteByOwner(user);

            } catch (Exception e) {
                logger.error("Error deleting groups where user is owner with ID: {}", userId, e);
                return false;
            }
            try {
                iUserRepo.deleteById(userId);
                return true;

            } catch (Exception e) {
                logger.error("Error deleting user with ID: {}", userId, e);
                return false;
            }
        } else {
            logger.error("Attempted to delete a user that does not exist with ID: {}", userId);
            throw new EntityNotFoundException("User not found with id " + userId);
        }
    }


    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByNameContaining(String nameText) {
        return iUserRepo.findByNameContainingIgnoreCase(nameText).stream()
                .map(userMapper::toUserDTO).collect(Collectors.toList());
    }


}
