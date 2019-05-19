package me.railrunner16.server.request;

import java.util.HashMap;
import java.util.Iterator;

import lombok.Getter;

public final class Request {
	private String req = null;
	
	@Getter
	private RequestMethod method = null;
	
	@Getter
	private String url = null;
	
	@Getter
	private String httpVersion = null;
	
	private HashMap<String,String> attributes;

	public Request(String req) {
		this.req = req;
		attributes = new HashMap<String, String>();
		this.parse();
	}

	private void parse(){
		String[] temp = req.split("\r\n");
		String firstLine = temp[0];
		String[] firstLineSplit = firstLine.split(" ");
		if (firstLineSplit.length == 3) {
			this.method = RequestMethod.valueOf(firstLineSplit[0].toUpperCase());
			this.httpVersion = firstLineSplit[2];
			
			if (this.method.equals(RequestMethod.POST)) {
				this.url = firstLineSplit[1];
				this.setAttributes(temp[temp.length - 1]);
			} else if (this.method.equals(RequestMethod.GET)) {
				String[] arr = firstLineSplit[1].split("[?]");
				
				if (arr.length == 2) {
					this.url = arr[0];
					this.setAttributes(arr[1]);
				} else this.url = firstLineSplit[1];
			} else this.url = firstLineSplit[1];
		}
	}

	/**
	 * Get the attribute iterator.
	 * @return The attribute iterator.
	 */
	public Iterator getAttributeIterator() {
		return this.attributes.keySet().iterator();
	}

	/**
	 * Handle querystring attributes.
	 * @param rawAttributes The raw querystring
	 */
	private void setAttributes(String rawAttributes) {
		String[] attribs = rawAttributes.split("&");
		for(int i = 0; i < attribs.length; i++) {
			String[] attr = attribs[i].split("=");
			if(attr.length == 2) this.setAttribute(attr[0], attr[1].replace("+", " "));
		}
	}

	/**
	 * Get an attribute.
	 * @param key The key of the attribute to get.
	 * @return The value of the attribute with the corresponding key.
	 */
	public String getAttribute(String key) {
		return this.attributes.get(key);
	}

	/**
	 * Set an attribute.
	 * @param key The key of the attribute to set.
	 * @param value The value to set the attribute to.
	 */
	public void setAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	public String toString() {
		return this.req;
	}
}
