package gq.glowman554.mikki.api.data;

import gq.glowman554.reflex.ReflexField;

public class MikkiAccount
{
	@ReflexField
	public String username;
	@ReflexField
	public String password_hash;
	@ReflexField
	public boolean editor;
	@ReflexField
	public String token;
}
