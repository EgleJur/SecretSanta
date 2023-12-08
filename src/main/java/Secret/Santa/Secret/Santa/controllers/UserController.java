package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.IUserService;
import Secret.Santa.Secret.Santa.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserRepo iUserRepo;
    @Autowired
    private IUserService iUserService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        try {
            List<UserDTO> userDTOs = iUserService.getAllUsers();
            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            logger.error("Error retrieving all users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {

        try {
            UserDTO createdUserDTO = iUserService.createUser(userDTO);
            return ResponseEntity.ok(createdUserDTO);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userid}")
    public ResponseEntity<UserDTO> getUserById(@Valid
                                            @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                            @PathVariable int userid) {

        try {
            UserDTO userDTO = iUserService.findByUserid(userid);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            logger.error("Error retrieving user with ID: {}", userid, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO userDTO) {

        try {
            UserDTO user = iUserService.editByUserId(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity<String> deleteUser(@Valid @Min(value = 1, message = "ID must be a non-negative integer and greater than 0")
                                             @PathVariable int userid) {
        try {
            if (Boolean.TRUE.equals(iUserService.deleteUserByUserid(userid))) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().body("Delete failed");
            }
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", userid, e);
            return ResponseEntity.internalServerError().body("Error occurred while deleting user");
        }
    }

//    @GetMapping(path = "name-filter/{nameText}")
//    @ResponseBody
//    public List<UserDTO> getUsersByNameContaining(@PathVariable String nameText) {
//        return iUserService.getUsersByNameContaining(nameText);
//    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsersByName(@RequestParam String name) {
        List<UserDTO> matchingUsers = iUserService.getUsersByNameContaining(name);
        return ok(matchingUsers);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Integer> getUserByUsername(@Valid
                                                    @PathVariable(name = "username") String username) {

        User user = iUserService.loadUserByEmail(username);
        return ResponseEntity.ok(user.getUserId());
    }


}
