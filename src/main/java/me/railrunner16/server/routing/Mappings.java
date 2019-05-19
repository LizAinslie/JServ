package me.railrunner16.server.routing;

import java.util.HashMap;
import java.util.Iterator;

import me.railrunner16.server.request.Request;
import me.railrunner16.server.response.AbstractResponse;

public class Mappings{
	private HashMap<String, AbstractResponse> urlMappings;

	public Mappings(){
		this.urlMappings = new HashMap<String, AbstractResponse>();
	}

	public AbstractResponse getMap(String key){
		return urlMappings.get(key);
	}

	public void addMap(Route r, AbstractResponse resp){
		urlMappings.put(r.getMethod().toString() + "_" + r.getUri(), resp);
	}

	private String replaceRequestAttribute(String res, Request req){
		Iterator itr = req.getAttributeIterator();

		while(itr.hasNext()) {
			String key = (String) itr.next();
			String val = req.getAttribute(key);
			res = res.replace("${" + key + "}", val);
		}
		
		return res;
	}

	private int indexOfAfter(String str, String toSearch, int after){
		str = str.substring(after);
		return after + str.indexOf(toSearch);
	}
}
