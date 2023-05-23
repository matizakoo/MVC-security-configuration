package auth.rest.security3.config.ldap;

import auth.rest.security3.controller.LdapController;
import auth.rest.security3.domain.AuthAttack;
import auth.rest.security3.dto.UserCredentialsDTO;
import auth.rest.security3.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class JwtLdapAuthorizationAttackFilter extends OncePerRequestFilter {
    @Autowired
    private AttackService attackService;
    @Autowired
    private PersonService personService;
    @Autowired
    private JsonNodeService jsonNodeService;

    @Value("${logging.limit}")
    private Integer limit;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().startsWith(LdapController.url + "/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest requestWrapper = new MultiReadHttpServletRequest(request);
        JsonNode jsonNode = jsonNodeService.getObjectTree(requestWrapper);

        try {
            if(personService.findPeopleByUsernameWuthCorrectCredentials(
                    jsonNode.get("username").asText(),
                    jsonNode.get("password").asText()) != null) {
                filterChain.doFilter(requestWrapper, response);
                return;
            }
        } catch (EmptyResultDataAccessException e) {
            System.out.println("exception");
        }


        System.out.println(attackService.findLast10Attempts(request.getRemoteAddr()).size());
        if(attackService.findLast10Attempts(request.getRemoteAddr()).size() == limit - 1) {
            return;
        }

        attackService.save(AuthAttack
                .builder()
                .ip(request.getRemoteAddr())
                .attemptTime(LocalDateTime.now())
                .build());


        filterChain.doFilter(request, response);
    }
}
