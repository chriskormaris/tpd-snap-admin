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
    public boolean isAuthenticUser(String user, String pass) {
        boolean result = false;

        if (user == null || pass == null || user.isEmpty() || pass.isEmpty()) {
            return result;
        }

        try {
            Hashtable<String, String> env = new Hashtable<>();

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, String.join(" ", ldapServerProperties.getUrls()));
            env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
            env.put(Context.SECURITY_PRINCIPAL, user + SECURITY_PRINCIPAL_POSTFIX);
            env.put(Context.SECURITY_CREDENTIALS, pass);

            LdapContext ctxGC = new InitialLdapContext(env, null);

            ctxGC.close();

            result = true;

            log.info("LDAP authentication for " + user + " succeeded!");
        } catch (Exception ex) {
            // Not authenticated
            // ex.printStackTrace();
            // result = false;
            // log.error("LDAP authentication for " + user + " failed!");
            throw new SnapAdminException("LDAP authentication for " + user + " failed!", ex);
        }

        return result;
    }

}
