package fr.nablihatem3.costomermanager.resource;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

import java.net.URI;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.nablihatem3.costomermanager.domain.HttpResponse;
import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;
import fr.nablihatem3.costomermanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDto = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user",userDto))
                        .message("User created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
        
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
