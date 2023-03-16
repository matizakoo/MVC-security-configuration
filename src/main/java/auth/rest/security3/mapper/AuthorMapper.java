package auth.rest.security3.mapper;

import auth.rest.security3.domain.Author;
import auth.rest.security3.dto.AuthorDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface AuthorMapper {
    AuthorDTO authorToAuthorDto(Author author);
    Author authorDtoToAuthor(AuthorDTO authorDTO);
}
