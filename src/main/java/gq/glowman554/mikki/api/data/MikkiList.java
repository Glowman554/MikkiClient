package gq.glowman554.mikki.api.data;

import gq.glowman554.reflex.ReflexField;
import gq.glowman554.reflex.types.ReflexCustomArray;

public class MikkiList
{
	@SuppressWarnings("unchecked")
	@ReflexField
	public ReflexCustomArray<MikkiPage> $ = ReflexCustomArray.from(() -> new MikkiPage());
}
