package magirator.model.neo4j;

import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;

import java.io.*;

public class SignupHandler {
	
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	public String signup(String username, String password) throws Exception {
	
		try {	
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (n:User) WHERE n.name = ? RETURN id(n)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, username);

			rs = ps.executeQuery();
				
			if (!rs.next()) {
				query = "CREATE (n:User { name: ?, password: ? })";

				ps = con.prepareStatement(query);

				ps.setString(1, username);
				ps.setString(2, password);
			
				ps.executeUpdate();

				return username;
			} else {
				return null;
			}			
		
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}
}
