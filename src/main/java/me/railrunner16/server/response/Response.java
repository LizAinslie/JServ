package me.railrunner16.server.response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;
import me.railrunner16.server.util.file.MimeType;

@NoArgsConstructor
public final class Response {
	private String resp = null;
	private Map<String, String> headers = new HashMap<>();
	private int status;
	private String body = "";
	private String statusMessage = "OK";
	private MimeType contentType = MimeType.HTML;

	public Response setStatus(int status) {
		this.status = status;
		return this;
	}

	public Response setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		return this;
	}

	public Response setContentType(MimeType contentType) {
		this.contentType = contentType;
		return this;
	}

	public Response addHeader(String headerKey, String headerValue) {
		this.headers.put(headerKey, headerValue);
		return this;
	}

	public Response setBody(String body) {
		this.body = body;
		return this;
	}

	public Response build() {
		Date date = new Date();
		String rawResponse = "";
		rawResponse += "HTTP/1.1 " + this.status + " " + this.statusMessage + "\n";
		rawResponse += "Date: " + date.toString() + "\n";
		rawResponse += "Content-Type: " + this.contentType.getMimeString() + "\n";

		for (Map.Entry<String, String> header : this.headers.entrySet())
			rawResponse += header.getKey() + ": " + header.getValue() + "\n";
		
		if (!this.body.isEmpty()) {
			rawResponse += "Content-length: " + this.body.length() + "\n";
			rawResponse += "\n";
			rawResponse += this.body;
		}

		this.resp = rawResponse;
		return this;
	}

	public String toString() {
		return this.resp;
	}
}