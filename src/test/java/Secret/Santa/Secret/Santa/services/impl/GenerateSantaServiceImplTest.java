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

        when(groupUtils.getGroupById(groupId)).thenThrow(new SantaValidationException("Group does not exist", "id",
                "Group not found", String.valueOf(groupId)));

        assertThrows(SantaValidationException.class, () -> generateSantaService.getAllGenerateSantaByGroup(groupId));
        verify(groupUtils).getGroupById(groupId);

        verifyNoInteractions(generateSantaRepository);
    }


    @Test
    void testGetGenerateSantaBySantaAndGroup() {
        int santaId = 1;
        int groupId = 10;

        User santa = new User();
        santa.setUserId(santaId);

        Group group = new Group();
        group.setGroupId(groupId);

        GenerateSanta generatedSanta = new GenerateSanta();

        when(userUtils.getUserById(santaId)).thenReturn(santa);
        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        when(generateSantaUtils.getBySantaAndGroup(santa, group)).thenReturn(generatedSanta);

        GenerateSanta result = generateSantaService.getGenerateSantaBySantaAndGroup(santaId, groupId);

        verify(userUtils, times(1)).getUserById(santaId);
        verify(groupUtils, times(1)).getGroupById(groupId);
        verify(generateSantaUtils, times(1)).getBySantaAndGroup(santa, group);
        assertEquals(generatedSanta, result);
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

        doThrow(new RuntimeException("Deletion failed")).when(generateSantaRepository).deleteByGroup(group);

        try {
            generateSantaService.deleteGenerateSantaByGroup(groupId);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {

            assertEquals("Deletion failed", e.getMessage());
        }


        verify(generateSantaRepository).deleteByGroup(group);
    }

    @Test
    public void testDeleteGenerateSantaByUser() {
        int userId = 1;
        int groupId = 1;
        User user = new User();
        Group group = new Group();

        GenerateSanta generateSanta = Mockito.mock(GenerateSanta.class);

        generateSanta.setRecipient(user);

        when(userUtils.getUserById(userId)).thenReturn(user);
        when(groupUtils.getGroupById(groupId)).thenReturn(group);
        when(generateSantaUtils.getBySantaAndGroup(user, group)).thenReturn(generateSanta);
        when(generateSantaUtils.getByUserAndGroup(user, group)).thenReturn(generateSanta);

        generateSantaService.deleteGenerateSantaByUser(userId, groupId);

        verify(generateSantaRepository).delete(generateSanta);

        verify(generateSanta, times(1)).setRecipient(null);

    }


}