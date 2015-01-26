package com.mde.test.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	private Connection connection = null;
	private OracleConnection conn = null;
	private Catalog catalog = null;
	
	public DataHandler(String jndi) {
		connection = getConnection(jndi);
		conn = (OracleConnection) connection;
		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		options.setSchemaInfoLevel(SchemaInfoLevel.standard());
		options.setRoutineInclusionRule(new ExcludeAll());
		try {
			System.out.println("Connection is null : " + conn == null);
			catalog = SchemaCrawlerUtility.getCatalog(conn, options);
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
	
	private Connection getConnection(String jndiName) {
		Hashtable<String, String> env = getEnv();
		try {
			Context context = new InitialContext(env);
			DataSource ds = (DataSource)context.lookup (jndiName);
			return ds.getConnection();
		} catch(Throwable e) {
			e.printStackTrace();
			return null;
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
	
	public boolean hasConnection() {
		return this.conn != null;
	}
	
	public boolean hasCatalog() {
		return this.catalog != null;
	}
	
	public String getDDL(String... sql) {
		StringBuilder res = new StringBuilder();
		try {
			for (int i = 0; i < sql.length - 1; ++i) {
				CallableStatement stmt = conn.prepareCall(sql[i]);
				stmt.execute();
			}
			PreparedStatement stmt = conn.prepareStatement(sql[sql.length - 1]);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				res.append(rs.getString(1)).append("\n");
			return res.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getRefConstraints() {
		List<String> tables = getTables();
		StringBuilder res = new StringBuilder();
		for (String table : tables) {
			String tableConsts = getTableConsts(table);
			if (tableConsts != null)
				res.append(tableConsts);
		}
		return res.toString();		
	}
	
	private List<String> getTables() {
		String query = "select table_name as t_name from user_tables";
		List<String> res = new LinkedList<String>();
		try {
			CallableStatement stmt = conn.prepareCall(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				res.add(rs.getString(1));
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getTableConsts(String table) {
		String query = "select dbms_metadata.get_dependent_ddl('REF_CONSTRAINT', ?) from dual";
		StringBuilder res = new StringBuilder();
		try {
			CallableStatement stmt = conn.prepareCall(query);
			stmt.setString(1, table);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				res.append(rs.getString(1)).append("\n");
			return res.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
