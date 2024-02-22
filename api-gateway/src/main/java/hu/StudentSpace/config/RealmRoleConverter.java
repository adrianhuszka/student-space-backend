package hu.StudentSpace.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class RealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Object realmAccessObject = jwt.getClaims().get("realm_access");
        if (realmAccessObject instanceof Map) {
            Map<String, ?> realmAccess = (Map<String, ?>) realmAccessObject;
            Object rolesObject = realmAccess.get("roles");
            if (rolesObject instanceof List<?> roles) {
                return roles.stream()
                        .filter(String.class::isInstance) // Filter out non-String elements
                        .map(String.class::cast) // Cast each element to String
                        .map(roleName -> "ROLE_" + roleName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        }
        // Handle invalid or missing data gracefully
        return Collections.emptyList();
    }
}