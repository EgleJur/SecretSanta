package Secret.Santa.Secret.Santa.controllers;

import Secret.Santa.Secret.Santa.authentification.AuthenticationRequest;
import Secret.Santa.Secret.Santa.authentification.AuthenticationResponse;
import Secret.Santa.Secret.Santa.authentification.AuthenticationService;
import Secret.Santa.Secret.Santa.authentification.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;



    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username already exists"));
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        try{
            AuthenticationResponse response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e){
            log.warn(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Password is incorrect"));
        }
    }
}
