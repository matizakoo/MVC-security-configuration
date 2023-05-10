package auth.rest.security3.config.ldap;

import auth.rest.security3.service.PersonService;
import auth.rest.security3.service.PersonServiceImpl;
import auth.rest.security3.service.Service2;
import auth.rest.security3.service.ServiceImpl;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories
public class LdapConfig {
//    @Bean
//    public LdapContextSource ldapContextSource() {
//        LdapContextSource ldapContextSource = new LdapContextSource();
//        ldapContextSource.setUrl("ldap://localhost:10389");
//        ldapContextSource.setBase("dc=example,dc=com");
//        ldapContextSource.setUserDn("uid=admin,ou=system");
//        ldapContextSource.setPassword("secret");
//        return ldapContextSource;
//    }

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://10.5.110.200:4260");
        ldapContextSource.setBase("dc=sso,dc=so");
        ldapContextSource.setUserDn("cn=Directory Manager");
        ldapContextSource.setPassword("password123");
        return ldapContextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() { return new LdapTemplate(ldapContextSource()); }

    @Bean
    public PersonService personRepositoryAdditional() {
        return new PersonServiceImpl();
    }

    @Bean
    public Service2 service2() {
        return new ServiceImpl();
    }
}
