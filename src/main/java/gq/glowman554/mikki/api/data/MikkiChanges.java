package gq.glowman554.mikki.api.data;

import gq.glowman554.reflex.ReflexField;
import gq.glowman554.reflex.types.ReflexCustomArray;

public class MikkiChanges
{
	@SuppressWarnings("unchecked")
	@ReflexField
	public ReflexCustomArray<MikkiChange> $ = ReflexCustomArray.from(() -> new MikkiChange());
}
