package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.mappers.UserMapper;
import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.Role;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.impl.UserServiceImpl;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private IUserRepo iUserRepo;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers() {
    User user1 = new User(1, "Tom Jackson", "tom@example.com", "password", Role.USER);
    User user2 = new User(2, "John Warner", "john@example.com", "password", Role.USER);
    List<User> mockUsers = Arrays.asList(user1, user2);

    when(iUserRepo.findAll()).thenReturn(mockUsers);

    when(userMapper.toUserDTO(user1)).thenReturn(new UserDTO(1, "Tom Jackson", "tom@example.com", "password", Role.USER));
    when(userMapper.toUserDTO(user2)).thenReturn(new UserDTO(2, "John Warner", "john@example.com","password", Role.USER));

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("authenticatedUser");
    SecurityContextHolder.getContext().setAuthentication(authentication);

    List<UserDTO> result = userService.getAllUsers();

    assertEquals(2, result.size());
    assertEquals("Tom Jackson", result.get(0).getName());
    assertEquals("john@example.com", result.get(1).getEmail());
    assertEquals("password", result.get(1).getPassword());
    assertEquals(Role.USER, result.get(1).getRole());

    verify(iUserRepo, times(1)).findAll();

    SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    void getAllUsers_EmptyList() {
        when(iUserRepo.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserid() {
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        User userEntity = new User();

        when(iUserRepo.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDTO(userEntity)).thenReturn(userDTO);

        UserDTO result = userService.findByUserid(userId);

        assertEquals(userDTO, result);
    }

    @Test
    void findByUserid_UserNotFound() {
        int userId = 1;

        when(iUserRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findByUserid(userId));
    }

//    @Test
//    void editByUserId() {
//        int userId = 1;
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("NewName");
//
//        User existingUser = new User();
//        existingUser.setUserId(userId);
//        existingUser.setName("OldName");
//
//        when(iUserRepo.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(iUserRepo.save(existingUser)).thenReturn(existingUser);
//
//        User result = userService.editByUserId(userDTO, userId);
//
//        assertEquals(userDTO.getName(), result.getName());
//    }

    @Test
    void editByUserId() {
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setName("Samantha");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setName("Mary");


        when(iUserRepo.findById(userId)).thenReturn(Optional.of(existingUser));

        when(iUserRepo.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setName(userDTO.getName());
            return savedUser;
        });

        UserDTO result = userService.editByUserId(userDTO);

//        assertNotNull(result, "The result should not be null");
        assertEquals(userDTO.getName(), result.getName());
    }

    @Test
    void createUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Benas");
        userDTO.setEmail("Benas@gmail.com");
        userDTO.setPassword("password");

        User expectedUser = new User();
        expectedUser.setName(userDTO.getName());
        expectedUser.setEmail(userDTO.getEmail());
        expectedUser.setPassword(userDTO.getPassword());

        when(iUserRepo.save(any())).thenReturn(expectedUser);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result, "The result should not be null");
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPassword(), result.getPassword());

        verify(iUserRepo, times(1)).save(argThat(savedUser -> {
            return Objects.equals(userDTO.getName(), savedUser.getName())
                    && Objects.equals(userDTO.getEmail(), savedUser.getEmail())
                    && Objects.equals(userDTO.getPassword(), savedUser.getPassword());
        }));
    }


//    @Test
//    void deleteUserByUserid() {
//        int userId = 1;
//
//        boolean result = userService.deleteUserByUserid(userId);
//
//        assertFalse(result);
//    }

    @Test
    void deleteUserByUserid() {
        int userId = 1;

        // Mock the userUtils to throw SantaValidationException when getUserById is called with the specified ID
        when(userUtils.getUserById(userId))
                .thenThrow(new SantaValidationException("User does not exist", "id",
                        "User not found", String.valueOf(userId)));

        // Call the method
        boolean result = userService.deleteUserByUserid(userId);

        // Verify the expected behavior
        assertFalse(result, "The result should be false");
    }





    @Test
    void deleteUserByUserid_UserNotFound() {
        int userId = 1;
        when(iUserRepo.existsById(userId)).thenReturn(false);

        boolean result = userService.deleteUserByUserid(userId);

        assertFalse(result);
    }

    void deleteUserByUserid_NegativeUserId() {
        int userId = -1;

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserByUserid(userId));
    }
}
