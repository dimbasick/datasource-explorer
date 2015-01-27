package com.mde.test.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

public class DataCrawler extends JNDIConnector {
	
	private Catalog catalog = null;
	
	public DataCrawler(String jndi) {
		super(jndi);
		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		options.setSchemaInfoLevel(SchemaInfoLevel.standard());
		options.setRoutineInclusionRule(new ExcludeAll());
		try {
			System.out.println("Connection is null : " + getOracleConnection() == null);
			catalog = SchemaCrawlerUtility.getCatalog(getOracleConnection(), options);
		} catch (SchemaCrawlerException e) {
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
	
	public boolean hasCatalog() {
		return this.catalog != null;
	}

}