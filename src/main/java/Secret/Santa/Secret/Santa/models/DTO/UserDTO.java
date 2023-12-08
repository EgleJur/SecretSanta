package Secret.Santa.Secret.Santa.models.DTO;

import Secret.Santa.Secret.Santa.authentification.AuthenticationRequest;
import Secret.Santa.Secret.Santa.authentification.RegisterRequest;
import Secret.Santa.Secret.Santa.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    @NotBlank(message = "Name is required")
    private String name;
    @Email
    private String email;
    private String password;
    private Role role;

    public static UserDTO fromRegisterRequest(RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(request.getName());
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(request.getPassword());
        userDTO.setRole(request.getRole());
        return userDTO;
    }
    public static UserDTO fromAuthenticationRequest(AuthenticationRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(request.getEmail()); // TODO check if this is right
        userDTO.setPassword(request.getPassword());
        return userDTO;
    }
}
