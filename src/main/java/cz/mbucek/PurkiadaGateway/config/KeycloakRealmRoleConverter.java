package cz.mbucek.PurkiadaGateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author matejbucek
 *
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {
    @SuppressWarnings("unchecked")
	@Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        final Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
        List<String> all = new ArrayList<String>();
        
        List <String> l = (List<String>)realmAccess.get("roles");
        all.addAll(l);
        
        Set<String> resources = resourceAccess.keySet();
        resources.forEach(res -> {
        	Map<String, Object> r = (Map<String, Object>)resourceAccess.get(res);
        	List<String> a = (List<String>)r.get("roles");
        	a.forEach(n -> {
        		all.add((res+"-"+n).replace("-", "_"));
        	});
        });
        
        Mono<List<SimpleGrantedAuthority>> mono =  Mono.just((all).stream()
                .map(roleName -> "ROLE_" + roleName) // prefix to map to a Spring Security "role"
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return mono.flatMapMany(Flux::fromIterable);
    }
}