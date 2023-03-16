package auth.rest.security3.dto;

import auth.rest.security3.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Integer id;
    private String name;
    private Author author;
}
