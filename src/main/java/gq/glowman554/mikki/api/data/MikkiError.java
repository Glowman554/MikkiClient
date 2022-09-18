package gq.glowman554.mikki.api.data;

import gq.glowman554.reflex.ReflexField;

public class MikkiError
{
	@ReflexField(optional = true)
	public String error;
	
	public void throw_if_error()
	{
		if (error != null)
		{
			throw new IllegalStateException(error);
		}
	}
}
