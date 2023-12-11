package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.models.DTO.GenerateSantaDTO;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.services.IGenerateSantaService;
import Secret.Santa.Secret.Santa.services.validationUnits.UserUtils;
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

@RestController
@RequestMapping("/api/v1/generate_santa")
@RequiredArgsConstructor
public class GenerateSantaController {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSantaController.class);
    @Autowired
    private final IGenerateSantaService generateSantaService;
    private final UserUtils userUtils;

    @PostMapping("/create")
    public ResponseEntity<String> createGenerateSanta(@RequestBody GenerateSantaDTO generateSantaDTO) {

        GenerateSanta createdSanta = generateSantaService.createGenerateSanta(generateSantaDTO);
        if (createdSanta != null) {
            return ResponseEntity.ok("GenerateSanta created successfully with ID: " + createdSanta.getId());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create GenerateSanta");
        }
    }

    @GetMapping("/all_in_group/{groupId}")
    public List<GenerateSanta> getAllGenerateSantaByGroup(@PathVariable("groupId") Integer groupId) {

        return generateSantaService.getAllGenerateSantaByGroup(groupId);
    }

    @GetMapping("/santa_group/{santaId}")
    public GenerateSanta getGenerateSantaBySantaAndGroup(@PathVariable("santaId") Integer santaId,
                                                         @RequestParam Integer groupId, Principal principal) {
        String authenticatedEmail = principal.getName();
        if (userUtils.getUserById(santaId).getEmail().equals(authenticatedEmail)) {
            return generateSantaService.getGenerateSantaBySantaAndGroup(santaId, groupId);
        } else {
            throw new AccessDeniedException("Authenticated user does not have access to this user's groups");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenerateSantaBySantaId(@PathVariable("id") Integer id) {
        try {
            generateSantaService.deleteGenerateSantaById(id);
            return ResponseEntity.ok("GenerateSanta with ID " + id + " deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting GenerateSanta by ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<String> deleteGenerateSantaByGroup(@PathVariable("groupId") Integer groupId) {
        try {
            generateSantaService.deleteGenerateSantaByGroup(groupId);
            return ResponseEntity.ok("GenerateSanta entries for Group ID " + groupId + " deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting GenerateSanta entries for group ID: {}", groupId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteGenerateSantaByUser(@PathVariable("userId") Integer userId,
                                                            @RequestParam Integer groupId) {
        try {
            generateSantaService.deleteGenerateSantaByUser(userId, groupId);
            return ResponseEntity.ok("GenerateSanta entries for User ID " + userId + " in Group ID " + groupId + " deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting GenerateSanta entries for user ID: {} in group ID: {}", userId, groupId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/random/{groupId}")
    public ResponseEntity<String> generateRandomSanta(@PathVariable("groupId") Integer groupId) {

        generateSantaService.randomSantaGenerator(groupId);
        return ResponseEntity.ok("Random Santa pairs generated successfully for Group ID: " + groupId);

    }

}
