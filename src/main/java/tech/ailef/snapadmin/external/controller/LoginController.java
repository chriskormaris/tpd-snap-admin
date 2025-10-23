package tech.ailef.snapadmin.external.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.ailef.snapadmin.external.SnapAdmin;
import tech.ailef.snapadmin.external.dbmapping.DbObjectSchema;
import tech.ailef.snapadmin.external.dbmapping.SnapAdminRepository;
import tech.ailef.snapadmin.external.dto.Credentials;
import tech.ailef.snapadmin.external.service.LdapService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value= {"/${snapadmin.baseUrl}", "/${snapadmin.baseUrl}/"})
public class LoginController {

    @Autowired
    private SnapAdminRepository repository;

    @Autowired
    private SnapAdmin snapAdmin;

    @Autowired
    private LdapService ldapService;

    @GetMapping("/login")
    public String login() {
        snapAdmin.setAuthenticated(false);
        return "snapadmin/login";
    }

    @PostMapping
    public String home(Model model, Credentials credentials) {
        boolean isAuthenticated = ldapService.isAuthenticUser(credentials.user(), credentials.pass());
        snapAdmin.setAuthenticated(isAuthenticated);

        List<DbObjectSchema> schemas = snapAdmin.getSchemas();

        Map<String, List<DbObjectSchema>> groupedBy =
                schemas.stream().collect(Collectors.groupingBy(DbObjectSchema::getBasePackage));

        Map<String, Long> counts =
                schemas.stream().collect(Collectors.toMap(DbObjectSchema::getClassName, s -> repository.count(s)));

        model.addAttribute("schemas", groupedBy);
        model.addAttribute("query", null);
        model.addAttribute("counts", counts);
        model.addAttribute("activePage", "entities");
        model.addAttribute("title", "Πίνακες | Index");
        model.addAttribute("snapadmin_authenticated", isAuthenticated);

        return "snapadmin/home";
    }

}
