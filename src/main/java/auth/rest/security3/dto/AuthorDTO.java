package auth.rest.security3.dto;

import auth.rest.security3.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDTO {
    private Integer id;
    private String surname;
    private Set<Book> books;
}
