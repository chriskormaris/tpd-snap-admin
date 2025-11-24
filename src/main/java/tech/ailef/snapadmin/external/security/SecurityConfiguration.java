package tech.ailef.snapadmin.external.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tech.ailef.snapadmin.external.SnapAdminProperties;

/**
 * This class shows how to secure your SnapAdmin using a standard
 * Spring Security configuration. In particular, look at the {@link SecurityFilterChain}
 * bean defined in the {@link #filterChain(HttpSecurity)} method to see
 * how to require authentication and authorization to access specific SnapAdmin
 * routes and features.
 */
@Configuration
public class SecurityConfiguration {

	@Autowired
	private SnapAdminProperties properties;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		String baseUrl = properties.getBaseUrl();

	    return http.authorizeHttpRequests(auth -> {
	        /* POST methods (create, edit and delete) require ADMIN role
	    	 * Note that with this configuration users will still be able to access the edit/create page
	    	 * but they will get a Forbidden error after submitting the form if they are not authorized.
	    	 * You can also stop the serving of these pages altogether by customizing the route matchers
	    	 */
	        auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/" + baseUrl + "/auth/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/" + baseUrl + "/**")).hasAuthority("ADMIN")
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/" + baseUrl)).permitAll()
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/" + baseUrl + "/auth/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/" + baseUrl + "/snapadmin/**")).permitAll()
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/" + baseUrl + "/**")).authenticated();
        })
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .formLogin(AbstractHttpConfigurer::disable)
	    //.formLogin(l -> l.loginPage("/" + baseUrl + "/login").permitAll())
	    /* This custom exception handling code is only needed if you want to have
	     * nicer Forbidden error pages, for cases when a user tries to perform an
	     * action they don't have the correct privileges for (e.g., in the previous
	     * configuration a user without ADMIN role trying to edit/create items).
	     * The exception handling is delegated to the default handler if the
	     * error didn't occur on a SnapAdmin route. You can further customize this
	     * according to your needs or just not use it. In this last scenario, your
	     * default access denied handler will be used even for errors occurring inside
	     * SnapAdmin.
	     */
	    .exceptionHandling(e -> e.accessDeniedHandler((req, res, ex) -> {
	    	AccessDeniedHandlerImpl defaultHandler = new AccessDeniedHandlerImpl();
	    	if (req.getServletPath().startsWith("/" + baseUrl + "/")) {
	    		res.sendRedirect("/" + baseUrl + "/forbidden");
	    	} else {
	    		defaultHandler.handle(req, res, ex);
	    	}
	    }))
	    .build();
	}

}
