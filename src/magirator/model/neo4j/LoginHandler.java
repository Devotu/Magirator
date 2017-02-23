package magirator.model.neo4j;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class LoginHandler {
	
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	public LoginHandler(){}

	public int login(String username, String password) throws Exception {
	
		try {	
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (n:User) WHERE n.name = ? RETURN n.password";

			PreparedStatement ps = con.prepareStatement(query);
      		ps.setString(1, username);

      		rs = ps.executeQuery();
		
			String passwordFromDatabase = "none";
		
			if (rs.next()) {
				passwordFromDatabase = rs.getString("n.password");
			}
			
			if (password.equals(passwordFromDatabase)) {
				
				query = "MATCH (n:User) WHERE n.name = ? RETURN id(n)";
				ps = con.prepareStatement(query);
				ps.setString(1, username);

				rs = ps.executeQuery();
				
				if (rs.next()) {
					return rs.getInt(1);
				} else { 
					return -1;
				}

			} else {
				return -1;
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
