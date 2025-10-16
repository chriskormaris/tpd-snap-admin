/* 
 * SnapAdmin - An automatically generated CRUD admin UI for Spring Boot apps
 * Copyright (C) 2023 Ailef (http://ailef.tech)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package tech.ailef.snapadmin.external.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.id.IdentifierGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tech.ailef.snapadmin.external.SnapAdmin;
import tech.ailef.snapadmin.external.SnapAdminProperties;
import tech.ailef.snapadmin.external.dbmapping.DbObject;
import tech.ailef.snapadmin.external.dbmapping.DbObjectSchema;
import tech.ailef.snapadmin.external.dbmapping.SnapAdminRepository;
import tech.ailef.snapadmin.external.dbmapping.query.DbQueryResult;
import tech.ailef.snapadmin.external.dto.CompareOperator;
import tech.ailef.snapadmin.external.dto.FacetedSearchRequest;
import tech.ailef.snapadmin.external.dto.LogsSearchRequest;
import tech.ailef.snapadmin.external.dto.PaginatedResult;
import tech.ailef.snapadmin.external.dto.PaginationInfo;
import tech.ailef.snapadmin.external.dto.QueryFilter;
import tech.ailef.snapadmin.external.dto.ValidationErrorsContainer;
import tech.ailef.snapadmin.external.exceptions.InvalidPageException;
import tech.ailef.snapadmin.external.exceptions.SnapAdminException;
import tech.ailef.snapadmin.external.exceptions.SnapAdminNotFoundException;
import tech.ailef.snapadmin.external.misc.Utils;
import tech.ailef.snapadmin.internal.model.ConsoleQuery;
import tech.ailef.snapadmin.internal.model.UserAction;
import tech.ailef.snapadmin.internal.model.UserSetting;
import tech.ailef.snapadmin.internal.service.ConsoleQueryService;
import tech.ailef.snapadmin.internal.service.UserActionService;
import tech.ailef.snapadmin.internal.service.UserSettingsService;

import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The main SnapAdmin controller that register most of the routes of the web interface.
 */
@Controller
@RequestMapping(value= {"/${snapadmin.baseUrl}", "/${snapadmin.baseUrl}/"})
public class SnapAdminController {
	private static final Logger logger = LoggerFactory.getLogger(SnapAdminController.class);

	private static final String UNIQUE_CONSTRAINT_MESSAGE = "Το %s αυτό υπάρχει ήδη στον πίνακα. " +
			"Εισάγετε ένα διαφορετικό.";

	@Autowired
	private SnapAdminProperties properties;
	
	@Autowired
	private SnapAdminRepository repository;
	
	@Autowired
	private SnapAdmin snapAdmin;
	
	@Autowired
	private UserActionService userActionService;
	
	@Autowired
	private ConsoleQueryService consoleService;
	
	@Autowired
	private UserSettingsService userSettingsService;
	
	/**
	 * Home page with list of schemas
	 * @param model
	 * @param query
	 * @return
	 */
	@GetMapping
	public String index(Model model, @RequestParam(required = false) String query) {
		List<DbObjectSchema> schemas = snapAdmin.getSchemas();
		if (query != null && !query.isBlank()) {
			schemas = schemas.stream().filter(s -> {
				return s.getClassName().toLowerCase().contains(query.toLowerCase())
					|| s.getTableName().toLowerCase().contains(query.toLowerCase());
			}).collect(Collectors.toList());
		}
		
		Map<String, List<DbObjectSchema>> groupedBy = 
			schemas.stream().collect(Collectors.groupingBy(s -> s.getBasePackage()));
		
		Map<String, Long> counts = 
			schemas.stream().collect(Collectors.toMap(s -> s.getClassName(), s -> repository.count(s)));
		
		model.addAttribute("schemas", groupedBy);
		model.addAttribute("query", query);
		model.addAttribute("counts", counts);
		model.addAttribute("activePage", "entities");
		model.addAttribute("title", "Πίνακες | Index");
		
		return "snapadmin/home";
	}
	
