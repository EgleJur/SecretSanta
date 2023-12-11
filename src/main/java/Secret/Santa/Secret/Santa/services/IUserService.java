package Secret.Santa.Secret.Santa.services;

import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService  {
    List<UserDTO> getAllUsers();
    UserDTO findByUserid(int userid);
    UserDTO editByUserId(UserDTO lessorDTO);
    UserDTO createUser(UserDTO lessorDTO);

    User loadUserByEmail(String username);
    boolean deleteUserByUserid(int userid);
    List<UserDTO> getUsersByNameContaining(String nameText);


}