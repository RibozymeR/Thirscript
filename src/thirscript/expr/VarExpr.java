package thirscript.expr;

import java.util.List;
import java.util.Map;

import thirscript.ThObject;
import thirscript.Var;

public class VarExpr implements Expr
{
	final String var;
	final String[] subpath;

	public VarExpr(String var, List<String> sub)
	{
		this.var = var;
		this.subpath = sub == null ? new String[0] : sub.toArray(new String[0]);
	}

	public ThObject eval(Map<String, Var> env)
	{
		if(!env.containsKey(var))
			throw new RuntimeException("No variable named \"" + var + "\"");
		Var v = env.get(var);
		for(String sub: subpath) {
			ThObject o = v.getValue();
			Var nv = o == null ? ThObject.NULL : o.getVar(sub);
			if(nv == null)
				throw new RuntimeException("No variable named \"" + sub + "\" in " + v);
			v = nv;
		}
		return v.getValue();
	}

	public void set(ThObject value, Map<String, Var> env, boolean constant)
	{
		// System.out.println("set " + this + " to " + value);
		Var v = env.get(var);
		for(String sub: subpath) {
			if(v == null)
				throw new RuntimeException("No variable \"" + v);
			ThObject o = v.getValue();
			Var nv = o == null ? ThObject.NULL : o.getVar(sub);
			v = nv;
		}
		if(v != null && !constant)
			v.setValue(value);
		else
			env.put(var, Var.newVar(value, constant));
	}

	public String toString()
	{
		return subpath.length > 0 ? String.format("%s.%s", var, String.join(".", subpath)) : var;
	}
}
