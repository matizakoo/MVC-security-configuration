package auth.rest.security3.service;

import auth.rest.security3.domain.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImpl implements Service2{
    @Override
    public String create(Person person) {
        return null;
    }

    @Override
    public List<Person> findAll() {
        return null;
    }
}
