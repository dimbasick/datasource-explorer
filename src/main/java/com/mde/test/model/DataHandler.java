package com.mde.test.model;

import java.sql.Connection;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.jdbc.OracleConnection;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

public class DataHandler {
	
	private Catalog catalog = null;
	
	public DataHandler(OracleConnection c) {
		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		options.setSchemaInfoLevel(SchemaInfoLevel.standard());
		options.setRoutineInclusionRule(new ExcludeAll());
		try {
			catalog = SchemaCrawlerUtility.getCatalog(c, options);
		} catch (SchemaCrawlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getSchemas() {
		List<String> schemas = new LinkedList<String>();
		for (final Schema schema : catalog.getSchemas()) 
			schemas.add(schema.getName());
		return schemas;
	}
	
	public List<String> getTables(String schemaName) {
		List<String> res = new LinkedList<String>();
		Schema schema = getSchemaByName(schemaName);
		Collection<Table> tables = catalog.getTables(schema);
		for (final Table table: tables)
			res.add(table.getName());
		return res;
	}

	public Schema getSchemaByName(String schemaName) {
		for (final Schema schema : catalog.getSchemas())
			if (schema.getName().equals(schemaName))
				return schema;
		return null;
	}
	
	public List<Column> getColumns(String schemaName, String tableName) {
		Schema schema = getSchemaByName(schemaName);
		Table table = getTable(schema, tableName);
		return table.getColumns();	
	}
	
	public Table getTable(Schema schema, String tableName) {
		for (final Table table: catalog.getTables(schema))
			if (table.getName().equals(tableName))
				return table;
		return null;
	}
	
	public static Connection getConnection(String jndiName) throws Throwable {
		Hashtable<String, String> env = getEnv();
		try {
			Context context = new InitialContext(env);
			DataSource ds = (DataSource)context.lookup (jndiName);
			return ds.getConnection();
		} catch(Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private static Hashtable<String, String> getEnv() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
		return env;
	}
	
	public static void closeResource(AutoCloseable res) {
		try {
			res.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
