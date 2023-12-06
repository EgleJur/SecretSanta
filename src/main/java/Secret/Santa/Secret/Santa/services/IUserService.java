package Secret.Santa.Secret.Santa.services;

import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UserDTO findByUserid(int userid);
    UserDTO editByUserId(UserDTO lessorDTO);
    UserDTO createUser(UserDTO lessorDTO);

    boolean deleteUserByUserid(int userid);
}
