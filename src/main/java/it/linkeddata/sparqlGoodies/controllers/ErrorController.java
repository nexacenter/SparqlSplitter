package it.linkeddata.sparqlGoodies.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorController {

	/* TODO: change the handler to send "error" param to the client */
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "unhandled encoding")
	@RequestMapping(value = "/406")
	public String error406(HttpServletResponse res, ModelMap model) {
		model.addAttribute("statusCode", "406");
		res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		return "error";
	}

	@RequestMapping(value = "/404")
	public String error404(HttpServletResponse res, ModelMap model, @RequestParam(value = "error", defaultValue = "") String error) {
		model.addAttribute("error", error);
		model.addAttribute("statusCode", "404");
		res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return "error";
	}

	@RequestMapping(value = "/400")
	public String error400(HttpServletResponse res, ModelMap model) {
		model.addAttribute("statusCode", "400");
		res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		return "error";
	}

	@RequestMapping(value = { "/500", "/error" })
	public String error500(HttpServletResponse res, ModelMap model, @RequestParam(value = "error", defaultValue = "") String error) {
		model.addAttribute("error", error);
		model.addAttribute("statusCode", "500");
		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return "error";
	}

}