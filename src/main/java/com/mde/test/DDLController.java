package com.mde.test;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mde.test.model.DataHandler;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DDLController {
	
	private static final Logger logger = LoggerFactory.getLogger(DDLController.class);

	private static DataHandler data = null;
	
	@RequestMapping(value = "/ddl", method = RequestMethod.GET)
	public ModelAndView ddl(Locale locale,
							@RequestParam("jndi") String jndi) throws Throwable {
		data = new DataHandler(jndi);
		ModelAndView model = new ModelAndView("ddl");
		String tables = data.getDDL("begin dbms_metadata.set_transform_param(DBMS_METADATA.SESSION_TRANSFORM, 'CONSTRAINTS', true); end;",
									"begin dbms_metadata.set_transform_param(DBMS_METADATA.SESSION_TRANSFORM, 'REF_CONSTRAINTS', false); end;",
									"begin dbms_metadata.set_transform_param(DBMS_METADATA.SESSION_TRANSFORM, 'CONSTRAINTS_AS_ALTER', false); end;",
									"begin dbms_metadata.set_transform_param(DBMS_METADATA.SESSION_TRANSFORM, 'STORAGE', false); end;",
									"begin dbms_metadata.set_transform_param(DBMS_METADATA.SESSION_TRANSFORM, 'SQLTERMINATOR', false); end;",
									"SELECT DBMS_METADATA.GET_DDL('TABLE',u.table_name) FROM USER_TABLES u");
		String indexes = data.getDDL("SELECT DBMS_METADATA.GET_DDL('INDEX',u.index_name) FROM USER_INDEXES u");
		String views = data.getDDL("SELECT DBMS_METADATA.GET_DDL('VIEW',u.view_name) FROM USER_VIEWS u");
		String triggers = data.getDDL("SELECT DBMS_METADATA.GET_DDL('TRIGGER',u.trigger_name) FROM USER_TRIGGERS u");
		String constraints = data.getRefConstraints();
		model.addObject("jndi", jndi);
		model.addObject("ddl", tables + indexes + views + triggers + constraints);
		return model;
	}

}
