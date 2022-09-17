package gq.glowman554.mikki.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;

import gq.glowman554.mikki.api.data.MikkiAccount;
import gq.glowman554.mikki.api.data.MikkiChanges;
import gq.glowman554.mikki.api.data.MikkiList;
import gq.glowman554.mikki.api.data.MikkiPage;
import gq.glowman554.mikki.api.data.MikkiToken;
import gq.glowman554.mikki.utils.Log;
import gq.glowman554.reflex.Reflex;
import gq.glowman554.reflex.loaders.ReflexJsonLoader;
import net.shadew.json.JsonNode;

public class Mikki extends Thread
{
	private final String base_url = "https://mikki.deno.dev/api/v2";
	private ProgressReporter pr = null;

	private HashMap<String, MikkiPage> page_cache = new HashMap<>();

	@SuppressWarnings("deprecation")
	public MikkiList list() throws IOException, IllegalArgumentException, IllegalAccessException
	{
		String response = HttpClient.get(this.base_url + "/wiki/page/list");

		return (MikkiList) new Reflex(new ReflexJsonLoader(URLDecoder.decode(response))).load(new MikkiList());
	}

	@SuppressWarnings("deprecation")
	public MikkiPage get(String page_id) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		if (page_cache.containsKey(page_id))
		{
			return page_cache.get(page_id);
		}
		else
		{
			String response = HttpClient.get(this.base_url + "/wiki/page/get?page_id=" + page_id);
			MikkiPage page = (MikkiPage) new Reflex(new ReflexJsonLoader(URLDecoder.decode(response))).load(new MikkiPage());
			page_cache.put(page.page_id, page);
			return page;
		}
	}

	@SuppressWarnings("deprecation")
	public MikkiPage update(String page_id, String page_text, String page_title, String token) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		Log.log(page_text);
		String response = HttpClient.post(String.format(this.base_url + "/wiki/page/edit?token=%s&page_id=%s&page_title=%s", token, page_id, page_title), page_text);
		MikkiPage page = (MikkiPage) new Reflex(new ReflexJsonLoader(URLDecoder.decode(response))).load(new MikkiPage());

		page_cache.remove(page.page_id);
		return page;
	}

	@SuppressWarnings("deprecation")
	public MikkiPage create(String page_text, String page_title, String token) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		String response = HttpClient.post(String.format(this.base_url + "/wiki/page/create?token=%s&page_title=%s", token, page_title), page_text);
		MikkiPage page = (MikkiPage) new Reflex(new ReflexJsonLoader(URLDecoder.decode(response))).load(new MikkiPage());

		return page;
	}

	public void delete(String page_id, String token) throws IOException
	{
		String response = HttpClient.get(String.format(this.base_url + "/wiki/page/delete?token=%s&page_id=%s", token, page_id));
		Log.log(response);
	}

	@SuppressWarnings("deprecation")
	public MikkiChanges changelog() throws IOException, IllegalArgumentException, IllegalAccessException
	{
		String response = HttpClient.get(this.base_url + "/wiki/page/changelog");

		return (MikkiChanges) new Reflex(new ReflexJsonLoader(URLDecoder.decode(response))).load(new MikkiChanges());
	}

	public MikkiToken login(String username, String password) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		JsonNode obj = JsonNode.object();
		obj.set("username", username);
		obj.set("password", password);

		String response = HttpClient.post(this.base_url + "/acc/login", obj.toString());

		return (MikkiToken) new Reflex(new ReflexJsonLoader(response)).load(new MikkiToken());
	}

	public MikkiToken create_account(String username, String password) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		JsonNode obj = JsonNode.object();
		obj.set("username", username);
		obj.set("password", password);

		String response = HttpClient.post(this.base_url + "/acc/create", obj.toString());

		return (MikkiToken) new Reflex(new ReflexJsonLoader(response)).load(new MikkiToken());
	}

	public void delete_account(String token) throws IOException
	{
		String response = HttpClient.post(this.base_url + "/acc/delete", token);
		Log.log(response);
	}

	public MikkiAccount info(String token) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		String response = HttpClient.post(this.base_url + "/acc/info", token);

		return (MikkiAccount) new Reflex(new ReflexJsonLoader(response)).load(new MikkiAccount());
	}

	public MikkiAccount chpasswd(String token, String new_password) throws IOException, IllegalArgumentException, IllegalAccessException
	{
		JsonNode obj = JsonNode.object();
		obj.set("token", token);
		obj.set("password", new_password);

		String response = HttpClient.post(this.base_url + "/acc/chpasswd", obj.toString());

		return (MikkiAccount) new Reflex(new ReflexJsonLoader(response)).load(new MikkiAccount());
	}

	public boolean check(String token) throws IOException
	{
		String response = HttpClient.post(this.base_url + "/acc/check", token);

		return response.equals("true");
	}

	public void clean_cache()
	{
		Log.log("Deleting cache...");
		page_cache.clear();
	}

	@Override
	public void run()
	{
		Log.log("Starting to preload pages...");
		try
		{
			var pages = this.list();

			for (int i = 0; i < pages.$.len(); i++)
			{
				if (page_cache.containsKey(pages.$.get(i).page_id))
				{
					Log.log(String.format("%s already in cache. Skipping...", pages.$.get(i).page_id));
				}
				var page = this.get(pages.$.get(i).page_id);

				page_cache.put(page.page_id, page);

				if (pr != null)
				{
					pr.update(i + 1);
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException | IOException e)
		{
			e.printStackTrace();
		}

		Log.log("Prload finished.");
	}

	public void setPr(ProgressReporter pr)
	{
		this.pr = pr;
	}

	public static interface ProgressReporter
	{
		void update(int prg);
	}
}
