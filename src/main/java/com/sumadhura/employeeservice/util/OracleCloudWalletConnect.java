package com.sumadhura.employeeservice.util;

/* Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.*/

/*
 DESCRIPTION
 The code sample demonstrates Universal Connection Pool (UCP) as a client
 side connection pool and does the following.    
 (a)Set the connection factory class name to 
 oracle.jdbc.pool.OracleDataSource before getting a connection.   
 (b)Set the driver connection properties(e.g.,defaultNChar,includeSynonyms).
 (c)Set the connection pool properties(e.g.,minPoolSize, maxPoolSize). 
 (d)Get the connection and perform some database operations.     

 Step 1: Enter the Database details in DBConfig.properties file. 
 USER, PASSWORD, UCP_CONNFACTORY and URL are required.                   
 Step 2: Run the sample with "ant UCPSample"

 NOTES
 Use JDK 1.7 and above  

 MODIFIED    (MM/DD/YY)
 nbsundar    02/13/15 - Creation (Contributor - tzhou)
 */
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

/*import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;*/
public class OracleCloudWalletConnect {
	// final static String DB_URL =
	// "jdbc:oracle:thin:@amsatpdb_high?TNS_ADMIN=H:/ACP/Docs/Add-ons/Cloud
	// wallet/Wallet_AMSATPDB";
	final static String DB_URL = "jdbc:oracle:thin:@amsatpdb_high?TNS_ADMIN=H:/ACP/Docs/Add-ons/Cloud wallet/amsatpdb_high";
	// final static String DB_URL =
	// "jdbc:oracle:thin:@amsatpdb_high?TNS_ADMIN=H:/ACP/Docs/Add-ons/Cloud
	// wallet/Wallet_AMSATPDB.zip";
	// final static String DB_URL =
	// "jdbc:oracle:thin:@AMSATPDB_high?TNS_ADMIN=H:/ACP/Docs/Add-ons/Cloud
	// wallet/amsatpdb_high";
//  final static String DB_URL=   "jdbc:oracle:thin:@myhost:1521/myorcldbservicename";
	// Use TNS alias when using tnsnames.ora. Use it while connecting to the
	// database service on cloud.
	// final static String DB_URL= "jdbc:oracle:thin:@orcldbaccess";
	final static String DB_USER = "admin";
	final static String DB_PASSWORD = "AMSdatabase01_#";
	final static String CONN_FACTORY_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource";

	
/*	public DataSource getDataSource() throws SQLException {
		// Get the PoolDataSource for UCP
		PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
		
		//oracle.ucp.jdbc.PoolXADataSource;
		// Set the connection factory first before all other properties
		pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
		pds.setURL(DB_URL);
		pds.setUser(DB_USER);
		pds.setPassword(DB_PASSWORD);
		pds.setConnectionPoolName("JDBC_UCP_POOL");

		// Default is 0. Set the initial number of connections to be created
		// when UCP is started.
		pds.setInitialPoolSize(5);

		// Default is 0. Set the minimum number of connections
		// that is maintained by UCP at runtime.
		pds.setMinPoolSize(5);

		// Default is Integer.MAX_VALUE (2147483647). Set the maximum number of
		// connections allowed on the connection pool.
		pds.setMaxPoolSize(20);

		// Default is 30secs. Set the frequency in seconds to enforce the timeout
		// properties. Applies to inactiveConnectionTimeout(int secs),
		// AbandonedConnectionTimeout(secs)& TimeToLiveConnectionTimeout(int secs).
		// Range of valid values is 0 to Integer.MAX_VALUE. .
		pds.setTimeoutCheckInterval(5);

		// Default is 0. Set the maximum time, in seconds, that a
		// connection remains available in the connection pool.
		pds.setInactiveConnectionTimeout(10);

		// Set the JDBC connection properties after pool has been created
		Properties connProps = new Properties();
		connProps.setProperty("fixedString", "false");
		connProps.setProperty("remarksReporting", "false");
		connProps.setProperty("restrictGetTables", "false");
		connProps.setProperty("includeSynonyms", "false");
		connProps.setProperty("defaultNChar", "false");
		connProps.setProperty("AccumulateBatchResult", "false");

		DataSource dt = pds;
		return dt;
	}
*/	
	/*
	 * The sample demonstrates UCP as client side connection pool.
	 */
	public static void main(String args[]) throws Exception {/*
		// Get the PoolDataSource for UCP
		PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
		
		//oracle.ucp.jdbc.PoolXADataSource;
		// Set the connection factory first before all other properties
		pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
		pds.setURL(DB_URL);
		pds.setUser(DB_USER);
		pds.setPassword(DB_PASSWORD);
		pds.setConnectionPoolName("JDBC_UCP_POOL");

		// Default is 0. Set the initial number of connections to be created
		// when UCP is started.
		pds.setInitialPoolSize(5);

		// Default is 0. Set the minimum number of connections
		// that is maintained by UCP at runtime.
		pds.setMinPoolSize(5);

		// Default is Integer.MAX_VALUE (2147483647). Set the maximum number of
		// connections allowed on the connection pool.
		pds.setMaxPoolSize(20);
//		oracle.jdbc.pool.OracleDataSource;
		// Default is 30secs. Set the frequency in seconds to enforce the timeout
		// properties. Applies to inactiveConnectionTimeout(int secs),
		// AbandonedConnectionTimeout(secs)& TimeToLiveConnectionTimeout(int secs).
		// Range of valid values is 0 to Integer.MAX_VALUE. .
		pds.setTimeoutCheckInterval(5);

		// Default is 0. Set the maximum time, in seconds, that a
		// connection remains available in the connection pool.
		pds.setInactiveConnectionTimeout(10);

		// Set the JDBC connection properties after pool has been created
		Properties connProps = new Properties();
		connProps.setProperty("fixedString", "false");
		connProps.setProperty("remarksReporting", "false");
		connProps.setProperty("restrictGetTables", "false");
		connProps.setProperty("includeSynonyms", "false");
		connProps.setProperty("defaultNChar", "false");
		connProps.setProperty("AccumulateBatchResult", "false");

		DataSource dt = pds;
		
		
		 * System.setProperty(
		 * "oracle.net.tns_admin","H:\\ACP\\Docs\\Add-ons\\Cloud wallet\\amsatpdb_high\\tnsnames.ora"
		 * );
		 * 
		 * connProps.setProperty(OracleConnection.CONNECTION_PROPERTY_FAN_ENABLED,
		 * "oracle.jdbc.fanEnabled"); connProps.setProperty(OracleConnection.
		 * CONNECTION_PROPERTY_FAN_ENABLED_DEFAULT, "false");
		 

		// JDBC connection properties will be set on the provided
		// connection factory.
		pds.setConnectionProperties(connProps);
		System.out.println("Available connections before checkout: " + pds.getAvailableConnectionsCount());
		System.out.println("Borrowed connections before checkout: " + pds.getBorrowedConnectionsCount());
		
		
		// Get the database connection from UCP.
		try (Connection conn = pds.getConnection()) {
			System.out.println("Available connections after checkout: " + pds.getAvailableConnectionsCount());
			System.out.println("Borrowed connections after checkout: " + pds.getBorrowedConnectionsCount());
			// Perform a database operation
			doSQLWork(conn);
		}
		System.out.println("Available connections after checkin: " + pds.getAvailableConnectionsCount());
		System.out.println("Borrowed connections after checkin: " + pds.getBorrowedConnectionsCount());
	*/}

