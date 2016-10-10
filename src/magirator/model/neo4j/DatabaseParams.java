package magirator.model.neo4j;

public class DatabaseParams {

	private String connectionString;
	private String username;
	private String password;
	
	public DatabaseParams(String cs, String un, String pw){
		this.connectionString = cs;
		this.username = un;
		this.password = pw;
	}
	
	public String getConnectionString(){
		return this.connectionString;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
}
