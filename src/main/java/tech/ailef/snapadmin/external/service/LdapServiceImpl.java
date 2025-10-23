package tech.ailef.snapadmin.external.service;

import org.springframework.stereotype.Service;
import tech.ailef.snapadmin.external.exceptions.SnapAdminException;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

@Service
public class LdapServiceImpl implements LdapService {

    public final String PROVIDER_URL = "ldap://MasterDC.tpd.local ldap://DC2.tpd.local ldap://192.168.10.234 ldap://192.168.10.236";
    public final String SECURITY_AUTHENTICATION = "simple";
    public final String SECURITY_PRINCIPAL_POSTFIX = "@tpd.local";

    @Override
    public boolean isAuthenticUser(String user, String pass) {
        boolean result = false;

        if (user == null || pass == null || user.isEmpty() || pass.isEmpty()) {
            return result;
        }

        try {
            Hashtable env = new Hashtable();

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, PROVIDER_URL);
            env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
            env.put(Context.SECURITY_PRINCIPAL, user + SECURITY_PRINCIPAL_POSTFIX);
            env.put(Context.SECURITY_CREDENTIALS, pass);

            LdapContext ctxGC = new InitialLdapContext(env, null);

            ctxGC.close();

            result = true;

            System.out.println("LDAP authentication for " + user + " succeeded!");
        } catch (Exception ex) {
            // Not authenticated
//            ex.printStackTrace();
//            result = false;
//            System.out.println("LDAP authentication for " + user + " failed!");
            throw new SnapAdminException("LDAP authentication for " + user + " failed!", ex);
        }

        return result;
    }

}
