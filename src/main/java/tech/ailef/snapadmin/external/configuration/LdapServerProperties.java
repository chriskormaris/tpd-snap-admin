package tech.ailef.snapadmin.external.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("ldap.server")
public class LdapServerProperties {

    private List<String> urls;

}
