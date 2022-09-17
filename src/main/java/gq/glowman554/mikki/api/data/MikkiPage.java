package gq.glowman554.mikki.api.data;

import java.net.URLEncoder;
import java.util.Base64;

import gq.glowman554.reflex.ReflexField;

public class MikkiPage
{
	@ReflexField
	public long page_created;
	@ReflexField
	public long page_edited;
	@ReflexField
	public String page_title;

	@ReflexField(optional = true)
	public String page_text;

	@ReflexField
	public String page_id;
	
	public static String process_escapes(String text)
	{
		return text.replace("\\\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\t", "\\t");
	}
	
	@SuppressWarnings("deprecation")
	public static String encode(String text)
	{
		return Base64.getEncoder().encodeToString(URLEncoder.encode(process_escapes(text)).replace("%0[aA]", "\n").getBytes());
	}
}
