package thirscript;

import java.util.List;
import java.util.Map;

public class ThConstFunction extends ThObject implements IFunction
{
	final ThObject value;

	public ThConstFunction(ThObject value)
	{
		super(OBJECT, Map.of());
		this.value = value;
	}

	public boolean can_eval(List<ThObject> arg_types)
	{
		return arg_types.isEmpty();
	}

	public ThObject eval(List<ThObject> args)
	{
		return value;
	}

	public String toString()
	{
		return String.format("#()%s", value);
	}
}
