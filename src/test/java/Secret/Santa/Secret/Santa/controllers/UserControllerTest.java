package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.mappers.UserMapper;
import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.services.IUserService;
import Secret.Santa.Secret.Santa.services.impl.UserServiceImpl;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IUserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserUtils userUtils;

    @Test
    void getAllUsers() {
        List<UserDTO> mockUsers = Arrays.asList(new UserDTO(), new UserDTO());

        when(userService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<UserDTO>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUsers, responseEntity.getBody());
    }


    @Test
    void createUser() {
        UserDTO userDTO = new UserDTO();
        UserDTO mockUser = new UserDTO();
        when(userService.createUser(userDTO)).thenReturn(mockUser);

        ResponseEntity<UserDTO> responseEntity = userController.createUser(userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUser, responseEntity.getBody());
    }

//    @Test
//    @WithMockUser(username = "testUser", roles = {"USER"})
//    void getUserById() {
//        int userId = 1;
//        UserDTO mockUser = new UserDTO();
//        mockUser.setEmail("example@example.com");
//
//        // Mock Principal
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn("authenticatedEmail");
//
//        when(userService.findByUserid(userId)).thenReturn(mockUser);
//
//        ResponseEntity<UserDTO> responseEntity = userController.getUserById(userId, mockPrincipal);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(mockUser, responseEntity.getBody());
//    }


    //
//    @Test
//    void updateUser() {
//        int userId = 1;
//        UserDTO userDTO = new UserDTO();
//        User mockUser = new User();
//        when(userService.editByUserId(userDTO)).thenReturn(mockUser);
//
//        ResponseEntity<User> responseEntity = userController.updateUser(userDTO);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(mockUser, responseEntity.getBody());
//    }

    @Test
    void deleteUser() {
        int userId = 1;
        when(userService.deleteUserByUserid(userId)).thenReturn(true);

        ResponseEntity<String> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
