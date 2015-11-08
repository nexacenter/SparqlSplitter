package it.linkeddata.sparqlGoodies.controllers;

import it.linkeddata.sparqlGoodies.splitter.ConfigurationBean;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jena.atlas.web.AcceptList;
import org.apache.jena.atlas.web.MediaType;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UrlPathHelper;

@Controller
@RequestMapping(value = "")
public class SparqlSplitter {

	@Autowired
	ConfigurationBean conf;

	// final AcceptList offeringRDF = new
	// AcceptList("text/turtle, application/turtle, " //
	// + "application/x-turtle, application/rdf+xml, " //
	// + "application/rdf+json, application/ld+json, " //
	// + "text/plain, application/n-triples, text/trig, " //
	// + "application/n-quads, application/x-trig, application/trig, " //
	// + "text/n-quads, text/nquads, application/trix+xml, " //
	// + "application/rdf+json, text/rdf+n3, application/n3, " //
	// + "text/n3");

	final AcceptList offeringRDF = new AcceptList("application/sparql-results+xml, application/sparql-results+json, text/boolean, application/x-binary-rdf-results-table");

	final AcceptList offeringResources = new AcceptList("text/html, application/xhtml+xml");

	@RequestMapping(value = { "robots.txt" }, produces = "text/plain")
	public ResponseEntity<String> robots() {
		return new ResponseEntity<String>("User-agent: *\nAllow: 	/", HttpStatus.OK);
	}

	@RequestMapping(value = { "{path:(?!staticResources).*/sparql$}", "{path:(?!staticResources).*$}/**/sparql" })
	public Object home(ConfigurationBean conf, ModelMap model, HttpServletRequest req, HttpServletResponse res, Locale locale, @RequestParam(value = "query", defaultValue = "") String query, @RequestParam(value = "output", defaultValue = "") String output) throws UnsupportedEncodingException {

		String requestUrl = req.getRequestURL().toString();
		String request = requestUrl.replaceAll("/sparql$", "");
		request = request.replaceAll(new UrlPathHelper().getContextPath(req), "");

		if (query.equals("")) {

			/* form page */
			String user = conf.getSingleConfValueForSubject(request, "user", null);

			if (user == null) {
				return new ErrorController().error404(res, model, "sorry, no endpoint configured");
			}

			String staticPath = requestUrl.replaceAll("/$", "") + "/../../" + conf.getStaticResources();

			model.addAttribute("user", user);
			model.addAttribute("staticPath", staticPath);

			return "form";
		} else {

			/* query result */

			return query(conf, model, req, res, locale, query, output);
		}
	}

	public Object query(ConfigurationBean conf, ModelMap model, HttpServletRequest req, HttpServletResponse res, Locale locale, String query, String output) throws UnsupportedEncodingException {

		/* guessing the content Lang */

		String[] acceptedContent = req.getHeader("Accept").split(",");
		AcceptList a = AcceptList.create(acceptedContent);
		MediaType matchItem = AcceptList.match(offeringRDF, a);
		Lang lang = matchItem != null ? RDFLanguages.contentTypeToLang(matchItem.getContentType()) : null;
		/* content type override */
		if (!output.equals("")) {
			try {
				output = output.replaceAll("([a-zA-Z]) ([a-zA-Z])", "$1+$2");
				a = AcceptList.create(output.split(","));
				matchItem = AcceptList.match(offeringRDF, a);
				lang = RDFLanguages.contentTypeToLang(matchItem.getContentType());
			} catch (Exception e) {
				return new ErrorController().error406(res, model);
			}
		}

		try {
			if (lang == null) {
				matchItem = AcceptList.match(offeringResources, a);
				if (matchItem != null) {

					/* return HTML content */

					return null;
				} else {
					return new ErrorController().error406(res, model);
				}
			} else {

				/* return RDF content */

				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null && e.getMessage().startsWith("404")) {
				return new ErrorController().error404(res, model, e.getMessage());
			} else {
				return new ErrorController().error500(res, model, e.getMessage());
			}
		}

	}

}
