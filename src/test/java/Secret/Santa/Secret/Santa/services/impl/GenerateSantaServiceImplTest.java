package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.DTO.GenerateSantaDTO;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGenerateSantaRepo;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.services.validationUnits.GenerateSantaUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateSantaServiceImplTest {

    @Mock
    private IGenerateSantaRepo generateSantaRepository;
    @Mock
    private IGroupRepo groupRepository;

    @Mock
    private GenerateSantaUtils generateSantaUtils;

    @Mock
    private GroupUtils groupUtils;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private GenerateSantaServiceImpl generateSantaService;

    @Mock
    private Logger logger;

    @Test
    void getAllGenerateSantaByGroup() {
        int groupId = 1;
        Group group = new Group();
        when(groupUtils.getGroupById(groupId)).thenReturn(group);

        List<GenerateSanta> expectedGenerateSanta = new ArrayList<>();
        when(generateSantaRepository.findByGroup(group)).thenReturn(expectedGenerateSanta);

        List<GenerateSanta> actualGenerateSanta = generateSantaService.getAllGenerateSantaByGroup(groupId);

        verify(groupUtils).getGroupById(groupId);
        verify(generateSantaRepository).findByGroup(group);
    }

    @Test
    void createGenerateSanta() {
        GenerateSantaDTO generateSantaDTO = new GenerateSantaDTO();
        GenerateSanta generatedSanta = new GenerateSanta();
        when(generateSantaRepository.save(any(GenerateSanta.class))).thenReturn(generatedSanta);

        GenerateSanta result = generateSantaService.createGenerateSanta(generateSantaDTO);

        verify(generateSantaRepository).save(any(GenerateSanta.class));
    }

    @Test
    void getAllGenerateSantaByGroup_Success() {
        int groupId = 1;
        Group group = new Group();
        // Setup group mocks if needed

        List<GenerateSanta> expectedGenerateSantaList = new ArrayList<>();
        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        when(generateSantaRepository.findByGroup(group)).thenReturn(expectedGenerateSantaList);

        List<GenerateSanta> actualGenerateSantaList = generateSantaService.getAllGenerateSantaByGroup(groupId);

        assertNotNull(actualGenerateSantaList);
        assertSame(expectedGenerateSantaList, actualGenerateSantaList);
    }

    @Test
    void getAllGenerateSantaByGroup_GroupNotFound() {
        int groupId = 1;
        // Simulate the group not being found
        when(groupUtils.getGroupById(groupId)).thenThrow(new SantaValidationException("Group does not exist", "id",
                "Group not found", String.valueOf(groupId)));

        assertThrows(SantaValidationException.class, () -> generateSantaService.getAllGenerateSantaByGroup(groupId));
        verify(groupUtils).getGroupById(groupId);
        verifyNoInteractions(generateSantaRepository); // Make sure the repository was not called
    }


    @Test
    void getGenerateSantaBySantaAndGroup_Success() {
        //can't make this because there is
        // @Setter(value = AccessLevel.NONE)
        //    private int groupId;
    }


    @Test
    void deleteGenerateSantaById() {
        int generateSantaId = 1;
        generateSantaService.deleteGenerateSantaById(generateSantaId);

        verify(generateSantaRepository).deleteById(generateSantaId);
    }

    @Test
    public void testDeleteGenerateSantaByGroup() {
        int groupId = 1;
        Group group = new Group();
        when(groupUtils.getGroupById(groupId)).thenReturn(group);

        generateSantaService.deleteGenerateSantaByGroup(groupId);

        verify(generateSantaRepository).deleteByGroup(group);
    }

    @Test
    public void testDeleteGenerateSantaByGroup_Failure() {
        int groupId = 1;
        Group group = new Group();
        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        // Assuming deletion fails and throws an exception
        doThrow(new RuntimeException("Deletion failed")).when(generateSantaRepository).deleteByGroup(group);

        // Execute the method and handle the failure
        try {
            generateSantaService.deleteGenerateSantaByGroup(groupId);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            // Verify that the exception was thrown
            assertEquals("Deletion failed", e.getMessage());
        }

        // Verify that deleteByGroup was called
        verify(generateSantaRepository).deleteByGroup(group);
    }

    @Test
    public void testDeleteGenerateSantaByUser() {
        int userId = 1;
        int groupId = 1;
        User user = new User();
        Group group = new Group();

        // Mock GenerateSanta
        GenerateSanta generateSanta = Mockito.mock(GenerateSanta.class);
        generateSanta.setRecipient(user); // Assuming GenerateSanta has a Recipient property

        when(userUtils.getUserById(userId)).thenReturn(user);
        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        when(generateSantaUtils.getBySantaAndGroup(user, group)).thenReturn(generateSanta);
        when(generateSantaUtils.getByUserAndGroup(user, group)).thenReturn(generateSanta);

        generateSantaService.deleteGenerateSantaByUser(userId, groupId);

        verify(generateSantaRepository).delete(generateSanta);
        verify(generateSanta, times(1)).setRecipient(null); // Verify that setRecipient was called with null
    }


    @Test
    void randomSantaGenerator() {
    }

    @Test
    public void testRandomSantaGenerator() {
        int groupId = 1;
        Group group = new Group();

        User user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        User user3 = new User();
        user3.setName("User3");
        user3.setEmail("user3@example.com");
        user3.setPassword("password3");


        List<User> usersInGroup = Arrays.asList(user1, user2, user3);

        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        when(userUtils.getUsersInGroup(group)).thenReturn(usersInGroup);

        // Mocking generateSantaUtils.alreadyPaired method to return false always
        when(generateSantaUtils.alreadyPaired(any(User.class), any(User.class))).thenReturn(false);

        generateSantaService.randomSantaGenerator(groupId);

        // Verify that generateSantaRepository.save is called for each user
        verify(generateSantaRepository, times(usersInGroup.size())).save(any(GenerateSanta.class));
    }

}