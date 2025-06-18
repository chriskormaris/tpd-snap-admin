package tech.ailef.snapadmin;


import local.tpd.oracle.OracleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.ailef.snapadmin.external.SnapAdminAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration({SnapAdminAutoConfiguration.class, OracleConfiguration.class})
public class SnapAdminProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnapAdminProjectApplication.class, args);
	}

}
