package thirscript;

import java.util.Map;
import java.util.TreeMap;

public class ThInteger extends ThObject
{
	/**
	 * integer = new(Object){ _native := true _value := ... }
	 */

	private static Map<Long, ThInteger> cache = new TreeMap<>();

	public static final ThInteger TRUE = valueOf(-1);
	public static final ThInteger FALSE = valueOf(0);

	public final long value;

	private ThInteger(long value)
	{
		super(OBJECT, Map.of());

		this.value = value;
		vars.put("_native", Var.constant(ThInteger.TRUE));
		// vars.put("_value", Var.constant(this));
	}

	public long istrue()
	{
		return value;
	}

	public String toString()
	{
		return Long.toString(value);
	}

	public static ThInteger valueOf(long v)
	{
		if(cache.containsKey(v))
			return cache.get(v);

		ThInteger i = new ThInteger(v);
		cache.put(v, i);
		return i;
	}
}
