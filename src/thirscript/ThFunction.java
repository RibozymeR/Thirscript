package thirscript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thirscript.expr.Expr;

public class ThFunction extends ThObject implements IFunction
{
	/**
	 * Function = new(Object){ _native := TRUE }
	 */

	final Expr func;
	final String[] arg_names;
	final Map<String, Var> enclosed;

	public ThFunction(Expr func, List<String> arg_names, Map<String, Var> enclosed)
	{
		super(OBJECT, Map.of());
		this.func = func;
		this.arg_names = arg_names.toArray(new String[0]);
		this.enclosed = new HashMap<>(enclosed);
	}

	public boolean can_eval(List<ThObject> arg_types)
	{
		if(arg_types.size() != arg_names.length)
			return false;
		// TODO should not always be true? maybe?
		return true;
	}

	public ThObject eval(List<ThObject> args)
	{
		Map<String, Var> new_env = new HashMap<>(enclosed);

		for(int i = 0; i < arg_names.length; ++i)
			new_env.put(arg_names[i], new Var(args.get(i)));

		return func.eval(new_env);
	}

	public String toString()
	{
		return String.format("#(%s)%s", String.join(",", arg_names), func);
	}
}
