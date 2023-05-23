package auth.rest.security3.config.ldap;

import auth.rest.security3.service.PersonService;
import auth.rest.security3.service.PersonServiceImpl;
import auth.rest.security3.service.Service2;
import auth.rest.security3.service.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories
public class LdapConfig {
    @Value("${ldap.user}")
    private String user;
    @Value("${ldap.dn.base}")
    private String base;
    @Value("${ldap.url}")
    private String url;
    @Value("${ldap.password}")
    private String password;

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.setBase(base);
        ldapContextSource.setUserDn(user);
        ldapContextSource.setPassword(password);
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
