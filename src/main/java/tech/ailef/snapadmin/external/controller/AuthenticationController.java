package tech.ailef.snapadmin.external.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.ailef.snapadmin.external.SnapAdmin;
import tech.ailef.snapadmin.external.SnapAdminProperties;
import tech.ailef.snapadmin.external.dbmapping.DbObjectSchema;
import tech.ailef.snapadmin.external.dbmapping.SnapAdminRepository;
import tech.ailef.snapadmin.external.dto.Credentials;
import tech.ailef.snapadmin.external.exceptions.SnapAdminException;
import tech.ailef.snapadmin.external.service.LdapService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(value= {"/${snapadmin.baseUrl}", "/${snapadmin.baseUrl}/"})
public class AuthenticationController {

    @Autowired
    private SnapAdminRepository repository;

    @Autowired
    private SnapAdmin snapAdmin;

	@Autowired
	private SnapAdminProperties snapAdminProperties;

    @Autowired
    private LdapService ldapService;

    @GetMapping("/login")
    public String login() {
        return "snapadmin/login";
    }

    @PostMapping("/login")
    public String login(Model model, Credentials credentials) {
	    String username = credentials.username().replace("@tpd.gr", "");

		boolean isAuthenticated = ldapService.isAuthenticUser(username, credentials.password());

	    if (isAuthenticated) {
		    if (!snapAdminProperties.getWhitelistUsers().contains(username)
				    && !snapAdminProperties.getWhitelistAdmins().contains(username)) {
			    log.error("User " + username + " is not whitelisted!");
			    throw new SnapAdminException("Ο χρήστης " + username + " δεν είναι στη λευκή λίστα!");
		    }

            snapAdmin.setAuthenticated(true);
            snapAdmin.setUsername(username);
	    }

        addAttributes(model);
        model.addAttribute("activePage", "entities");
        model.addAttribute("title", "Πίνακες | Index");
        model.addAttribute("snapadmin_authenticated", isAuthenticated);
        model.addAttribute("snapadmin_authenticatedUser", username);

        return "snapadmin/home";
    }

    @PostMapping("/logout")
    public String logout(Model model, @RequestParam(required = false) String query) {
        boolean isAuthenticated = false;
        snapAdmin.setAuthenticated(isAuthenticated);
        snapAdmin.setUsername(null);

        addAttributes(model);
        model.addAttribute("title", "Αποσύνδεση");
        model.addAttribute("snapadmin_authenticated", isAuthenticated);
        model.addAttribute("snapadmin_authenticatedUser", null);

        return "snapadmin/login";
    }

    private void addAttributes(Model model) {
        List<DbObjectSchema> schemas = snapAdmin.getSchemas();

        Map<String, List<DbObjectSchema>> groupedBy =
                schemas.stream().collect(Collectors.groupingBy(s -> s.getBasePackage()));

        Map<String, Long> counts =
                schemas.stream().collect(Collectors.toMap(s -> s.getClassName(), s -> repository.count(s)));

        model.addAttribute("schemas", groupedBy);
        model.addAttribute("query", null);
        model.addAttribute("counts", counts);
    }

}
