package com.project.ams;

import java.io.*;
import java.lang.*;
import java.net.*;
import java.security.*;
import java.sql.*;
import java.text.*;
import java.util.*;



import java.util.Date;
import java.security.MessageDigest;


public class silcoreDBConnectionConstants
{
  public static String
	  _classNamePSQL	="org.postgresql.Driver",
		//_dbURLPSQL  = "jdbc:postgresql://mydatabase.ct9lh7rafpaw.ap-southeast-1.rds.amazonaws.com:5432/csirnew",
	   _dbURLPSQL1  = "jdbc:postgresql://localhost:5432/demodb",
	   _userNamePSQL	="postgres",
	   _passwordPSQL	="smit123";
 

public static synchronized  ResultSet retrieveData(String query  )	{
	ResultSet res	=null;
	try	{
	Connection con = null;
	Class.forName(silcoreDBConnectionConstants._classNamePSQL); 
	con = DriverManager.getConnection(silcoreDBConnectionConstants._dbURLPSQL1, _userNamePSQL, _passwordPSQL);
	Statement stmt	=	null;
	stmt 		= 	con.createStatement();
	res	=	stmt.executeQuery(query);
	con.close();
	}
	catch(Exception e)	{
		System.out.println( e );
		return res;
	}
	return res;
}


public static synchronized  int insertPOSTGresSqlData(String query  )	{
	int inserted = 0;
	try	{
	Connection con = null;
	Class.forName(silcoreDBConnectionConstants._classNamePSQL); 
		con = DriverManager.getConnection(silcoreDBConnectionConstants._dbURLPSQL1, _userNamePSQL, _passwordPSQL);
	Statement stmt	=	null;
	stmt 		= 	con.createStatement();
	inserted	=	stmt.executeUpdate(query);
	con.close();
	}
	catch(Exception e)	{
		System.out.println( e );
		return inserted;
	}
	return inserted;
}
 

 public static synchronized  int insertPOSTGresSqlData1(String query  )	{
	int inserted = 0;
	try	{
	Connection con = null;
	Class.forName(silcoreDBConnectionConstants._classNamePSQL); 
		con = DriverManager.getConnection(silcoreDBConnectionConstants._dbURLPSQL1, _userNamePSQL, _passwordPSQL);
	Statement stmt	=	null;
	stmt 		= 	con.createStatement();
	inserted	=	stmt.executeUpdate(query);
	con.close();
	}
	catch(Exception e)	{
		System.out.println( e );
		return inserted;
	}
	return inserted;
}
 public static synchronized  int insertOracleDBData(String query)	{
	int inserted = 0;
	try	{
	Connection con = null;
	Class.forName(silcoreDBConnectionConstants._classNamePSQL); 
		con = DriverManager.getConnection(silcoreDBConnectionConstants._dbURLPSQL1, _userNamePSQL, _passwordPSQL);
	Statement stmt	=	null;
	stmt 		= 	con.createStatement();
	inserted	=	stmt.executeUpdate(query);
	stmt.close();
	con.close();
	}
	catch(Exception e)	{
		System.out.println( e );
		return inserted;
	}
	return inserted;
}



}
