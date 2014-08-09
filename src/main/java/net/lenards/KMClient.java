package net.lenards;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.util.Map;

public class KMClient {
	private URL hostUrl;
	private String apiKey; 
	
	public KMClient(String key) {
		this(key, "http://trk.kissmetrics.com");
	}
	
	public KMClient(String key, String trackingHost) {
		// oh ... I got how URL can blow up with a malformed URL exception...
		try {
			hostUrl = new URL(trackingHost);
			apiKey = key;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// UGH ... 
		}
	}
	
	public boolean record(String person, String event) {
		return false;
	}
	
	public boolean record(String person, String event, Map<String, String> properties) {
		return false;
	}
	
	public boolean record(String person, String event, Map<String, String> properties, long timestamp) {
		return false;
	}
		
	public boolean set(String person, Map<String, String> properties) {
		return false;
	}
	
	public boolean set(String person, Map<String, String> properties, long timestamp) {
		return false;
	}
	
	public boolean alias(String person, String identity) {
		return false;
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