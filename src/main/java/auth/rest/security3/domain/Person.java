package auth.rest.security3.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.nio.charset.StandardCharsets;

@Entry(base = "ou=users",
        objectClasses = { "inetOrgPerson","organizationalPerson","person", "top" })
@Getter
@Setter
public final class Person implements Persistable<Name> {
    @Id
    private Name dn;
    @Attribute(name="cn")
    private String fullname;
    @Attribute(name="sn")
    private String lastname;
    @Attribute(name = "userPassword", type = Attribute.Type.BINARY)
    private byte[] password;
    @Attribute(name="description")
    private String description;
    @Attribute(name="mail")
    private String mail;
    @Attribute(name="uid")
    private String uid;

    @Override
    public Name getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public void setPasswordEncoded(String password) {
        this.password = password.getBytes(StandardCharsets.UTF_8);
    }
}
