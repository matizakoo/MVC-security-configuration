package auth.rest.security3.config.ldap;

import auth.rest.security3.domain.Person;
import auth.rest.security3.repository.PersonRepository;
import auth.rest.security3.repository.PersonRepositoryAdditional;
import auth.rest.security3.service.PersonService;
import auth.rest.security3.service.PersonServiceImpl;
import auth.rest.security3.service.Service2;
import auth.rest.security3.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.ldap.LdapPasswordComparisonAuthenticationManagerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;

@Configuration
@EnableLdapRepositories
public class LdapConfig {
    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://localhost:10389");
        ldapContextSource.setBase("dc=example,dc=com");
        ldapContextSource.setUserDn("uid=admin,ou=system");
        ldapContextSource.setPassword("secret");
        return ldapContextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(ldapContextSource());
    }

    @Bean
    public PersonService personRepositoryAdditional() {
        return new PersonServiceImpl();
    }

    @Bean
    public Service2 service2() {
        return new ServiceImpl();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid=admin,ou=system")
                .groupSearchBase("ou=people")
                .contextSource()
                .url("ldap://localhost:10389/dc=example,dc=com")
                .and()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordCompare()
                .passwordAttribute("userPassword");
    }
}
