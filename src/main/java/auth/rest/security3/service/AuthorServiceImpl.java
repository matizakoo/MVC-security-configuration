package auth.rest.security3.service;


import auth.rest.security3.domain.Author;
import auth.rest.security3.dto.AuthorDTO;
import auth.rest.security3.mapper.AuthorMapper;
import auth.rest.security3.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::authorToAuthorDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> findAllNoDto(){
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public void saveAll(List<Author> authors) {
        authorRepository.saveAll(authors);
    }
}
