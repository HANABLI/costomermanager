package fr.nablihatem3.costomermanager.resource;

import fr.nablihatem3.costomermanager.domain.HttpResponse;
import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.domain.UserPrincipal;
import fr.nablihatem3.costomermanager.dto.UserDTO;
import fr.nablihatem3.costomermanager.form.LoginForm;
import fr.nablihatem3.costomermanager.provider.TokenProvider;
import fr.nablihatem3.costomermanager.service.RoleService;
import fr.nablihatem3.costomermanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static fr.nablihatem3.costomermanager.dtomapper.UserDTOMapper.toUser;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @GetMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        UserDTO userDTO = userService.getUserByEmail(loginForm.getEmail());
        return userDTO.isUsingMfa() ? sendVerificationCode(userDTO) : sendResponse(userDTO);
    }

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

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        UserDTO userDto = userService.verifyCode(email, code);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDto, "access_token", tokenProvider.createAccessToken(getUserPrinciple(userDto)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrinciple(userDto))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDto) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDto, "access_token", tokenProvider.createAccessToken(getUserPrinciple(userDto)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrinciple(userDto))))
                        .message("Login Success")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private UserPrincipal getUserPrinciple(UserDTO userDTO) {
        return new UserPrincipal(toUser(userService.getUserByEmail(userDTO.getEmail())), roleService.getRoleByUserId(userDTO.getId()).getPermission());
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO userDTO) {
        userService.sendVerificvationCode(userDTO);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Verification code sent to" + userDTO.getPhone())
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

}