	/**
	 * Lists the items of a schema by applying a variety of filters:
	 *  - query: fuzzy search
	 *  - otherParams: filterable fields
	 * Includes pagination and sorting options.
	 *  
	 * @param model
	 * @param className
	 * @param page
	 * @param query
	 * @param pageSize
	 * @param sortKey
	 * @param sortOrder
	 * @param otherParams
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/model/{className}")
	public String list(Model model, @PathVariable String className,
			@RequestParam(required=false) Integer page, @RequestParam(required=false) String query,
			@RequestParam(required=false) Integer pageSize, @RequestParam(required=false) String sortKey, 
			@RequestParam(required=false) String sortOrder, @RequestParam MultiValueMap<String, String> otherParams,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if (page == null) page = 1;
		if (pageSize == null) pageSize = 50;
		
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		
		Set<QueryFilter> queryFilters = Utils.computeFilters(schema, otherParams);
		if (otherParams.containsKey("remove_field")) {
			List<String> fields = otherParams.get("remove_field");
			
			for (int i = 0; i < fields.size(); i++) {
				QueryFilter toRemove = 
					new QueryFilter(
						schema.getFieldByJavaName(fields.get(i)), 
						CompareOperator.valueOf(otherParams.get("remove_op").get(i).toUpperCase()), 
						otherParams.get("remove_value").get(i)
					);
				
				queryFilters.removeIf(f -> f.equals(toRemove));
			}
			
			FacetedSearchRequest filterRequest = new FacetedSearchRequest(queryFilters);
			MultiValueMap<String, String> parameterMap = filterRequest.computeParams();
			
			MultiValueMap<String, String> filteredParams = new LinkedMultiValueMap<>();
 			request.getParameterMap().entrySet().stream()
				.filter(e -> !e.getKey().startsWith("remove_") && !e.getKey().startsWith("filter_"))
				.forEach(e -> {
					filteredParams.putIfAbsent(e.getKey(), new ArrayList<>());
					for (String v : e.getValue()) {
						if (filteredParams.get(e.getKey()).isEmpty()) {
							filteredParams.get(e.getKey()).add(v);
						} else {
							filteredParams.get(e.getKey()).set(0, v);
						}
					}
				});
 			
 			filteredParams.putAll(parameterMap);
 			String queryString = Utils.getQueryString(filteredParams);
			String redirectUrl = request.getServletPath() + queryString; 
			return "redirect:" + redirectUrl.trim();
		}
		
		try {
			PaginatedResult<DbObject> result = null;
			if (query != null || !otherParams.isEmpty()) {
				result = repository.search(schema, query, page, pageSize, sortKey, sortOrder, queryFilters);
			} else {
				result = repository.findAll(schema, page, pageSize, sortKey, sortOrder);
			}
				
			model.addAttribute("title", "Πίνακες | " + schema.getJavaClass().getSimpleName() + " | Δείκτης");
			model.addAttribute("page", result);
			model.addAttribute("schema", schema);
			model.addAttribute("activePage", "entities");
			model.addAttribute("sortKey", sortKey);
			model.addAttribute("query", query);
			model.addAttribute("sortOrder", sortOrder);
			model.addAttribute("activeFilters", queryFilters);
			return "snapadmin/model/list";
			
		} catch (InvalidPageException e) {
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		} catch (SnapAdminException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("errorTitle", "Invalid request");
			model.addAttribute("schema", schema);
			model.addAttribute("activePage", "entities");
			model.addAttribute("sortKey", sortKey);
			model.addAttribute("query", query);
			model.addAttribute("sortOrder", sortOrder);
			model.addAttribute("activeFilters", queryFilters);
			return "snapadmin/model/list";
		}
	}
	
	/**
	 * Displays information about the schema
	 * @param model
	 * @param className
	 * @return
	 */
	@GetMapping("/model/{className}/schema")
	public String schema(Model model, @PathVariable String className) {
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		
		model.addAttribute("activePage", "entities");
		model.addAttribute("schema", schema);
		
		return "snapadmin/model/schema";
	}
	
