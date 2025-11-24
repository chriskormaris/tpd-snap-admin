package tech.ailef.snapadmin.external.service;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.ailef.snapadmin.external.configuration.LdapServerProperties;
import tech.ailef.snapadmin.external.exceptions.SnapAdminException;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class LdapServiceImpl implements LdapService {

    private static final Logger logger = LoggerFactory.getLogger(LdapServiceImpl.class);

    @Autowired
    private LdapServerProperties ldapServerProperties;

    @Autowired
    private HttpSession httpSession;

    public final String SECURITY_AUTHENTICATION = "simple";
    public final String SECURITY_PRINCIPAL_POSTFIX = "@tpd.local";

    @Override
    public boolean isAuthenticUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }

        try {
            Hashtable<String, String> env = new Hashtable<>();

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, String.join(" ", ldapServerProperties.getUrls()));
            env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
            env.put(Context.SECURITY_PRINCIPAL, username + SECURITY_PRINCIPAL_POSTFIX);
            env.put(Context.SECURITY_CREDENTIALS, password);

            LdapContext ctxGC = new InitialLdapContext(env, null);

            ctxGC.close();

            logger.info("LDAP authentication for " + username + " succeeded!");

	        return true;
        } catch (Exception ex) {
            // Not authenticated
            logger.error("LDAP authentication for " + username + " failed!", ex);
            SecurityContextHolder.clearContext();
            httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            throw new SnapAdminException("Σφάλμα πιστοποίησης για τον χρήστη " + username + "!", ex);
        }
    }

}
