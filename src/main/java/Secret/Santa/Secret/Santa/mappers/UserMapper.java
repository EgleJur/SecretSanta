package Secret.Santa.Secret.Santa.mappers;

import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserDTO userDTO) {
        User user = new User();
//        if (userDTO.getUserId() != null) {
            user.setUserId(userDTO.getUserId());
//        }
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword((userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        return user;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
//        if (userDTO.getUserId() != null) {
        userDTO.setUserId(user.getUserId());
//        }
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword((user.getPassword()));
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
