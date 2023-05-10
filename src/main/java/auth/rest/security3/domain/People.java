package auth.rest.security3.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(base = "ou=people",
        objectClasses = { "users", "top" })
@Getter
@Setter
public class People implements Persistable<Name> {
    @Id
    private Name dn;

    @Attribute(name = "emailAddress")
    private String emailAddress;

    @Attribute(name = "emailContext")
    private String emailContext;

    @Attribute(name = "inMigrationProcess")
    private boolean inMigrationProcess;

    @Attribute(name = "context")
    private String context;

    @Attribute(name = "lastLoginDate")
    private String lastLoginDate;

    @Attribute(name = "lastBadLoginDate")
    private String lastBadLoginDate;

    @Attribute(name = "businessId")
    private String businessId;

    @Override
    public Name getId() {
        return dn;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public String toString() {
        return "People{" +
                "dn=" + dn +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailContext='" + emailContext + '\'' +
                ", inMigrationProcess=" + inMigrationProcess +
                ", context='" + context + '\'' +
                ", lastLoginDate=" + lastLoginDate +
                ", lastBadLoginDate=" + lastBadLoginDate +
                ", businessId='" + businessId + '\'' +
                '}';
    }
}
