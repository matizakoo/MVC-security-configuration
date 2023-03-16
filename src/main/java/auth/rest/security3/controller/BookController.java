package auth.rest.security3.controller;

import auth.rest.security3.dto.BookDTO;
import auth.rest.security3.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(BookController.url)
@AllArgsConstructor
@Slf4j
public class BookController {
    public static final String url = "/user/book";
    private final BookService bookService;

    @GetMapping(value = "/dto")
    public List<BookDTO> getBooks(){
        return bookService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Set<BookDTO> getBooksByAuthorId(@PathVariable("id") Integer id){
        return bookService.findByAuthorId(id);
    }
}