	/**
	 * Shows a single item
	 * @param model
	 * @param className
	 * @param id
	 * @return
	 */
	@GetMapping("/model/{className}/show/{id}")
	public String show(Model model, @PathVariable String className, @PathVariable String id) {
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		
		Object pkValue = schema.getPrimaryKey().getType().parseValue(id);
		
		DbObject object = repository.findById(schema, pkValue).orElseThrow(() -> {
			return new SnapAdminNotFoundException(
				schema.getJavaClass().getSimpleName() + " with ID " + id + " not found."
			);
		});
		
		model.addAttribute("title", "Πίνακες | " + schema.getJavaClass().getSimpleName() + " | " + object.getDisplayName());
		model.addAttribute("object", object);
		model.addAttribute("activePage", "entities");
		model.addAttribute("schema", schema);
		
		return "snapadmin/model/show";
	}
	
	
	@GetMapping("/model/{className}/create")
	public String create(Model model, @PathVariable String className, RedirectAttributes attr) {
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		
		if (!schema.isCreateEnabled()) {
			attr.addFlashAttribute("errorTitle", "Unauthorized");
			attr.addFlashAttribute("error", "CREATE λειτουργίες έχουν απενεργοποιηθεί για αυτόν τον τύπο (" + schema.getJavaClass().getSimpleName() + ").");
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		}
		
		model.addAttribute("className", className);
		model.addAttribute("schema", schema);
		model.addAttribute("title", "Πίνακες | " + schema.getJavaClass().getSimpleName() + " | Δημιουργία");
		model.addAttribute("activePage", "entities");
		model.addAttribute("create", true);
		
		return "snapadmin/model/create";
	}
	
	@GetMapping("/model/{className}/edit/{id}")
	public String edit(Model model, @PathVariable String className, @PathVariable String id, RedirectAttributes attr) {
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		
		Object pkValue = schema.getPrimaryKey().getType().parseValue(id);
		
		if (!schema.isEditEnabled()) {
			attr.addFlashAttribute("errorTitle", "Unauthorized");
			attr.addFlashAttribute("error", "EDIT λειτουργίες έχουν απενεργοποιηθεί για αυτόν τον τύπο (" + schema.getJavaClass().getSimpleName() + ").");
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		}
		
		DbObject object = repository.findById(schema, pkValue).orElseThrow(() -> {
			return new SnapAdminNotFoundException(
			  "Object " + className + " with id " + id + " not found"
			);
		});
		
		model.addAttribute("title", "Πίνακες | " + schema.getJavaClass().getSimpleName() + " | Επεξεργασία | " + object.getDisplayName());
		model.addAttribute("className", className);
		model.addAttribute("object", object);
		model.addAttribute("schema", schema);
		model.addAttribute("activePage", "entities");
		model.addAttribute("create", false);
		
		return "snapadmin/model/create";
	}
	
	@PostMapping(value="/model/{className}/delete/{id}")
	/**
	 * Delete a single row based on its primary key value
	 * @param className
	 * @param id
	 * @param attr
	 * @return
	 */
	public String delete(@PathVariable String className, @PathVariable String id, RedirectAttributes attr,
			Principal principal) {
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		String authUser = principal != null ? principal.getName() : null;
		
		if (!schema.isDeleteEnabled()) {
			attr.addFlashAttribute("errorTitle", "Αδύνατο το DELETE γραμμής");
			attr.addFlashAttribute("error", "DELETE λειτουργίες έχουν απενεργοποιηθεί για αυτόν τον πίνακα.");
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		}
		
		try {
			repository.delete(schema, id);
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("errorTitle", "Αδύνατο το DELETE γραμμής");
			attr.addFlashAttribute("error", e.getMessage());
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		}
		
		saveAction(new UserAction(schema.getTableName(), id, "DELETE", schema.getClassName(), authUser));
		attr.addFlashAttribute("message", "Διαγράφηκε " + schema.getJavaClass().getSimpleName() + " με "
				+ schema.getPrimaryKey().getName() + "=" + id);

		return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
	}
	
	@PostMapping(value="/model/{className}/delete")
	/**
	 * Delete multiple rows based on their primary key values
	 * @param className
	 * @param ids
	 * @param attr
	 * @return
	 */
	public String delete(@PathVariable String className, @RequestParam String[] ids, RedirectAttributes attr,
			Principal principal) {
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		String authUser = principal != null ? principal.getName() : null;
		
		if (!schema.isDeleteEnabled()) {
			attr.addFlashAttribute("errorTitle", "Αδύνατο το DELETE γραμμών");
			attr.addFlashAttribute("error", "DELETE λειτουργίες έχουν απενεργοποιηθεί για αυτόν τον πίνακα.");
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		}
		
		int countDeleted = 0;
		for (String id : ids) {
			try {
				repository.delete(schema, id);
				countDeleted += 1;
			} catch (DataIntegrityViolationException e) {
				attr.addFlashAttribute("error", e.getMessage());
			}
		}
		
		if (countDeleted > 0)
			attr.addFlashAttribute("message", "Διαγράφηκαν " + countDeleted + " από " + ids.length + " αντικείμενα");
		
		for (String id : ids) {
			saveAction(new UserAction(schema.getTableName(), id, "DELETE", schema.getClassName(), authUser));
		}
		
		return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
	}
	
