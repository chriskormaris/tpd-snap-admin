package tech.ailef.snapadmin.external.service;

public interface LdapService {

    boolean isAuthenticUser(String user, String pass);

}
