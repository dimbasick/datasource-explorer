package com.mde.test;

import java.util.Collection;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mde.test.model.DataHandler;

import schemacrawler.schema.Column;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private static DataHandler data = null;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
	
	@RequestMapping(value = "/schemas", method = RequestMethod.GET)
	public ModelAndView schemas(Locale locale, @RequestParam("jndi") String jndi) throws Throwable {
		data = new DataHandler(jndi);
		ModelAndView model = new ModelAndView("schemas");
		model.addObject("jndi", jndi);
		model.addObject("list", data.getSchemas());
		return model;
	}
	
	@RequestMapping(value = "/tables", method = RequestMethod.GET)
	public ModelAndView tables(Locale locale, @RequestParam("schema") String schema) throws Throwable {
		ModelAndView model = new ModelAndView("tables");
		model.addObject("schema", schema);
		model.addObject("list", data.getTables(schema));
		return model;
	}
	
	@RequestMapping(value = "/columns", method = RequestMethod.GET)
	public ModelAndView columns(Locale locale, 
								@RequestParam("schema") String schema,
								@RequestParam("table") String table) throws Throwable {
		ModelAndView model = new ModelAndView("columns");
		model.addObject("schema", schema);
		model.addObject("table", table);
		Collection<Column> columns = data.getColumns(schema, table);
		model.addObject("list", columns);
		return model;
	}
	
}
