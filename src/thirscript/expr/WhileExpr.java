package thirscript.expr;

import java.util.Map;

import thirscript.ThObject;
import thirscript.Var;

public class WhileExpr implements Expr
{
	final Expr test, body;

	public WhileExpr(Expr test, Expr body)
	{
		this.test = test;
		this.body = body;
	}

	public ThObject eval(Map<String, Var> env)
	{
		ThObject v = null, t;
		while((t = test.eval(env)) != null && t.istrue() != 0)
			v = body.eval(env);
		return v;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder("(while ");
		sb.append(test);
		sb.append(' ');
		sb.append(body);
		sb.append(')');
		return sb.toString();
	}
}
