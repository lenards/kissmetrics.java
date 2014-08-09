package net.lenards;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class KMClient {
	private String hostUrl;
	private String apiKey; 
	
	public KMClient(String key) {
		this(key, "http://trk.kissmetrics.com");
	}
	
	public KMClient(String key, String trackingHost) {
			hostUrl = trackingHost;
			apiKey = key;
	}
	
	public boolean record(String person, String event) {
		return record(person, event, null, -1);
	}
	
	public boolean record(String person, String event, Map<String, String> properties) {
		return record(person, event, properties, -1);
	}
	
	public boolean record(String person, String event, Map<String, String> properties, long timestamp) {
		// TODO add some empty value checks and whatnot...
		Map<String, String> params = new HashMap<String, String>();
		params.put(TrackingRequest.API_KEY, this.apiKey);
		params.put(TrackingRequest.PERSON_KEY, person);
		params.put(TrackingRequest.EVENTNAME_KEY, event);
		if (properties != null && properties.size() > 0) {
			params.putAll(properties);
		}
		if (timestamp > 0) {
			params.put(TrackingRequest.TIMESTAMP_KEY, ""+timestamp);
			params.put(TrackingRequest.TIME_FLAG_KEY, ""+1);
		}
		int status = TrackingRequest.record(this.hostUrl, params);
		return status == 200;

	}
		
	public boolean set(String person, Map<String, String> properties) {
		return set(person, properties, -1);
	}
	
	public boolean set(String person, Map<String, String> properties, long timestamp) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(TrackingRequest.API_KEY, this.apiKey);
		params.put(TrackingRequest.PERSON_KEY, person);
		if (timestamp > 0) {
			params.put(TrackingRequest.TIMESTAMP_KEY, ""+timestamp);
			params.put(TrackingRequest.TIME_FLAG_KEY, ""+1);
		}
		params.putAll(properties);
		int status = TrackingRequest.set(this.hostUrl, params);
		return status == 200;
	}
	
	public boolean alias(String person, String identity) {
		// TODO need to verify that neither is empty before performing operation
		Map<String, String> params = new HashMap<String, String>();
		params.put(TrackingRequest.API_KEY, this.apiKey);
		params.put(TrackingRequest.PERSON_KEY, person);
		params.put(TrackingRequest.EVENTNAME_KEY, identity);
		int status = TrackingRequest.alias(this.hostUrl, params);
		return status == 200;
	}
}

class TrackingRequest {
	// KISSmetrics Tracking API keys
	public static final String API_KEY = "_k";
	public static final String PERSON_KEY = "_p";
	public static final String EVENTNAME_KEY = "_n";
	public static final String TIMESTAMP_KEY = "_t";
	public static final String TIME_FLAG_KEY = "_d";
	public static final String ALIAS_KEY = "_a";
	// KISSmetrics Tracking Service Paths
	public static final String RECORD_PATH = "e";
	public static final String SET_PATH = "s";
	public static final String ALIAS_PATH = "a";
	
	public static final String charset = "UTF-8";

	public static int record(String hostUrl, Map<String, String> parameters) {
		return request(RECORD_PATH, hostUrl, parameters);
	}

	public static int set(String hostUrl, Map<String, String> parameters) {
		return request(SET_PATH, hostUrl, parameters);
	}

	public static int alias(String hostUrl, Map<String, String> parameters) {
		return request(ALIAS_PATH, hostUrl, parameters);
	}

	private static int request(String operation, String hostUrl, Map<String, String> parameters) {
		String queryString = formatQueryString(parameters).toString();

		try {
			HttpURLConnection conn = (HttpURLConnection)new URL(hostUrl + "/" + operation + "?" + queryString).openConnection();
			return conn.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 418; // I am a TEAPOT!
	}

	public static StringBuilder formatQueryString(Map<String, String> parameters) {
		StringBuilder qs = new StringBuilder();
		for (Map.Entry<String, String> kv : parameters.entrySet()) {
			String encodedKey;
			String encodedVal;
			try {
				encodedKey = URLEncoder.encode(kv.getKey(), charset);
				encodedVal = URLEncoder.encode(kv.getValue(), charset);
				qs.append(encodedKey);
				qs.append('=');
				qs.append(encodedVal);
				qs.append('&');
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(qs.length() > 0 && qs.charAt(qs.length() - 1) == '&') {
			qs.deleteCharAt(qs.length() - 1);
		}
		return qs;
	}
}