package gq.glowman554.mikki.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import gq.glowman554.mikki.utils.Log;

public class HttpClient
{
	public static String get(String _url, Map<String, String> headers) throws IOException
	{
		Log.log("GET " + _url);
		
		URL url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		con.setRequestProperty("Accept", "application/json");
		for (String key : headers.keySet())
		{
			con.setRequestProperty(key, headers.get(key));
		}
		if (con.getResponseCode() != 200)
		{
			throw new IllegalStateException("HTTP " + con.getResponseCode());
		}
		
		String response = "";
		for (byte b : con.getInputStream().readAllBytes())
		{
			response += (char) b;
		}
		con.getInputStream().close();
		con.disconnect();
		return response;

	}

	public static String get(String _url) throws IOException
	{
		return get(_url, new HashMap<>());
	}

	public static String post(String _url, String body, Map<String, String> headers) throws IOException
	{
		Log.log("POST " + _url);

		URL url = new URL(_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-Length", String.valueOf(body.length()));
		for (String key : headers.keySet())
		{
			con.setRequestProperty(key, headers.get(key));
		}
		
		con.getOutputStream().write(body.getBytes());
		
		if (con.getResponseCode() != 200)
		{
			throw new IllegalStateException("HTTP " + con.getResponseCode());
		}
		
		
		String response = "";
		for (byte b : con.getInputStream().readAllBytes())
		{
			response += (char) b;
		}
		con.getInputStream().close();
		con.disconnect();
		return response;

	}

	public static String post(String _url, String body) throws IOException
	{
		return post(_url, body, new HashMap<>());
	}
}
