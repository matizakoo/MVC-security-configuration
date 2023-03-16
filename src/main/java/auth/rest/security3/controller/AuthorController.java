package auth.rest.security3.controller;

import auth.rest.security3.domain.Author;
import auth.rest.security3.dto.AuthorDTO;
import auth.rest.security3.service.AuthorServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AuthorController.url)
@AllArgsConstructor
@Slf4j
public class AuthorController {
    public static final String url = "/user/author";
    private final AuthorServiceImpl authorService;
    //DTO - print relations between 2 columns, able to see books
    @GetMapping(value = "/dto")
    public List<AuthorDTO> authors(){
        return authorService.findAll();
    }

    @GetMapping(value = "/nodto")
    public List<Author> authorsNoDto(){
        return authorService.findAllNoDto();
    }
}
