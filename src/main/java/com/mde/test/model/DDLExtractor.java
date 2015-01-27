package com.mde.test.model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DDLExtractor extends JNDIConnector {
	
	public DDLExtractor(String jndi) {
		super(jndi);
		System.out.println("DDL Extractor constructor finished");
	}

	public String getDDL(String... sql) {
		StringBuilder res = new StringBuilder();
		try {
			for (int i = 0; i < sql.length - 1; ++i) {
				CallableStatement stmt = getOracleConnection().prepareCall(sql[i]);
				stmt.execute();
			}
			PreparedStatement stmt = getOracleConnection().prepareStatement(sql[sql.length - 1]);
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
			CallableStatement stmt = getOracleConnection().prepareCall(query);
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
			CallableStatement stmt = getOracleConnection().prepareCall(query);
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