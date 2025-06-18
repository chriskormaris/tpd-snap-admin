package tech.ailef.snapadmin.external.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.ailef.snapadmin.external.configuration.LdapServerProperties;
import tech.ailef.snapadmin.external.exceptions.SnapAdminException;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Slf4j
@Service
@RequiredArgsConstructor
public class LdapServiceImpl implements LdapService {

    private final LdapServerProperties ldapServerProperties;

    public final String SECURITY_AUTHENTICATION = "simple";
    public final String SECURITY_PRINCIPAL_POSTFIX = "@tpd.local";

    @Override
    public boolean isAuthenticUser(String username, String password) {
        boolean result = false;

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return result;
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

            result = true;

            log.info("LDAP authentication for " + username + " succeeded!");
        } catch (Exception ex) {
            // Not authenticated
            log.error("LDAP authentication for " + username + " failed!", ex);
            throw new SnapAdminException("Σφάλμα πιστοποίησης για τον χρήστη " + username + "!", ex);
        }

        return result;
    }

}
