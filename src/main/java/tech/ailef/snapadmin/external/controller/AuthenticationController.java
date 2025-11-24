package tech.ailef.snapadmin.external.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.ailef.snapadmin.external.SnapAdmin;
import tech.ailef.snapadmin.external.SnapAdminProperties;
import tech.ailef.snapadmin.external.dbmapping.SnapAdminRepository;
import tech.ailef.snapadmin.external.dto.Credentials;
import tech.ailef.snapadmin.external.exceptions.SnapAdminException;
import tech.ailef.snapadmin.external.service.LdapService;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping(value= {"/${snapadmin.baseUrl}/auth", "/${snapadmin.baseUrl}/auth/"})
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private SnapAdminRepository repository;

    @Autowired
    private SnapAdmin snapAdmin;

    @Autowired
    private SnapAdminProperties properties;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/login")
    public String login() {
        return "snapadmin/login";
    }

    @PostMapping("/login")
    public String login(Credentials credentials) {
        String username = credentials.username().replace("@tpd.gr", "");

        ldapService.isAuthenticUser(username, credentials.password());

        if (!properties.getWhitelistUsers().contains(username)
                && !properties.getAdmins().contains(username)) {
            logger.error("User " + username + " is not whitelisted!");
            SecurityContextHolder.clearContext();
            httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            throw new SnapAdminException("Ο χρήστης " + username + " δεν είναι στη λευκή λίστα!");
        }

        List<GrantedAuthority> authorities = Collections.emptyList();
        if (properties.getAdmins().contains(username)) {
            authorities = List.of(new SimpleGrantedAuthority("ADMIN"));
        }

        UsernamePasswordAuthenticationToken authWithRoles = new UsernamePasswordAuthenticationToken(
				username,
	            credentials.password(),
	            authorities
        );
        authWithRoles.eraseCredentials();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authWithRoles);
        SecurityContextHolder.setContext(context);

        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);

        snapAdmin.setLoggedIn(true);

	    return "redirect:/" + properties.getBaseUrl();
    }

    @PostMapping("/logout")
    public String logout() {
        snapAdmin.setLoggedIn(false);

        SecurityContextHolder.clearContext();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

	    return "redirect:/" + properties.getBaseUrl();
    }

}
