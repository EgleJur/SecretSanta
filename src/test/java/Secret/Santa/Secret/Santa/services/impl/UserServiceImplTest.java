package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGiftRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.mappers.UserMapper;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
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
class UserServiceImplTest {

    @Mock
    private IUserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUtils userUtils;

    @Mock
    private IGiftRepo giftRepo;

    @Mock
    private IGroupRepo groupRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers() {
        User user1 = new User(1, "Alice", "alice@example.com", "password", null);
        User user2 = new User(2, "Bob", "bob@example.com", "password", null);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepo.findAll()).thenReturn(users);
        when(userMapper.toUserDTO(any(User.class))).thenAnswer(i -> new UserDTO(i.getArgument(0, User.class).getUserId(), i.getArgument(0, User.class).getName(), null, null, null));

        List<UserDTO> userDTOs = userService.getAllUsers();

        assertNotNull(userDTOs);
        assertEquals(2, userDTOs.size());
        verify(userRepo).findAll();
        verify(userMapper, times(2)).toUserDTO(any(User.class));
    }

    @Test
    void findByUserid() {
        int userId = 1;
        User user = new User(userId, "Alice", "alice@example.com", "password", null);
        UserDTO userDTO = new UserDTO(userId, "Alice", null, null, null);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.findByUserid(userId);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userRepo).findById(userId);
        verify(userMapper).toUserDTO(user);
    }

    @Test
    void findByUserid_UserNotFound() {
        int userId = 1;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findByUserid(userId));
    }

    @Test
    void editByUserId() {
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setName("New Name");
        userDTO.setPassword("password");
        userDTO.setEmail("email@example.com");

        User user = new User();
        user.setUserId(userId);
        user.setName("Old Name");
        user.setPassword("password");
        user.setEmail("email@example.com");

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(user);
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.editByUserId(userDTO);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void createUser() {
        UserDTO userDTO = new UserDTO(null, "Charlie", "charlie@example.com", "password", null);
        User savedUser = new User(3, "Charlie", "charlie@example.com", "password", null);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserDTO(savedUser)).thenReturn(new UserDTO(3, "Charlie", "charlie@example.com", "password", null));

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals("Charlie", result.getName());
        verify(userRepo).save(any(User.class));
        verify(userMapper).toUserDTO(any(User.class));
    }

    @Test
    void deleteUserByUserid() {
        int userId = 1;
        User mockUser = new User();
        when(userUtils.getUserById(userId)).thenReturn(mockUser);
        when(groupRepo.findByUserContaining(mockUser)).thenReturn(List.of());
        when(giftRepo.findByCreatedBy(mockUser)).thenReturn(List.of());
        doNothing().when(userRepo).deleteById(userId);

        assertTrue(userService.deleteUserByUserid(userId));

        verify(userRepo).deleteById(userId);
    }

    @Test
    void deleteUserByUserid_UserNotFound() {
        int userId = 1;
        when(userUtils.getUserById(userId)).thenThrow(new EntityNotFoundException("User not found with id " + userId));

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserByUserid(userId));
    }

    @Test
    void getUsersByNameContaining() {
        String name = "Al";
        List<User> users = Arrays.asList(new User(1, "Alice", "alice@example.com", "password", null));
        when(userRepo.findByNameContainingIgnoreCase(name)).thenReturn(users);
        when(userMapper.toUserDTO(any(User.class))).thenReturn(new UserDTO(1, "Alice", "alice@example.com", "password", null));

        List<UserDTO> result = userService.getUsersByNameContaining(name);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(userRepo).findByNameContainingIgnoreCase(name);
        verify(userMapper, times(1)).toUserDTO(any(User.class));
    }
}
