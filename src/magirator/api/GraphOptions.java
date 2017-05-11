package magirator.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import magirator.logic.graph.Options;
import magirator.support.Constants;

public class GraphOptions extends ServerResource {

    @Post("json")
    public String toJson(Representation rep) {
    	
    	JsonObject response = new JsonObject();
    	response.addProperty(Constants.result, "Something went wrong");
		
        try {

        	JsonArray types = Options.getTypes();        	
        	JsonArray conditions = Options.getConditions();
        	
        	response.add("types", types);
        	response.add("conditions", conditions);
        	
        	response.addProperty(Constants.result, "Success");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}