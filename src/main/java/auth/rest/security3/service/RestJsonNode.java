package auth.rest.security3.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class RestJsonNode {
    public JsonNode getJsonNode(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        String requestBody = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(requestBody);
    }
}
