package com.mde.test.model;

public class TestObject {
	
	private String schema;
	private String table;
	public TestObject(String schema, String table) {
		super();
		this.schema = schema;
		this.table = table;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
}
