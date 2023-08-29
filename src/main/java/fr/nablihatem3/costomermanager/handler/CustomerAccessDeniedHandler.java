package fr.nablihatem3.costomermanager.handler;

import java.io.IOException;
import java.io.OutputStream;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nablihatem3.costomermanager.domain.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static java.time.LocalDateTime.now;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 24/08/2023
 */

@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.builder()
        .timeStamp(now().toString())
        .reason("You don't have enough permission")
        .status(FORBIDDEN)
        .statusCode(FORBIDDEN.value())
        .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }
}
