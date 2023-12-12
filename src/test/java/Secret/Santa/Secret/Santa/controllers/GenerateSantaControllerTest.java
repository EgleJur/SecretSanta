package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.models.DTO.GenerateSantaDTO;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.services.IGenerateSantaService;

import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateSantaControllerTest {

    @Mock
    private IGenerateSantaService generateSantaService;

    @InjectMocks
    private GenerateSantaController generateSantaController;

    @Mock
    private UserUtils userUtils;

    @Test
    void testCreateGenerateSanta() {
        User santa = new User();
        User recipient = new User();
        Group group = new Group();
        Integer id = 1;

        GenerateSantaDTO generateSantaDTO = new GenerateSantaDTO(id, group, santa, recipient);

        GenerateSanta createdSanta = new GenerateSanta();
        createdSanta.setGroup(group);
        createdSanta.setSanta(santa);
        createdSanta.setRecipient(recipient);

        when(generateSantaService.createGenerateSanta(any(GenerateSantaDTO.class))).thenReturn(createdSanta);

        ResponseEntity<String> response = generateSantaController.createGenerateSanta(generateSantaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("GenerateSanta created successfully with ID: " + createdSanta.getId(), response.getBody());

        verify(generateSantaService, times(1)).createGenerateSanta(any(GenerateSantaDTO.class));
    }

    @Test
    void testCreateGenerateSantaFailure() {

        GenerateSantaDTO generateSantaDTO = new GenerateSantaDTO();

        when(generateSantaService.createGenerateSanta(generateSantaDTO)).thenReturn(null);

        ResponseEntity<String> response = generateSantaController.createGenerateSanta(generateSantaDTO);

        assertEquals("Failed to create GenerateSanta", response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(generateSantaService, times(1)).createGenerateSanta(generateSantaDTO);
    }

    @Test
    void testGetAllGenerateSantaByGroup() {
        int groupId = 1;
        GenerateSanta generateSanta1 = new GenerateSanta();
        GenerateSanta generateSanta2 = new GenerateSanta();
        GenerateSanta generateSanta3 = new GenerateSanta();

        List<GenerateSanta> santaList = new ArrayList<>();

        santaList.add(generateSanta1);
        santaList.add(generateSanta2);
        santaList.add(generateSanta3);

        when(generateSantaService.getAllGenerateSantaByGroup(groupId)).thenReturn(santaList);

        List<GenerateSanta> result = generateSantaController.getAllGenerateSantaByGroup(groupId);

        assertEquals(santaList, result);

        verify(generateSantaService, times(1)).getAllGenerateSantaByGroup(groupId);
    }

    @Test
    void testGetGenerateSantaBySantaAndGroup() {
        int santaId = 1;
        int groupId = 10;
        String authenticatedEmail = "authenticated@example.com";

        GenerateSanta mockGeneratedSanta = new GenerateSanta();

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(authenticatedEmail);

        User mockUser = new User();
        mockUser.setEmail(authenticatedEmail);

        when(userUtils.getUserById(santaId)).thenReturn(mockUser);
        when(generateSantaService.getGenerateSantaBySantaAndGroup(santaId, groupId)).thenReturn(mockGeneratedSanta);

        GenerateSanta result = generateSantaController.getGenerateSantaBySantaAndGroup(santaId, groupId, principal);

        assertEquals(mockGeneratedSanta, result);
    }

    @Test
    void testDeleteGenerateSantaBySantaId() {
        int id = 1;
        doNothing().when(generateSantaService).deleteGenerateSantaById(id);

        ResponseEntity<String> response = generateSantaController.deleteGenerateSantaBySantaId(id);

        assertEquals("GenerateSanta with ID " + id + " deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check status
        verify(generateSantaService, times(1)).deleteGenerateSantaById(id);
    }

    @Test
    void deleteGenerateSantaByGroup() {
        int id = 1;
        doNothing().when(generateSantaService).deleteGenerateSantaByGroup(id);

        ResponseEntity<String> response = generateSantaController.deleteGenerateSantaByGroup(id);

        assertEquals("GenerateSanta entries for Group ID " + id + " deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check status
        verify(generateSantaService, times(1)).deleteGenerateSantaByGroup(id);

    }

    @Test
    void deleteGenerateSantaByUser() {
        int userId = 1;
        int groupId = 1;
        doNothing().when(generateSantaService).deleteGenerateSantaByUser(userId, groupId);

        ResponseEntity<String> response = generateSantaController.deleteGenerateSantaByUser(userId, groupId);

        assertEquals("GenerateSanta entries for User ID " + userId + " in Group ID " + groupId + " deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Check status
        verify(generateSantaService, times(1)).deleteGenerateSantaByUser(userId, groupId);
    }

    @Test
    void testGenerateRandomSanta() {
        int groupId = 1;

        doNothing().when(generateSantaService).randomSantaGenerator(groupId);

        ResponseEntity<String> response = generateSantaController.generateRandomSanta(groupId);

        assertEquals("Random Santa pairs generated successfully for Group ID: " + groupId, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(generateSantaService, times(1)).randomSantaGenerator(groupId);
    }
}