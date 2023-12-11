package Secret.Santa.Secret.Santa.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.IUserService;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {
    @Mock
    private IUserRepo userRepository;
    @Mock
    private IUserService userService;

    @InjectMocks
    private UserUtils userUtils;

    @Test
    void getUserById_ValidId_ReturnsUser() {
        int userId = 1;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(expectedUser));

        User result = userUtils.getUserById(userId);

        verify(userRepository).findById(userId);
        assertSame(expectedUser, result);
    }

    @Test
    void getUserById_InvalidId_ThrowsException() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(SantaValidationException.class, () -> userUtils.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUsersInGroup() {

        Group mockGroup = new Group();

        User user1 = new User();
        user1.setName("User1");
        User user2 = new User();
        user2.setName("User2");
        User user3 = new User();
        user3.setName("User3");

        List<User> mockUsers = Arrays.asList(user1, user2, user3);

        when(userRepository.findByGroups(any(Group.class))).thenReturn(mockUsers);

        List<User> result = userUtils.getUsersInGroup(mockGroup);

        verify(userRepository, times(1)).findByGroups(mockGroup);

        assertNotNull(result);
        assertEquals(mockUsers.size(), result.size());

    }

    @Test
    void testGetUsersInGroupEmpty() {
        Group mockGroup = new Group();

        when(userRepository.findByGroups(any(Group.class))).thenReturn(new ArrayList<>());
        assertThrows(SantaValidationException.class, () -> userUtils.getUsersInGroup(mockGroup));
        verify(userRepository, times(1)).findByGroups(mockGroup);
    }
}