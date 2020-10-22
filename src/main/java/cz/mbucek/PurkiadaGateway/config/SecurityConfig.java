package cz.mbucek.PurkiadaGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

/**
 * @author matejbucek
 *
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig{
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
		    ReactiveClientRegistrationRepository clientRegistrationRepository) {
//		http.oauth2Login();
//		http.logout(logout -> logout.logoutSuccessHandler(
//			    new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository)));
//		http.headers().frameOptions().mode(Mode.SAMEORIGIN);
//		http.authorizeExchange()
//		.pathMatchers("/actuator/info", "/actuator/health").permitAll()
//		.pathMatchers("/actuator/**").hasAnyRole("ACTUATOR")
//		.pathMatchers("/services/admin/**").authenticated()
//		.anyExchange().permitAll();
//		http.csrf().disable();
		http
	      .authorizeExchange()
	      .pathMatchers("/v1.0/**").permitAll()
	      .pathMatchers("/services/admin/**").permitAll()
	      .pathMatchers("/actuator/**").hasAnyRole("spring_gateway_actuator","role_spring_gateway_actuator")
	      .anyExchange().permitAll()
	      .and()
	      .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		http.csrf().disable();
		return http.build();
	}
	
	private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
		ReactiveJwtAuthenticationConverter jwtConverter = new ReactiveJwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }

}
