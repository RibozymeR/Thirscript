package thirscript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThObject
{
	/**
	 * The initial object, from which all objects descend. Starts only with the required members: <br>
	 * - <code>_native := 0</code><br>
	 * - <code>_true</code>, which always evaluates to true<br>
	 * - <code>_</code>, the object itself
	 */
	public static final ThObject OBJECT;

	public static final Var NULL = Var.constant(null);

	static {
		OBJECT = new ThObject();
		OBJECT.vars.put("_native", Var.constant(ThInteger.FALSE));
		OBJECT.vars.put("_true", new Var(new ThConstFunction(ThInteger.TRUE)));
	}

	public final Map<String, Var> vars;

	// we only ever have one object without prototype
	private ThObject()
	{
		vars = new HashMap<>();
		vars.put("_", Var.constant(this));
	}

	public ThObject(ThObject proto, Map<String, Var> new_vars)
	{
		Var const_this = Var.constant(this);

		vars = new HashMap<>(proto.vars);
		vars.put("_native", Var.constant(ThInteger.FALSE));
		vars.put("_", const_this);
		vars.putAll(new_vars);

		// functions should have access to this specific object using _
		for(Var var: vars.values()) {
			if(var.getValue() != this && var.getValue() instanceof ThFunction func)
				func.enclosed.put("_", const_this);
		}
	}

	public long istrue()
	{
		ThObject testf = getVar("_true").getValue();

		if(!(testf instanceof IFunction) || !((IFunction) testf).can_eval(List.of()))
			throw new RuntimeException("Member true has to be a function with no arguments");

		ThObject testr = ((IFunction) testf).eval(List.of());
		if(!(testr instanceof ThInteger))
			throw new RuntimeException("Function true has to return truth value");

		return ((ThInteger) testr).value;
	}

	public Var getVar(String name)
	{
		if(!vars.containsKey(name))
			return NULL;

		return vars.get(name);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		for(var entry: vars.entrySet()) if(entry.getValue().getValue() != this) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue().getValue());
			sb.append(", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("}");
		
		return sb.toString();
	}
}
