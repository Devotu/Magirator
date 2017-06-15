package magirator.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
	
	public static Connection getConnection() throws NamingException, SQLException{
		
		Context initContext = new InitialContext();
		Context webContext = (Context)initContext.lookup("java:comp/env");
		DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
		return ds.getConnection();
	}

	public static PreparedStatement setStatementParams(PreparedStatement ps, List<Object> params) throws Exception {
		
		int order = 1;
		
		for	(Object o : params){
			
			if(o instanceof Integer){
				
				ps.setInt(order, (int)o);
				
			} else if(o instanceof String){

				ps.setString(order, (String)o);
				
			} else if(o instanceof Boolean){

				ps.setBoolean(order, (boolean)o);
				
			}
			
			order++;
		}
		
		return ps;
	}
}
