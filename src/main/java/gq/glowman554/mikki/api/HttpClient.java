package gq.glowman554.mikki.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import gq.glowman554.mikki.utils.Log;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient
{
	public static String get(String _url, Map<String, String> headers) throws IOException
	{
		Log.log("GET " + _url);

		OkHttpClient client = new OkHttpClient();

		var req = new Request.Builder();

		req.url(_url);

		req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		req.addHeader("Accept", "application/json");

		for (String key : headers.keySet())
		{
			req.addHeader(key, headers.get(key));
		}

		Response res = client.newCall(req.build()).execute();

		assert res.body() != null;
		return res.body().string();

	}

	public static String get(String _url) throws IOException
	{
		return get(_url, new HashMap<>());
	}

	@SuppressWarnings("deprecation")
	public static String post(String _url, String body, Map<String, String> headers) throws IOException
	{
		Log.log("POST " + _url);

		OkHttpClient client = new OkHttpClient();

		var req = new Request.Builder();

		req.url(_url);
		req.method("POST", RequestBody.create(MediaType.parse("text/plain"), body));

		req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		req.addHeader("Accept", "application/json");

		for (String key : headers.keySet())
		{
			req.addHeader(key, headers.get(key));
		}

		Response res = client.newCall(req.build()).execute();

		assert res.body() != null;
		return res.body().string();

	}

	public static String post(String _url, String body) throws IOException
	{
		return post(_url, body, new HashMap<>());
	}
}
