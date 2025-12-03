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
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
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
    public AuthResponse isAuthenticUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new SnapAdminException("Σφάλμα πιστοποίησης για τον χρήστη " + username + "!");
        }

        try {
            Hashtable<String, String> env = new Hashtable<>();

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, String.join(" ", ldapServerProperties.getUrls()));
            env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
            env.put(Context.SECURITY_PRINCIPAL, username + SECURITY_PRINCIPAL_POSTFIX);
            env.put(Context.SECURITY_CREDENTIALS, password);

            LdapContext ctxGC = new InitialLdapContext(env, null);

            AuthResponse authResponse = getUserInfo(username, ctxGC, getSearchControls());

            ctxGC.close();

            logger.info("LDAP authentication for {} succeeded!", username);

	        return authResponse;
        } catch (Exception ex) {
            // Not authenticated
            logger.error("LDAP authentication for {} failed!", username, ex);
            SecurityContextHolder.clearContext();
            httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            throw new SnapAdminException("Σφάλμα πιστοποίησης για τον χρήστη " + username + "!", ex);
        }
    }

    private static AuthResponse getUserInfo(String username, LdapContext ctx, SearchControls searchControls) {
        try {
            String name = "DC=tpd,DC=local";
            String filter = "(&(objectClass=user)(sAMAccountName=" + username + "))";
            NamingEnumeration<SearchResult> answer = ctx.search(name, filter, searchControls);
            if (answer.hasMoreElements()) {
                SearchResult sr = answer.nextElement();
                Attributes allAttr = sr.getAttributes();

                Attribute cnAttr = allAttr.get("cn");
                String firstName = String.valueOf(cnAttr).split(" ")[1];
                String lastName = String.valueOf(cnAttr).split(" ").length > 2 ? String.valueOf(cnAttr).split(" ")[2] : "";

                Attribute mailAttr = allAttr.get("mail");
                String email = String.valueOf(mailAttr).split(" ")[1];

                return new AuthResponse(firstName, lastName, email);
            } else {
                logger.error("User {} not found in LDAP search.", username);
                throw new SnapAdminException("User " + username + " not found in LDAP search.");
            }
        } catch (NamingException ex) {
            logger.error("User {} not found in LDAP search.", username, ex);
            throw new SnapAdminException("User " + username + " not found in LDAP search.", ex);
        }
    }

    private static SearchControls getSearchControls() {
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] attrIDs = {"cn", "mail"};
        cons.setReturningAttributes(attrIDs);
        return cons;
    }

}
