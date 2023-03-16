package auth.rest.security3.mapper;


import auth.rest.security3.domain.Book;
import auth.rest.security3.dto.BookDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface BookMapper {
    BookDTO bookToBookDto(Book book);
    Book bookDtoToBook(BookDTO book);
}
