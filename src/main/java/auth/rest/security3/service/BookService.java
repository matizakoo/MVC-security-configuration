package auth.rest.security3.service;

import auth.rest.security3.domain.Book;
import auth.rest.security3.dto.BookDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface BookService {
    List<BookDTO> findAll();
    void saveAll(List<Book> books);
    void save(BookDTO bookDTO);
    Set<BookDTO> findByAuthorId(Integer id);
}
