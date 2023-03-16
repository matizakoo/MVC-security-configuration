package auth.rest.security3.service;


import auth.rest.security3.domain.Author;
import auth.rest.security3.dto.AuthorDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AuthorService {
    List<AuthorDTO> findAll();
    List<Author> findAllNoDto();
    void saveAll(List<Author> authors);
}
