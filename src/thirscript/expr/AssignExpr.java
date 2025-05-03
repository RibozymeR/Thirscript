package thirscript.expr;

import java.util.Map;

import thirscript.ThObject;
import thirscript.Var;

public class AssignExpr implements Expr
{
	final VarExpr var;
	final Expr value;
	final boolean constant;

	public AssignExpr(VarExpr var, Expr value, boolean constant)
	{
		if(var.var == null || var.var.equals("_"))
			throw new IllegalArgumentException("Assignment expression contains invalid variable name");
		this.var = var;
		this.value = value;
		this.constant = constant;
	}

	public ThObject eval(Map<String, Var> env)
	{
		ThObject v = value.eval(env);
		var.set(v, env, constant);
		return v;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(constant ? "(:= " : "(= ");
		sb.append(var);
		sb.append(' ');
		sb.append(value);
		sb.append(')');

		return sb.toString();
	}
}
