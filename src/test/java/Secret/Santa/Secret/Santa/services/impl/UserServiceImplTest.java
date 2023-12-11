//package Secret.Santa.Secret.Santa.services.impl;
//
//import Secret.Santa.Secret.Santa.mappers.UserMapper;
//import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
//import Secret.Santa.Secret.Santa.models.Role;
//import Secret.Santa.Secret.Santa.models.User;
//import Secret.Santa.Secret.Santa.repos.IGiftRepo;
//import Secret.Santa.Secret.Santa.repos.IGroupRepo;
//import Secret.Santa.Secret.Santa.repos.IUserRepo;
//import Secret.Santa.Secret.Santa.services.impl.UserServiceImpl;
//import Secret.Santa.Secret.Santa.validationUnits.UserUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserServiceImplTest {
//
//    @Mock
//    private UserMapper userMapper;
//
//    @Mock
//    private IUserRepo iUserRepo;
//
//    @Mock
//    private IGiftRepo iGiftRepo;
//
//    @Mock
//    private UserUtils userUtils;
//
//    @Mock
//    private IGroupRepo iGroupRepo;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllUsers() {
//        // Arrange
//        List<User> users = new ArrayList<>();
//        when(iUserRepo.findAll()).thenReturn(users);
//        when(userMapper.toUserDTO(any())).thenReturn(new UserDTO());
//
//        // Act
//        List<UserDTO> result = userService.getAllUsers();
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testFindByUserId() {
//        // Arrange
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUserId(1);
//        when(iUserRepo.findById(anyInt())).thenReturn(Optional.of(new User()));
//        when(userMapper.toUserDTO(any())).thenReturn(userDTO);
//
//        // Act
//        UserDTO result = userService.findByUserid(1);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.getUserId());
//    }
//
//    // Add more test cases for other methods...
//
//}
