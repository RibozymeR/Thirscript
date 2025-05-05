package thirscript.expr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import thirscript.ThObject;
import thirscript.Var;

public class NewExpr implements Expr
{
	final Expr proto;
	final List<AssignExpr> new_vars;
	private final Set<String> var_names;

	public NewExpr(Expr proto, List<AssignExpr> var_exprs)
	{
		this.proto = proto;
		this.new_vars = var_exprs;
		var_names = new HashSet<>();
		for(AssignExpr e: new_vars)
			var_names.add(e.var.var);
	}

	public ThObject eval(Map<String, Var> env)
	{
		ThObject proto = this.proto.eval(env);
		
		Map<String, Var> new_env = new HashMap<>(env);
		for(AssignExpr assexpr: new_vars)
			assexpr.eval(new_env);
		new_env.keySet().retainAll(var_names);

		return new ThObject(proto, new_env);
	}

	public void addVar(AssignExpr ass)
	{
		new_vars.add(ass);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder("(new ");
		sb.append(proto);
		for(AssignExpr ass: new_vars) {
			sb.append(' ');
			sb.append(ass);
		}
		sb.append(')');
		return sb.toString();
	}
}