	/*
	 * Creates an EMP table and does an insert, update and select operations on the
	 * new table created.
	 */
	public static void doSQLWork(Connection conn) {
		try {
			conn.setAutoCommit(false);
			// Prepare a statement to execute the SQL Queries.
			Statement statement = conn.createStatement();
			// Create table EMP
			statement.executeUpdate("create table EMP(EMPLOYEEID NUMBER," + "EMPLOYEENAME VARCHAR2 (20))");
			System.out.println("New table EMP is created");
			// Insert some records into the table EMP
			statement.executeUpdate("insert into EMP values(1, 'Jennifer Jones')");
			statement.executeUpdate("insert into EMP values(2, 'Alex Debouir')");
			System.out.println("Two records are inserted.");

			// Update a record on EMP table.
			statement.executeUpdate("update EMP set EMPLOYEENAME='Alex Deborie'" + " where EMPLOYEEID=2");
			System.out.println("One record is updated.");

			// Verify the table EMP
			ResultSet resultSet = statement.executeQuery("select * from EMP");
			System.out.println("\nNew table EMP contains:");
			System.out.println("EMPLOYEEID" + " " + "EMPLOYEENAME");
			System.out.println("--------------------------");
			while (resultSet.next()) {
				System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));
			}
			System.out.println("\nSuccessfully tested a connection from UCP");
		} catch (SQLException e) {
			System.out.println("UCPSample - " + "doSQLWork()- SQLException occurred : " + e.getMessage());
		} finally {
			// Clean-up after everything
			try (Statement statement = conn.createStatement()) {
				statement.execute("drop table EMP");
			} catch (SQLException e) {
				System.out.println("UCPSample - " + "doSQLWork()- SQLException occurred : " + e.getMessage());
			}
		}
	}
}