	@PostMapping(value="/model/{className}/create")
	public String store(@PathVariable String className,
			@RequestParam MultiValueMap<String, String> formParams,
			@RequestParam Map<String, MultipartFile> files,
			RedirectAttributes attr,
			Principal principal) {
		String authUser = principal != null ? principal.getName() : null;

		// Extract all parameters that have exactly 1 value,
		// as these will be the raw values for the object that is being
		// created.
		// The remaining parmeters which have more than 1 value
		// are IDs in a many-to-many relationship and need to be
		// handled separately
		Map<String, String> params = new HashMap<>();
		for (String param : formParams.keySet()) {
			if (!param.endsWith("[]")) {
				params.put(param, formParams.getFirst(param));
			}
		}
		
		Map<String, List<String>> multiValuedParams = new HashMap<>();
		for (String param : formParams.keySet()) {
			if (param.endsWith("[]")) {
				List<String> list = formParams.get(param);
				// If the request contains only 1 parameter value, it's the empty
				// value that signifies just the presence of the field (e.g. the
				// user might've deleted all the value)
				if (list.size() == 1) {
					multiValuedParams.put(param, new ArrayList<>());
				} else {
					list.removeIf(String::isBlank);
					multiValuedParams.put(
						param, 
						list
					);
				}
			}
		}
		
 		String c = params.get("__snapadmin_create");
		if (c == null) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST, "Missing required param __snapadmin_create"
			);
		}
		
		boolean create = Boolean.parseBoolean(c);
		
		DbObjectSchema schema = snapAdmin.findSchemaByClassName(className);
		
		if (!schema.isCreateEnabled() && create) {
			attr.addFlashAttribute("errorTitle", "Unauthorized");
			attr.addFlashAttribute("error", "CREATE λειτουργίες έχουν απενεργοποιηθεί για αυτόν τον τύπο (" + schema.getJavaClass().getSimpleName() + ").");
			return "redirect:/" + properties.getBaseUrl() + "/model/" + className;
		}

		String pkValue = params.get(schema.getPrimaryKey().getName());
		if (pkValue == null || pkValue.isBlank()) {
			pkValue = null;
		}

		try {
			if (pkValue == null) {
				Object newPrimaryKey = repository.create(schema, params, files, pkValue);
				repository.attachManyToMany(schema, newPrimaryKey, multiValuedParams);
				pkValue = newPrimaryKey.toString();
				attr.addFlashAttribute("message", "Το αντικείμενο δημιουργήθηκε επιτυχώς.");
				saveAction(new UserAction(schema.getTableName(), pkValue, "CREATE", schema.getClassName(), authUser));
			} else {
				Object parsedPkValue = schema.getPrimaryKey().getType().parseValue(pkValue);

				Optional<DbObject> object = repository.findById(schema, parsedPkValue);

				if (!object.isEmpty()) {
					if (create) {
						attr.addFlashAttribute("errorTitle", "Αδύνατο να δημιουργηθεί το αντικείμενο");
						attr.addFlashAttribute("error", "Αντικείμενο με id " + object.get().getPrimaryKeyValue() + " υπάρχει ήδη.");
						attr.addFlashAttribute("params", params);
					} else {
						repository.update(schema, params, files);
						repository.attachManyToMany(schema, parsedPkValue, multiValuedParams);
						attr.addFlashAttribute("message", "Το αντικείμενο αποθηκεύτηκε επιτυχώς.");
						saveAction(new UserAction(schema.getTableName(), parsedPkValue.toString(), "EDIT", schema.getClassName(), authUser));
					}
				} else {
					Object newPrimaryKey = repository.create(schema, params, files, pkValue);
					repository.attachManyToMany(schema, newPrimaryKey, multiValuedParams);
					attr.addFlashAttribute("message", "Το αντικείμενο δημιουργήθηκε επιτυχώς");
					saveAction(new UserAction(schema.getTableName(), pkValue, "CREATE", schema.getClassName(), authUser));
				}
			}
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("errorTitle", "Σφάλμα βάσης");
			if (e.getMessage().contains("ORA-00001: unique constraint (DTPD.MHTRWO_AFM_UNIQUE) violated")) {
				attr.addFlashAttribute("error", UNIQUE_CONSTRAINT_MESSAGE.formatted("ΑΦΜ"));
			} else if (e.getMessage().contains("ORA-00001: unique constraint (DTPD.MHTRWO_ACCOUNT_IBAN_UNIQUE) violated")) {
				attr.addFlashAttribute("error", UNIQUE_CONSTRAINT_MESSAGE.formatted("ΙΒΑΝ"));
			} else {
				attr.addFlashAttribute("error", e.getMessage());
			}
			attr.addFlashAttribute("params", params);
		} catch (UncategorizedSQLException | IdentifierGenerationException e) {
			attr.addFlashAttribute("errorTitle", "Σφάλμα βάσης");
			attr.addFlashAttribute("error", e.getMessage());
			attr.addFlashAttribute("params", params);
		} catch (ConstraintViolationException e) {
			attr.addFlashAttribute("errorTitle", "Σφάλμα επαλήθευσης");
			attr.addFlashAttribute("error", "Δείτε παρακάτω για λεπτομέρειες");
			attr.addFlashAttribute("validationErrors", new ValidationErrorsContainer(e));
			attr.addFlashAttribute("params", params);
		} catch (SnapAdminException e) {
			Throwable cause = e.getCause() != null ? e.getCause() : e;
			logger.error(Arrays.toString(cause.getStackTrace()));
			attr.addFlashAttribute("errorTitle", "Σφάλμα");
			attr.addFlashAttribute("error", e.getMessage());
			attr.addFlashAttribute("params", params);
		} catch (TransactionSystemException e) {
			if (e.getRootCause() instanceof ConstraintViolationException) {
				ConstraintViolationException ee = (ConstraintViolationException)e.getRootCause();
				attr.addFlashAttribute("errorTitle", "Σφάλμα επαλήθευσης");
				attr.addFlashAttribute("error", "Δείτε παρακάτω για λεπτομέρειες");
				attr.addFlashAttribute("validationErrors", new ValidationErrorsContainer(ee));
				attr.addFlashAttribute("params", params);
			} else {
				throw new RuntimeException(e);
			}
		} catch (RuntimeException e) {
			attr.addFlashAttribute("errorTitle", "Σφάλμα βάσης");
			attr.addFlashAttribute("error", e.getMessage());
			attr.addFlashAttribute("params", params);
		}


		if (attr.getFlashAttributes().containsKey("error")) {
			if (create)
				return "redirect:/" + properties.getBaseUrl() + "/model/" + schema.getClassName() + "/create";
			else
				return "redirect:/" + properties.getBaseUrl() + "/model/" + schema.getClassName() + "/edit/" + pkValue;
		} else {
			return "redirect:/" + properties.getBaseUrl() + "/model/" + schema.getClassName() + "/show/" + pkValue;
		}
	}
	
	@GetMapping("/logs")
	public String logs(Model model, LogsSearchRequest searchRequest) {
		if (searchRequest.getUsername() != null && searchRequest.getUsername().isEmpty()) {
			searchRequest.setUsername(null);
		}
		model.addAttribute("activePage", "logs");
		model.addAttribute(
			"page", 
			userActionService.findActions(searchRequest)
		);
		model.addAttribute("title", "Action logs");
		model.addAttribute("schemas", snapAdmin.getSchemas());
		model.addAttribute("searchRequest", searchRequest);
		return "snapadmin/logs";
	}
	
	
	@GetMapping("/settings")
	public String settings(Model model) {
		model.addAttribute("title", "Settings");
		model.addAttribute("activePage", "settings");
		return "snapadmin/settings/settings";
	}
	
	@GetMapping("/help")
	public String help(Model model) {
		model.addAttribute("title", "Help");
		model.addAttribute("activePage", "help");
		return "snapadmin/help";
	}
	
	@GetMapping("/console/new")
	public String consoleNew(Model model) {
		if (!properties.isSqlConsoleEnabled()) {
			throw new SnapAdminException("SQL console not enabled");
		}
		
		ConsoleQuery q = new ConsoleQuery();
		consoleService.save(q);
		return "redirect:/" + properties.getBaseUrl() + "/console/run/" + q.getId();
	}
	
	@GetMapping("/console")
	public String console() {
		if (!properties.isSqlConsoleEnabled()) {
			throw new SnapAdminException("SQL console not enabled");
		}
		
		List<ConsoleQuery> tabs = consoleService.findAll();
		
		if (tabs.isEmpty()) {
			ConsoleQuery q = new ConsoleQuery();

			// int randomIndex = new Random().nextInt(0, snapAdmin.getSchemas().size());
			// String randomTable = snapAdmin.getSchemas().get(randomIndex).getTableName();

			q.setSql(
				"-- Σας προτείνουμε να συμπεριλάβετε την έκφραση ROWNUM στο ερώτημά σας\n"
				+ "-- Παρόλο που η Κονσόλα SQL υποστηρίζει σελιδοποίηση, επιστρέφει όλα τα αποτελέσματα\n\n"
				+ "SELECT * FROM DTPD.MHTRWO WHERE ROWNUM <= 1000"
			);

			consoleService.save(q);
			return "redirect:/" + properties.getBaseUrl() + "/console/run/" + q.getId();
		} else {
			return "redirect:/" + properties.getBaseUrl() + "/console/run/" + tabs.get(0).getId();
		}
	}
	
	@PostMapping("/console/delete/{queryId}")
	public String consoleDelete(@PathVariable String queryId, Model model) {
		if (!properties.isSqlConsoleEnabled()) {
			throw new SnapAdminException("SQL console not enabled");
		}
		consoleService.delete(queryId);
		return "redirect:/" + properties.getBaseUrl() + "/console";
	}
	
	@GetMapping("/console/run/{queryId}")
	public String consoleRun(Model model, @RequestParam(required = false) String query,
			@RequestParam(required = false) String queryTitle,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize,
			@PathVariable String queryId) {
		if (page == null || page <= 0) page = 1;
		if (pageSize == null) pageSize = 50;
		
		long startTime = System.currentTimeMillis();
		
		if (!properties.isSqlConsoleEnabled()) {
			throw new SnapAdminException("SQL console not enabled");
		}
		
		ConsoleQuery activeQuery = consoleService.findById(queryId).orElseThrow(() -> {
			return new SnapAdminNotFoundException("Query with ID " + queryId + " not found.");
		});
		
		if (query != null && !query.isBlank()) {
			activeQuery.setSql(query);
		}
		if (queryTitle != null && !queryTitle.isBlank()) {
			activeQuery.setTitle(queryTitle);
		}
		
		activeQuery.setUpdatedAt(LocalDateTime.now());
		consoleService.save(activeQuery);
		
		model.addAttribute("activePage", "console");
		model.addAttribute("activeQuery", activeQuery);
		
		List<ConsoleQuery> tabs = consoleService.findAll();
		model.addAttribute("tabs", tabs);
		
		DbQueryResult results = repository.executeQuery(activeQuery.getSql());
		
		if (!results.isEmpty()) {
			int maxPage = (int)(Math.ceil ((double)results.size() / pageSize));
			PaginationInfo pagination = new PaginationInfo(page, maxPage, pageSize, results.size(), null, null);
			int startOffset = (page - 1) * pageSize;
			int endOffset = (page) * pageSize;
			
			endOffset = Math.min(results.size(), endOffset);
			
			results.crop(startOffset, endOffset);
			model.addAttribute("pagination", pagination);
			model.addAttribute("results", results);
		} else {
			PaginationInfo pagination = new PaginationInfo(page, 0, pageSize, results.size(), null, null);
			model.addAttribute("pagination", pagination);
		}
		
		model.addAttribute("title", "SQL Console | " + activeQuery.getTitle());
		double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
		model.addAttribute("elapsedTime", new DecimalFormat("0.0#").format(elapsedTime));
		return "snapadmin/console";
	}

	
	@GetMapping("/settings/appearance")
	public String settingsAppearance(Model model) {
		model.addAttribute("activePage", "settings");
		return "snapadmin/settings/appearance";
	}
	
	@GetMapping("/forbidden")
	public String forbidden(Model model) {
		model.addAttribute("error", "Forbidden");
		model.addAttribute("status", "403");
		model.addAttribute("message", "You don't have the privileges to perform this action");
		return "snapadmin/other/error";
	}
	
	@PostMapping("/settings")
	public String settings(@RequestParam Map<String, String> params, Model model) {
		String next = params.getOrDefault("next", "snapadmin/settings/settings");
		
		for (String paramName : params.keySet()) {
			if (paramName.equals("next")) continue;
			
			userSettingsService.save(new UserSetting(paramName, params.get(paramName)));
		}
		model.addAttribute("activePage", "settings");
		return next;
	}
	
	private UserAction saveAction(UserAction action) {
		return userActionService.save(action);
	}
}
