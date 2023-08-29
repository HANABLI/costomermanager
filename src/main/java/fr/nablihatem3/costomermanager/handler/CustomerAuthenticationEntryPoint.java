package fr.nablihatem3.costomermanager.handler;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nablihatem3.costomermanager.domain.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 24/08/2023
 */

@Component
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.builder()
            .timeStamp(now().toString())
            .reason("You need to log in to access this ressource")
            .status(UNAUTHORIZED)
            .statusCode(UNAUTHORIZED.value())
            .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }
    
}
