package solar;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.labs.repackaged.org.json.*;

public class PanelServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PanelServlet.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reqOption = request.getParameter("option");
		
		JSONArray jsonArray = new JSONArray();
		JSONObject returnJsonObj = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		if (reqOption.equals("addPanel")) {		
			String manufacturer = "";
			String model = "";
			Integer power = 0;
			Integer length = 0;
			Integer width = 0;
			String powerStr = "";
			String lengthStr = "";
			String widthStr = "";
			
			boolean validInput = false;
	
			try {
				manufacturer = request.getParameter("manufacturer");
				model = request.getParameter("model");
				power = Integer.parseInt(request.getParameter("power"));			
				length = Integer.parseInt(request.getParameter("newPanelLength"));			
				width = Integer.parseInt(request.getParameter("newPanelWidth"));
				
				validInput = true;
			}
			catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			powerStr = power.toString();
			lengthStr = length.toString();
			widthStr = width.toString();			
			
			if (validInput) {
				
				Key panelKey = KeyFactory.createKey("Panel", model);
				
				Entity panel = new Entity("Panel", panelKey);
				panel.setProperty("manufacturer", manufacturer);
				panel.setProperty("model", model);
				panel.setProperty("power", powerStr);
				panel.setProperty("length", lengthStr);
				panel.setProperty("width", widthStr);
				
		        datastore.put(panel);
	
		        getJSONArray(jsonArray, returnJsonObj, datastore);
			}			
			else {
				
				// TODO ...................
				
			}		
		}
		else if (reqOption.equals("getPanels")) {
			getJSONArray(jsonArray, returnJsonObj, datastore);
		}
		else if (reqOption.equals("deletePanel")) {
			
			deleteSpec(request, jsonArray, returnJsonObj, datastore, "Panel");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(returnJsonObj.toString());
		
		log.log(Level.WARNING, returnJsonObj.toString());
	}
	
	
	private void deleteSpec(HttpServletRequest request, JSONArray jsonArray, JSONObject returnJsonObj, DatastoreService datastore, String spec) {
		String model = request.getParameter("model");
		
		Query query = new Query(spec);
		Query.Filter filter = new Query.FilterPredicate("model", FilterOperator.EQUAL, model);
		query.setFilter(filter);
		PreparedQuery pq = datastore.prepare(query);
		Entity e = pq.asSingleEntity();
		datastore.delete(e.getKey());
		
		getJSONArray(jsonArray, returnJsonObj, datastore);
	}

	
	private void getJSONArray(JSONArray jsonArray, JSONObject returnJsonObj, DatastoreService datastore) {
		
		Query query = new Query("Panel").addSort("manufacturer", Query.SortDirection.ASCENDING);
		query.getKind();
		List<Entity> panels = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
		
		try {
			if (panels.size() > 0) {
		   
		    	for (Entity panelInList : panels) {
		    		JSONObject panelObj = new JSONObject();	        	
					panelObj.put("manufacturer", panelInList.getProperty("manufacturer"));
					panelObj.put("model", panelInList.getProperty("model"));
					panelObj.put("power", panelInList.getProperty("power") + "W");
					panelObj.put("length", panelInList.getProperty("length"));
					panelObj.put("width", panelInList.getProperty("width"));
					
					jsonArray.put(panelObj);
		    	}
		        
		        returnJsonObj.put("Panels", jsonArray);
		        returnJsonObj.put("Success", true);
			}
			else {
				returnJsonObj.put("Success", "empty");
			}
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
