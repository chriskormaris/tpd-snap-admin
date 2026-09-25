package tech.ailef.snapadmin.external.service;

public interface LdapService {

    AuthResponse isAuthenticUser(String username, String password);

}
