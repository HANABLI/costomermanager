package fr.nablihatem3.costomermanager.resource;

import fr.nablihatem3.costomermanager.domain.HttpResponse;
import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.domain.UserPrincipal;
import fr.nablihatem3.costomermanager.dto.UserDTO;
import fr.nablihatem3.costomermanager.exception.ApiException;
import fr.nablihatem3.costomermanager.form.LoginForm;
import fr.nablihatem3.costomermanager.provider.TokenProvider;
import fr.nablihatem3.costomermanager.service.RoleService;
import fr.nablihatem3.costomermanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static fr.nablihatem3.costomermanager.dtomapper.UserDTOMapper.toUser;
import static fr.nablihatem3.costomermanager.utils.ExceptionUtils.processError;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final HttpServletRequest servletRequest;
    private final HttpServletResponse servletResponse;

    @GetMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        Authentication authentication = authenticate(loginForm.getEmail(), loginForm.getPassword());
        UserDTO userDTO = getAuthenticatedUserDTO(authentication);
        System.out.println(authentication);
        System.out.println(userDTO);
        System.out.println(((UserPrincipal) authentication.getPrincipal()).getUser());
        return userDTO.isUsingMfa() ? sendVerificationCode(userDTO) : sendResponse(userDTO);
    }

    private UserDTO getAuthenticatedUserDTO(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
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

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        UserDTO userDto = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user",userDto))
                        .message("Profile Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

/** Start Reset password controller's : when user is not logged in. */
    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> getResetPasswordUrl(@PathVariable("email") String email) {
        userService.resetPassword(email);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Email sent. Please check your email to reset your password.")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPasswordUrl(@PathVariable("key") String key) {
        UserDTO user = userService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Please enter a new password.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/resetpassword/{key}/{password}/{confirmPassword}")
    public ResponseEntity<HttpResponse> resetpassword(@PathVariable("key") String key, @PathVariable("password") String password,
                                                      @PathVariable("confirmPassword") String confirmPassword) {
        userService.renewPassword(key, password, confirmPassword);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Password has been reset successfully.")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
    }

    /** End Reset Password Controller's : when user is not login */

    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccountUrl(@PathVariable("key") String key) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message(userService.verifyAccountUrl(key).isEnabled() ? "Account already verified" : "Account verified" )
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if(isHeaderTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            UserDTO userDTO = userService.getUserByEmail(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(of("user", userDTO, "access_token", tokenProvider.createAccessToken(getUserPrinciple(userDTO)), "refresh_token", token))
                            .message("Token refresh")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } else {
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Refresh Token missing or invalid")
                            .developperMessage("Refresh Token missing or invalid")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());

        }
    }

    private boolean isHeaderTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTockenValid(
                        tokenProvider.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                        request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length())
                );
    }

    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build());
    }

//    @RequestMapping("/error")
//    public ResponseEntity<HttpResponse> handleError1(HttpServletRequest request) {
//        return new ResponseEntity<>(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
//                        .status(NOT_FOUND)
//                        .statusCode(NOT_FOUND.value())
//                        .build(), NOT_FOUND);
//    }

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
        return new UserPrincipal(toUser(userService.getUserByEmail(userDTO.getEmail())), roleService.getRoleByUserId(userDTO.getId()));
    }

    private ResponseEntity<HttpResponse> sendVerificationCode(UserDTO userDTO) {
        userService.sendVerificationCode(userDTO);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDTO))
                        .message("Verification code sent to" + userDTO.getPhone())
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private Authentication authenticate(String email, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(unauthenticated(email, password));
            return authentication;
        } catch (Exception exception) {
            processError(servletRequest, servletResponse, exception);
            throw new ApiException(exception.getMessage());
        }

    }

}
