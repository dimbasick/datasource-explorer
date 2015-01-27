package com.mde.test.model;

import java.sql.Connection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.jdbc.OracleConnection;

public class JNDIConnector {
	
	private Connection connection = null;
	private OracleConnection oracleConnection = null;
	
	public JNDIConnector(String jndi) {
		System.out.println("Starting JNDIConnector constructor");
		System.out.println("Getting connection");
		connection = getConnection(jndi);
		System.out.println("Getting oracle connection");
		oracleConnection = (OracleConnection) connection;
		System.out.println("JNDIConnector constructor finished");
	}
	
	private static Connection getConnection(String jndiName) {
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
		return this.oracleConnection != null;
	}

	public Connection getConnection() {
		return connection;
	}

	public OracleConnection getOracleConnection() {
		return oracleConnection;
	}

}