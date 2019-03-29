package app.sagen.tidderfront;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {

    ROLE_USER(new SimpleGrantedAuthority("ROLE_USER")),
    ROLE_ADMIN(new SimpleGrantedAuthority("ROLE_ADMIN"));

    @Getter
    SimpleGrantedAuthority grantedAuthority;

    Role(SimpleGrantedAuthority grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }

}
