package Secret.Santa.Secret.Santa.authentification;

import Secret.Santa.Secret.Santa.models.DTO.UserDTO;
import Secret.Santa.Secret.Santa.jwt.JwtService;
import Secret.Santa.Secret.Santa.models.Role;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IUserRepo;
import Secret.Santa.Secret.Santa.services.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final IUserRepo userRepo;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        UserDTO userDTO = UserDTO.fromRegisterRequest(request);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        var savedUser = userService.createUser(userDTO);
        var jwtToken = jwtService.generateToken(savedUser.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        UserDTO userDTO = UserDTO.fromAuthenticationRequest(request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );

        var user = userRepo.findByEmail(userDTO.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user.getUsername());

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

}
