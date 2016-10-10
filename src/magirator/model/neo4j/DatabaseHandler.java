package magirator.model.neo4j;

public class DatabaseHandler {
	
	private String url = "jdbc:neo4j:bolt://localhost:7474";
	private String user = "neo4j";
	private String password = "neo";
	
	public DatabaseParams getDatabaseParameters(){
		return new DatabaseParams(this.url, this.user, this.password);		
	}
}

